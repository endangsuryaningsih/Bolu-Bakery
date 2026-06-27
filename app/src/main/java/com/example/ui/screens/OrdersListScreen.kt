package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Order
import com.example.ui.BakeryViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersListScreen(
    viewModel: BakeryViewModel,
    onOrderClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.allOrders.collectAsState()

    Scaffold(
        modifier = modifier.background(CreamBackground),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Riwayat Pesanan",
                            fontWeight = FontWeight.Bold,
                            color = WarmBrown,
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        }
    ) { innerPadding ->
        if (orders.isEmpty()) {
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
                    Text("📋", fontSize = 64.sp)
                    Text(
                        text = "Belum Ada Pesanan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmBrown
                    )
                    Text(
                        text = "Riwayat pesanan kue kamu akan muncul di sini.",
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders, key = { it.orderId }) { order ->
                    OrderHistoryCard(
                        order = order,
                        onClick = { onOrderClick(order.orderId) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(
    order: Order,
    onClick: () -> Unit
) {
    val timeFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val orderDateString = timeFormat.format(Date(order.timestamp))

    val displayStatus = when (order.status) {
        "DITERIMA" -> "Pesanan Diterima"
        "DISIAPKAN" -> "Sedang Disiapkan"
        "PENGIRIMAN" -> "Dalam Pengiriman"
        "SAMPAI" -> "Selesai"
        else -> "Diproses"
    }

    val statusColor = when (order.status) {
        "SAMPAI" -> SuccessGreen
        else -> AccentOrange
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("order_history_card_${order.orderId}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.orderId,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = orderDateString,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                // Status Text Badge
                Text(
                    text = displayStatus,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            Divider(color = SoftCream)

            // Items Json
            Text(
                text = order.itemsJson,
                fontSize = 13.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Pembayaran",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Text(
                    text = BakeryViewModel.formatRupiah(order.total),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = WarmBrown
                )
            }
        }
    }
}
