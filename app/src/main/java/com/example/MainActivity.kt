package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.BakeryViewModel
import com.example.ui.BakeryViewModelFactory
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppEntry()
            }
        }
    }
}

@Composable
fun MainAppEntry() {
    val context = LocalContext.current
    val viewModel: BakeryViewModel = viewModel(
        factory = BakeryViewModelFactory(context.applicationContext as android.app.Application)
    )

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // SnackBar host for adding to cart feedback
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Determine if we should show Bottom Navigation
    val showBottomBar = currentRoute in listOf(
        "home",
        "orders",
        "profile",
        "status/{orderId}"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                BakeryBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (route == "status") {
                            // If user clicks Orders but there is an active order, go to active order status,
                            // otherwise go to orders list!
                            val activeId = viewModel.activeOrderId.value
                            if (activeId != null) {
                                navController.navigate("status/$activeId") {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            } else {
                                navController.navigate("orders") {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        } else {
                            navController.navigate(route) {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home Screen
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onProductClick = { productId ->
                        navController.navigate("detail/$productId")
                    },
                    onCartQuickAdd = { product ->
                        viewModel.addToCart(product.id, 1)
                        Toast.makeText(context, "${product.name} ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Detail Screen
            composable(
                route = "detail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 1
                DetailScreen(
                    productId = productId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onGoToCart = { navController.navigate("cart") },
                    onAddToCartSuccess = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        navController.popBackStack() // Go back after success
                    }
                )
            }

            // Cart Screen
            composable("cart") {
                CartScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onOrderPlaced = { orderId ->
                        navController.navigate("status/$orderId") {
                            // Clear cart from backstack
                            popUpTo("home")
                        }
                    }
                )
            }

            // Orders List Screen
            composable("orders") {
                OrdersListScreen(
                    viewModel = viewModel,
                    onOrderClick = { orderId ->
                        viewModel.setActiveOrder(orderId)
                        navController.navigate("status/$orderId")
                    }
                )
            }

            // Status Timeline Screen
            composable(
                route = "status/{orderId}",
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                LaunchedEffect(orderId) {
                    if (orderId.isNotEmpty()) {
                        viewModel.setActiveOrder(orderId)
                    }
                }
                OrderStatusScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.navigate("orders") {
                            popUpTo("home")
                        }
                    }
                )
            }

            // Profile Screen
            composable("profile") {
                ProfileScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BakeryBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    // Custom styled Bottom Navigation that closely matches the design in screenshots
    NavigationBar(
        containerColor = PureWhite,
        tonalElevation = 8.dp,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(72.dp)
            .testTag("bakery_bottom_nav")
    ) {
        val navItems = listOf(
            BottomNavItem("Home", "home", Icons.Filled.Home, Icons.Outlined.Home),
            BottomNavItem("Orders", "orders", Icons.Filled.ListAlt, Icons.Outlined.ListAlt),
            BottomNavItem("Cart", "cart", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
            BottomNavItem("Profile", "profile", Icons.Filled.Person, Icons.Outlined.Person)
        )

        navItems.forEach { item ->
            // Highlight orders tab also if we are on order status screen, to match image 3!
            val isSelected = when (item.route) {
                "orders" -> currentRoute == "orders" || (currentRoute?.startsWith("status") == true)
                else -> currentRoute == item.route
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (item.route == "orders" && currentRoute?.startsWith("status") == true) {
                        // Stay on status or go to list
                        onNavigate("orders")
                    } else {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    // Custom rounded capsule highlight for selected item to match original aesthetics
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(AccentOrange)
                                .padding(horizontal = 20.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.filledIcon,
                                contentDescription = item.label,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.outlinedIcon,
                            contentDescription = item.label,
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TextPrimary else TextSecondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent // Disable default Material 3 capsule
                ),
                modifier = Modifier.testTag("nav_item_${item.route}")
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
)
