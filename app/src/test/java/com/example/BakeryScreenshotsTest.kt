package com.example

import android.app.Application
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.example.data.*
import com.example.ui.BakeryViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class BakeryScreenshotsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context
    private lateinit var viewModel: BakeryViewModel
    private lateinit var dao: BakeryDao

    @Before
    fun setUp() = runBlocking {
        context = ApplicationProvider.getApplicationContext()
        viewModel = BakeryViewModel(context as Application)
        val database = BakeryDatabase.getDatabase(context)
        dao = database.bakeryDao()

        // Clear database before each test to have consistent screenshots
        dao.clearCart()
        // Delete orders or clear them if possible. Let's just insert specific ones if needed.
    }

    @Test
    fun capture_1_home_screen() {
        composeTestRule.setContent {
            MyApplicationTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onProductClick = {},
                    onCartQuickAdd = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/1_home_screen.png")
    }

    @Test
    fun capture_2_detail_screen() {
        composeTestRule.setContent {
            MyApplicationTheme {
                DetailScreen(
                    productId = 1,
                    viewModel = viewModel,
                    onBackClick = {},
                    onGoToCart = {},
                    onAddToCartSuccess = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/2_detail_screen.png")
    }

    @Test
    fun capture_3_cart_screen() = runBlocking {
        // Seed cart items to make the screen look fully populated and nice
        dao.insertCartItem(CartItem(productId = 1, quantity = 2)) // 2x Bolu Gulung Pandan
        dao.insertCartItem(CartItem(productId = 2, quantity = 1)) // 1x Brownies Coklat Premium

        // Wait for Compose to apply state updates from the flows
        composeTestRule.setContent {
            MyApplicationTheme {
                CartScreen(
                    viewModel = viewModel,
                    onBackClick = {},
                    onOrderPlaced = {}
                )
            }
        }
        
        composeTestRule.awaitIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/3_cart_screen.png")
    }

    @Test
    fun capture_4_order_status_screen() = runBlocking {
        val timestamp = System.currentTimeMillis() - 10 * 60 * 1000 // 10 minutes ago
        val mockOrderId = "#BB-73892"
        val mockOrder = Order(
            orderId = mockOrderId,
            subtotal = 145000.0,
            shippingFee = 0.0,
            total = 145000.0,
            status = "PENGIRIMAN", // Match the dynamic "Dalam Pengiriman" or "Sedang Disiapkan" state
            timestamp = timestamp,
            addressName = "Rumah (Mhd Fauzan)",
            addressDetail = "Jl. Yosudarso, Medan",
            paymentMethod = "Transfer Bank",
            itemsJson = "Bolu Gulung Pandan (x2), Brownies Coklat Premium (x1)"
        )

        dao.insertOrder(mockOrder)
        viewModel.setActiveOrder(mockOrderId)

        composeTestRule.setContent {
            MyApplicationTheme {
                OrderStatusScreen(
                    viewModel = viewModel,
                    onBackClick = {}
                )
            }
        }

        composeTestRule.awaitIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/4_order_status_screen.png")
    }

    @Test
    fun capture_5_profile_screen() {
        composeTestRule.setContent {
            MyApplicationTheme {
                ProfileScreen(
                    viewModel = viewModel
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/5_profile_screen.png")
    }
}
