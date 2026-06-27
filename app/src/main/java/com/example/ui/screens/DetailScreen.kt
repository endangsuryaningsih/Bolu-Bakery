package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocalMall
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductRepository
import com.example.ui.BakeryViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    productId: Int,
    viewModel: BakeryViewModel,
    onBackClick: () -> Unit,
    onGoToCart: () -> Unit,
    onAddToCartSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val product = ProductRepository.getProductById(productId) ?: return
    val favorites by viewModel.favoriteProductIds.collectAsState()
    val isFavorite = favorites.contains(productId)

    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        modifier = modifier.background(CreamBackground),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Detail Produk",
                            fontWeight = FontWeight.Bold,
                            color = WarmBrown,
                            fontSize = 20.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("detail_back_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = WarmBrown
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleFavorite(productId) },
                        modifier = Modifier.testTag("detail_fav_btn")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) ErrorRed else WarmBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        },
        bottomBar = {
            // Bottom Action Bar
            Surface(
                tonalElevation = 8.dp,
                color = PureWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("detail_bottom_bar")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Small shopping bag outline button
                    OutlinedButton(
                        onClick = onGoToCart,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .size(54.dp)
                            .testTag("detail_cart_icon_btn"),
                        border = BorderStroke(1.5.dp, WarmBrown),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = WarmBrown)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalMall,
                            contentDescription = "Shopping Bag",
                            tint = WarmBrown,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Large add to cart button
                    val totalPrice = product.price * quantity
                    Button(
                        onClick = {
                            viewModel.addToCart(productId, quantity)
                            onAddToCartSuccess("${product.name} (x$quantity) ditambahkan ke keranjang")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("add_to_cart_submit_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WarmBrown)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tambah ke Keranjang",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color.White.copy(alpha = 0.3f))
                            )
                            Text(
                                text = BakeryViewModel.formatRupiah(totalPrice),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CreamBackground)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Main Large Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .testTag("detail_product_image")
            ) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Rating Badge on Image (Top Right)
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .align(Alignment.TopEnd),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Rating Star",
                        tint = AccentOrange,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Title and Price with Quantity Selector side by side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmBrown,
                        lineHeight = 32.sp,
                        modifier = Modifier.testTag("detail_product_name")
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = BakeryViewModel.formatRupiah(product.price),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOrangeBadge
                    )
                }

                // Quantity Selector: minus, count, plus
                Row(
                    modifier = Modifier
                        .background(PureWhite, shape = RoundedCornerShape(50.dp))
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SoftCream)
                            .clickable { if (quantity > 1) quantity-- }
                            .testTag("detail_qty_minus"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "−",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "$quantity",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.widthIn(min = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(WarmBrown)
                            .clickable { quantity++ }
                            .testTag("detail_qty_plus"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Badges: Favorit, Produk Segar
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(LightOrangeBadge, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Favorit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOrangeBadge
                    )
                }

                Box(
                    modifier = Modifier
                        .background(SoftCream, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Produk Segar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section "Deskripsi"
            Text(
                text = "Deskripsi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WarmBrown,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = product.description,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 22.sp,
                modifier = Modifier.testTag("detail_description")
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Section "Informasi Tambahan"
            Text(
                text = "Informasi Tambahan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WarmBrown,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ketahanan Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(105.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, SoftCream),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = "Ketahanan Icon",
                            tint = TextOrangeBadge,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Ketahanan",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = product.ketahananRoom,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = product.ketahananKulkas,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Ukuran Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(105.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, SoftCream),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange, // Simple card or box icon replacement
                            contentDescription = "Ukuran Icon",
                            tint = TextOrangeBadge,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Ukuran",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = product.ukuranPanjang,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = product.ukuranDiameterOrLebar,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
