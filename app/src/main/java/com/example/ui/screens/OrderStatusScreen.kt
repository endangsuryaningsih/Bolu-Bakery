package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Order
import com.example.ui.BakeryViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusScreen(
    viewModel: BakeryViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeOrder by viewModel.activeOrder.collectAsState()

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
                            text = "Status Pesanan",
                            fontWeight = FontWeight.Bold,
                            color = WarmBrown,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Pesanan kamu sedang diproses",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("status_back_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = WarmBrown
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Help */ }) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Help",
                            tint = WarmBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        }
    ) { innerPadding ->
        if (activeOrder == null) {
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
                    Text("📦", fontSize = 64.sp)
                    Text(
                        text = "Tidak ada pesanan aktif",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmBrown
                    )
                    Text(
                        text = "Silakan pesan kue di keranjang terlebih dahulu.",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            val order = activeOrder!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(CreamBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Orange Banner: Transaksi Berhasil!
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("success_banner_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF98E45)) // Precise orange from image
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Checkmark Circle
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Success",
                                    tint = Color(0xFFF98E45),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Text(
                            text = "Transaksi Berhasil!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Pembayaran telah dikonfirmasi",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        // Pill with Price
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(50.dp))
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = BakeryViewModel.formatRupiah(order.total),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Order Number Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("order_id_card"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, SoftCream),
                    colors = CardDefaults.cardColors(containerColor = PureWhite)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "NOMOR PESANAN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = order.orderId,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        // LUNAS Badge
                        Box(
                            modifier = Modifier
                                .background(LightRedBadge, shape = RoundedCornerShape(50.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "LUNAS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ErrorRed
                            )
                        }
                    }
                }

                // Shipping Timeline
                Text(
                    text = "Status Pengiriman",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = WarmBrown,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("timeline_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = BorderStroke(1.dp, SoftCream)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Let's draw 4 stages
                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val orderTime = timeFormat.format(Date(order.timestamp))
                        val prepTime = timeFormat.format(Date(order.timestamp + 4 * 60 * 1000)) // +4 mins
                        val shipTime = timeFormat.format(Date(order.timestamp + 19 * 60 * 1000)) // +19 mins

                        val stages = listOf(
                            TimelineStage(
                                title = "Pesanan Diterima",
                                desc = "Toko mengonfirmasi pesanan anda",
                                time = orderTime,
                                statusCode = "DITERIMA"
                            ),
                            TimelineStage(
                                title = "Sedang Disiapkan",
                                desc = "Kue sedang dikemas dengan rapi oleh tim kami",
                                time = prepTime,
                                statusCode = "DISIAPKAN"
                            ),
                            TimelineStage(
                                title = "Dalam Pengiriman",
                                desc = "Kurir sedang menuju lokasi anda",
                                time = shipTime,
                                statusCode = "PENGIRIMAN"
                            ),
                            TimelineStage(
                                title = "Pesanan Sampai",
                                desc = "Nikmati bolu hangat anda!",
                                time = "",
                                statusCode = "SAMPAI"
                            )
                        )

                        // Helper function to check if stage is active/passed
                        val currentStatusIndex = when (order.status) {
                            "DITERIMA" -> 0
                            "DISIAPKAN" -> 1
                            "PENGIRIMAN" -> 2
                            "SAMPAI" -> 3
                            else -> 0
                        }

                        stages.forEachIndexed { index, stage ->
                            val isCompleted = index <= currentStatusIndex
                            val isLast = index == stages.size - 1

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = if (isLast) 0.dp else 16.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Timeline Circle & line
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.width(32.dp)
                                ) {
                                    // Circle Icon
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(if (isCompleted) AccentOrange else Color.Transparent)
                                            .border(
                                                width = if (isCompleted) 0.dp else 2.dp,
                                                color = if (isCompleted) Color.Transparent else TextSecondary.copy(alpha = 0.4f),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isCompleted) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Done",
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }

                                    // Line to next node
                                    if (!isLast) {
                                        Box(
                                            modifier = Modifier
                                                .width(2.dp)
                                                .height(44.dp)
                                                .background(
                                                    if (index < currentStatusIndex) AccentOrange else TextSecondary.copy(
                                                        alpha = 0.2f
                                                    )
                                                )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // Timeline Text details
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stage.title,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCompleted) TextPrimary else TextSecondary
                                        )

                                        if (stage.time.isNotEmpty() && isCompleted) {
                                            Text(
                                                text = stage.time,
                                                fontSize = 13.sp,
                                                color = TextSecondary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = stage.desc,
                                        fontSize = 13.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // Courier Card Ahmad Junaedi
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("courier_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftCream)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Courier Avatar Icon/Initials
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(WarmBrown),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "AJ",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Kurir Anda",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Ahmad Junaedi",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            // Call & Chat buttons
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .clickable { /* Call courier */ },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Phone,
                                        contentDescription = "Call",
                                        tint = TextPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .clickable { /* Message courier */ },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Chat,
                                        contentDescription = "Chat",
                                        tint = TextPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom Stylized Mini Map Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(125.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE2D6CA)) // Soft brown background representing map
                        ) {
                            // Drawing map details using pure compose
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                // Draw some road lines
                                val roadColor = Color(0xFFF3EBE3)
                                drawLine(
                                    color = roadColor,
                                    start = androidx.compose.ui.geometry.Offset(0f, size.height * 0.5f),
                                    end = androidx.compose.ui.geometry.Offset(size.width, size.height * 0.7f),
                                    strokeWidth = 36f
                                )
                                drawLine(
                                    color = roadColor,
                                    start = androidx.compose.ui.geometry.Offset(size.width * 0.3f, 0f),
                                    end = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height),
                                    strokeWidth = 30f
                                )
                            }

                            // Courier moving animation
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterStart)
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.LocalShipping,
                                    contentDescription = "Courier Mini",
                                    tint = AccentOrange,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Kurir sedang jalan",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            // "Lihat Peta" Button Overlayed at Center
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .background(Color.White, shape = RoundedCornerShape(50.dp))
                                    .border(1.dp, SoftCream, shape = RoundedCornerShape(50.dp))
                                    .clickable { /* Open map action */ }
                                    .padding(horizontal = 18.dp, vertical = 8.dp)
                                    .testTag("view_map_btn"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Lihat Peta",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

data class TimelineStage(
    val title: String,
    val desc: String,
    val time: String,
    val statusCode: String
)
