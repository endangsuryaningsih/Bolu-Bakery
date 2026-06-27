package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BakeryViewModel
import com.example.ui.CartItemWithProduct
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: BakeryViewModel,
    onBackClick: () -> Unit,
    onOrderPlaced: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItemsDetails.collectAsState()
    val subtotal by viewModel.subtotal.collectAsState()
    val shippingFee by viewModel.shippingFee.collectAsState()
    val total by viewModel.total.collectAsState()

    val addressName by viewModel.addressName.collectAsState()
    val addressDetail by viewModel.addressDetail.collectAsState()
    val paymentMethod by viewModel.paymentMethod.collectAsState()

    var showAddressDialog by remember { mutableStateOf(false) }
    var tempAddressName by remember { mutableStateOf(addressName) }
    var tempAddressDetail by remember { mutableStateOf(addressDetail) }

    // Address Change Dialog
    if (showAddressDialog) {
        AlertDialog(
            onDismissRequest = { showAddressDialog = false },
            title = { Text("Ubah Alamat Pengiriman", fontWeight = FontWeight.Bold, color = WarmBrown) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = tempAddressName,
                        onValueChange = { tempAddressName = it },
                        label = { Text("Label Alamat") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tempAddressDetail,
                        onValueChange = { tempAddressDetail = it },
                        label = { Text("Detail Alamat") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateAddress(tempAddressName, tempAddressDetail)
                        showAddressDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmBrown)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddressDialog = false }) {
                    Text("Batal", color = TextSecondary)
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.background(CreamBackground),
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Keranjang Belanja",
                            fontWeight = FontWeight.Bold,
                            color = WarmBrown,
                            fontSize = 20.sp
                        )
                        val totalSelected = cartItems.sumOf { it.quantity }
                        Text(
                            text = "$totalSelected produk dipilih",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("cart_back_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = WarmBrown
                        )
                    }
                },
                actions = {
                    // Empty spacer to balance navigation icon
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp,
                    color = PureWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.placeOrder { orderId ->
                                    onOrderPlaced(orderId)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("checkout_submit_btn"),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WarmBrown)
                        ) {
                            Text(
                                text = "Lanjut ke Pemesanan  •  ${BakeryViewModel.formatRupiah(total)}",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(CreamBackground),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🛒", fontSize = 64.sp)
                    Text(
                        text = "Keranjang Belanja Kosong",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmBrown
                    )
                    Text(
                        text = "Ayo tambahkan kue lezat untuk memulai!",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(CreamBackground),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cart Items List
                items(cartItems, key = { it.product.id }) { item ->
                    CartItemCard(
                        item = item,
                        onQuantityChange = { newQty ->
                            viewModel.updateCartQuantity(item.product.id, newQty)
                        },
                        onDelete = {
                            viewModel.removeFromCart(item.product.id)
                        }
                    )
                }

                // Delivery Address Header & Card
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Alamat Pengiriman",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarmBrown,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("address_card"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SoftCream)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.LocationOn,
                                    contentDescription = "Address Icon",
                                    tint = TextOrangeBadge,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = addressName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = addressDetail,
                                        fontSize = 13.sp,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    text = "Ubah",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextOrangeBadge,
                                    modifier = Modifier
                                        .clickable {
                                            tempAddressName = addressName
                                            tempAddressDetail = addressDetail
                                            showAddressDialog = true
                                        }
                                        .padding(8.dp)
                                        .testTag("change_address_btn")
                                )
                            }
                        }
                    }
                }

                // Payment Methods
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Metode Pembayaran",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarmBrown,
                            modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
                        )

                        val paymentOptions = listOf(
                            Triple("Transfer Bank", Icons.Outlined.AccountBalance, "transfer_bank"),
                            Triple("Bayar di Tempat (COD)", Icons.Outlined.Payment, "cod"),
                            Triple("QRIS / E-Wallet", Icons.Outlined.QrCodeScanner, "qris")
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            paymentOptions.forEach { (option, icon, tag) ->
                                val isSelected = paymentMethod == option
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.dp,
                                            color = if (isSelected) WarmBrown else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.setPaymentMethod(option) }
                                        .testTag("payment_option_$tag"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) SoftCream else PureWhite
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = option,
                                                tint = WarmBrown,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = option,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        }
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { viewModel.setPaymentMethod(option) },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = WarmBrown,
                                                unselectedColor = TextSecondary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Price Summary
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .testTag("summary_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftCream)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Ringkasan Harga",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Subtotal", fontSize = 14.sp, color = TextSecondary)
                                Text(
                                    text = BakeryViewModel.formatRupiah(subtotal),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Ongkos Kirim", fontSize = 14.sp, color = TextSecondary)
                                if (shippingFee == 0.0) {
                                    Text(
                                        text = "GRATIS",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                } else {
                                    Text(
                                        text = BakeryViewModel.formatRupiah(shippingFee),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(TextSecondary.copy(alpha = 0.2f))
                                    .padding(vertical = 4.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = BakeryViewModel.formatRupiah(total),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextOrangeBadge
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItemWithProduct,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cart_item_card_${item.product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Image(
                painter = painterResource(id = item.product.imageRes),
                contentDescription = item.product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text info & Selector
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.product.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = BakeryViewModel.formatRupiah(item.product.price),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextOrangeBadge
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Quantity Row
                Row(
                    modifier = Modifier
                        .background(SoftCream, shape = RoundedCornerShape(50.dp))
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }
                            .testTag("cart_item_minus_${item.product.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "−",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "${item.quantity}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.widthIn(min = 10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(WarmBrown)
                            .clickable { onQuantityChange(item.quantity + 1) }
                            .testTag("cart_item_plus_${item.product.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Delete trash button on the right
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .testTag("cart_item_delete_${item.product.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = ErrorRed,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
