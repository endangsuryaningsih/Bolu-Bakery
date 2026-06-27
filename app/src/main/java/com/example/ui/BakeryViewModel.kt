package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class BakeryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = BakeryDatabase.getDatabase(application)
    private val repository = BakeryRepository(database.bakeryDao())

    // UI state for catalog
    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Products based on category and search
    val filteredProducts: StateFlow<List<Product>> = combine(
        _selectedCategory,
        _searchQuery
    ) { category, query ->
        ProductRepository.products.filter { product ->
            val matchesCategory = category == "Semua" || product.category.equals(category, ignoreCase = true)
            val matchesSearch = query.isEmpty() || product.name.contains(query, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductRepository.products)

    // Cart items mapped to detailed items
    val cartItemsDetails: StateFlow<List<CartItemWithProduct>> = repository.cartItems
        .map { cartList ->
            cartList.mapNotNull { cartItem ->
                val product = ProductRepository.getProductById(cartItem.productId)
                if (product != null) {
                    CartItemWithProduct(product, cartItem.quantity)
                } else null
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Price summary calculations
    val subtotal: StateFlow<Double> = cartItemsDetails.map { list ->
        list.sumOf { it.product.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val shippingFee: StateFlow<Double> = subtotal.map { sub ->
        if (sub > 100000.0 || sub == 0.0) 0.0 else 15000.0 // Free shipping above 100k
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val total: StateFlow<Double> = combine(subtotal, shippingFee) { sub, ship ->
        sub + ship
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Order checkout fields
    private val _addressName = MutableStateFlow("Rumah (Mhd Fauzan)")
    val addressName: StateFlow<String> = _addressName.asStateFlow()

    private val _addressDetail = MutableStateFlow("Jl. Yosudarso, Medan")
    val addressDetail: StateFlow<String> = _addressDetail.asStateFlow()

    private val _paymentMethod = MutableStateFlow("Transfer Bank")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    // Recent orders
    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active order for status timeline
    private val _activeOrderId = MutableStateFlow<String?>(null)
    val activeOrderId: StateFlow<String?> = _activeOrderId.asStateFlow()

    val activeOrder: StateFlow<Order?> = _activeOrderId.flatMapLatest { id ->
        if (id != null) repository.getOrderById(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Favorite Product IDs
    private val _favoriteProductIds = MutableStateFlow<Set<Int>>(setOf(1)) // Pandan roll as default favorite
    val favoriteProductIds: StateFlow<Set<Int>> = _favoriteProductIds.asStateFlow()

    fun toggleFavorite(productId: Int) {
        val current = _favoriteProductIds.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _favoriteProductIds.value = current
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addToCart(productId: Int, quantity: Int) {
        viewModelScope.launch {
            repository.addToCart(productId, quantity)
        }
    }

    fun updateCartQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(productId, quantity)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    fun updateAddress(name: String, detail: String) {
        _addressName.value = name
        _addressDetail.value = detail
    }

    fun setPaymentMethod(method: String) {
        _paymentMethod.value = method
    }

    // Checkout order
    fun placeOrder(onOrderPlaced: (String) -> Unit) {
        val currentCart = cartItemsDetails.value
        if (currentCart.isEmpty()) return

        val randNum = (10000..99999).random()
        val generatedId = "#BB-$randNum"

        val sub = subtotal.value
        val ship = shippingFee.value
        val tot = total.value
        val addrName = _addressName.value
        val addrDetail = _addressDetail.value
        val payMethod = _paymentMethod.value

        // Simple item summaries list as JSON
        val itemsSummary = currentCart.joinToString(", ") { "${it.product.name} (x${it.quantity})" }

        val newOrder = Order(
            orderId = generatedId,
            subtotal = sub,
            shippingFee = ship,
            total = tot,
            status = "DITERIMA", // Starts at DITERIMA
            timestamp = System.currentTimeMillis(),
            addressName = addrName,
            addressDetail = addrDetail,
            paymentMethod = payMethod,
            itemsJson = itemsSummary
        )

        viewModelScope.launch {
            repository.createOrder(newOrder)
            _activeOrderId.value = generatedId
            repository.clearCart()
            onOrderPlaced(generatedId)

            // Start a simulated shipping status timeline progress
            simulateOrderStatusProgress(generatedId)
        }
    }

    private fun simulateOrderStatusProgress(orderId: String) {
        viewModelScope.launch {
            // Stage 1: DITERIMA is already set
            delay(12000) // Wait 12s, then transition to DISIAPKAN
            updateOrderStage(orderId, "DISIAPKAN")

            delay(15000) // Wait 15s, then transition to PENGIRIMAN
            updateOrderStage(orderId, "PENGIRIMAN")

            delay(18000) // Wait 18s, then transition to SAMPAI
            updateOrderStage(orderId, "SAMPAI")
        }
    }

    private suspend fun updateOrderStage(orderId: String, nextStatus: String) {
        val orderFlow = repository.getOrderById(orderId)
        val currentOrder = orderFlow.first()
        if (currentOrder != null) {
            val updated = currentOrder.copy(status = nextStatus)
            repository.updateOrder(updated)
        }
    }

    fun setActiveOrder(orderId: String) {
        _activeOrderId.value = orderId
    }

    companion object {
        fun formatRupiah(amount: Double): String {
            val formatSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
                groupingSeparator = '.'
                decimalSeparator = ','
            }
            val formatter = DecimalFormat("Rp #,###", formatSymbols)
            return formatter.format(amount).replace("Rp ", "Rp ")
        }
    }
}

data class CartItemWithProduct(
    val product: Product,
    val quantity: Int
)

class BakeryViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BakeryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BakeryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
