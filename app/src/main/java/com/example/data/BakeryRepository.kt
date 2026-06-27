package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class BakeryRepository(private val bakeryDao: BakeryDao) {
    val cartItems: Flow<List<CartItem>> = bakeryDao.getCartItems()
    val allOrders: Flow<List<Order>> = bakeryDao.getAllOrders()

    suspend fun addToCart(productId: Int, quantity: Int) {
        val currentCart = cartItems.first()
        val existingItem = currentCart.find { it.productId == productId }
        if (existingItem != null) {
            bakeryDao.insertCartItem(CartItem(productId, existingItem.quantity + quantity))
        } else {
            bakeryDao.insertCartItem(CartItem(productId, quantity))
        }
    }

    suspend fun updateCartQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            bakeryDao.deleteCartItem(productId)
        } else {
            bakeryDao.insertCartItem(CartItem(productId, quantity))
        }
    }

    suspend fun removeFromCart(productId: Int) {
        bakeryDao.deleteCartItem(productId)
    }

    suspend fun clearCart() {
        bakeryDao.clearCart()
    }

    suspend fun createOrder(order: Order) {
        bakeryDao.insertOrder(order)
    }

    fun getOrderById(orderId: String): Flow<Order?> {
        return bakeryDao.getOrderById(orderId)
    }

    suspend fun updateOrder(order: Order) {
        bakeryDao.updateOrder(order)
    }
}
