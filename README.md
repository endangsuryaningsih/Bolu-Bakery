# 🍰 Cute Bakery - Premium Cake Ordering App

Cute Bakery is a visually stunning, premium Android application crafted with **Kotlin** and **Jetpack Compose**. It replicates a cozy, modern, and high-quality bakery online shop experience. The application features elegant Material 3 typography, spacious layout padding, cohesive color schemes, custom photorealistic AI-generated visual assets, and local SQLite data persistence with Android Room.

---

## 🎨 Visual Identity & Theme

The interface utilizes a custom warm bakery color palette that aligns perfectly with a premium brand aesthetic:
*   **Cosmic Cream Background (`#FCF6F0`)**: A warm, cozy backdrop that reduces visual fatigue and presents items beautifully.
*   **Rich Warm Brown (`#5D2E14`)**: Used as the primary color for prominent headings, buttons, and central brand texts.
*   **Vibrant Accent Orange (`#EE8733`)**: Highlights interactive badges, timelines, active states, and successful checkout milestones.
*   **Pure White (`#FFFFFF`)**: Seamless card surfaces offering strong, elegant contrast against the cream backdrop.

---

## ✨ Features & Functionality

The application consists of 5 highly cohesive, fully functional user journeys:

1.  **🏪 Interactive Catalog (Home)**
    *   **Live Location Header**: Displays user's current shipping location.
    *   **Live Product Search**: Search through premium items dynamically in real-time.
    *   **Hero Promo Banner**: Displays active discount offers (such as "Gratis Ongkir > 100k") accompanied by a countdown timer.
    *   **Filter Categories**: Seamlessly filter products via smooth modern chips: *Semua*, *Bolu Roll*, and *Brownies*.
    *   **Grid Cards**: Cards featuring real ratings (e.g. ⭐ 4.9), number of items sold, and an orange immediate add-to-cart button.

2.  **🍰 Detailed Product Screen**
    *   **Dynamic Image & Rating Overlay**: High-resolution image of the product featuring a rating badge.
    *   **Flexible Quantity Selector**: Add or subtract items with live total price computation before adding to the shopping cart.
    *   **Favoriting Toggle**: Tap to add/remove a product from your local favorites (backed by persistent state).
    *   **Product Information Cards**: Detail-rich tables outlining **Ketahanan** (item shelf life on room temp vs. fridge) and **Ukuran** (exact physical dimensions of the cake).

3.  **🛒 Shopping Cart & Checkout**
    *   **Itemized Listing**: View all chosen cakes, adjust quantities directly, or tap the delete icon to remove items.
    *   **Delivery Address Selection**: Card with a quick "Ubah" (Edit) button that displays an elegant modal dialog to change the recipient's name and detail address.
    *   **Payment Gateway Methods**: Choose payment channels like **Transfer Bank**, **Bayar di Tempat (COD)**, or **QRIS / E-Wallet** with beautiful modern radio highlights.
    *   **Price Breakdown summary**: Automatically calculates Subtotal, Promo Shipping Fees (marked as **GRATIS** above 100k), and final absolute Total.

4.  **📦 Order Status & Timeline Tracking**
    *   **Celebration Header Banner**: Large modern orange checkmark banner acknowledging your transaction's success.
    *   **Unique Invoice Number**: Automatically generates receipt codes starting with `#BB-xxxxx`.
    *   **Dynamic Shipping Timeline**: Displays active real-time stages: *Pesanan Diterima* ➔ *Sedang Disiapkan* ➔ *Dalam Pengiriman* ➔ *Pesanan Sampai*.
    *   **Live Simulated Status Updates**: In the background, the app utilizes coroutines to periodically transition through preparation stages, shifting timeline highlights and status icons automatically.
    *   **Courier Contact details**: Connect with your courier (Ahmad Junaedi) with options to call or message, plus a customized schematic mini-map drawn with Compose Canvas highlighting the moving delivery vector.

5.  **👤 User Profile**
    *   Personalized greeting and avatar.
    *   Displays current active shipping location, contact channels, and help documents.

---

## 🛠️ Tech Stack & Architecture

*   **Jetpack Compose**: 100% declarative modern UI implementation following Material 3 guidelines.
*   **Kotlin Coroutines & Flow**: For seamless asynchronous operations, real-time database state observation, and order lifecycle simulations.
*   **Room Database**: Core SQLite local database caching cart items and historical checkout logs permanently.
*   **Navigation Compose**: Safe and type-robust screen transitions managing the backstack.
*   **Robolectric & Roborazzi**: Configured JVM-level screenshot and visual regression testing ensuring pixel-perfect interface preservation.

---

## 📸 Automated Screen Capture & Verification

Cute Bakery includes fully automated visual screenshot verification tests. You can generate or verify pixel-perfect design files directly from the terminal without requiring a physical emulator or device.

To trigger the automated screenshot generation:
```bash
gradle :app:recordRoborazziDebug
```

Screenshots are recorded and structured in `/app/src/test/screenshots/` as:
1.  `1_home_screen.png` - Home catalog with hero banner and cake grid.
2.  `2_detail_screen.png` - Pandan Swiss Roll product details with quantity controls.
3.  `3_cart_screen.png` - Populated shopping bag, shipping form, and payment methods.
4.  `4_order_status_screen.png` - Checkmark payment banner, timeline status progression, and map.
5.  `5_profile_screen.png` - Personalized profile and administrative sections.

To verify existing UI consistency:
```bash
gradle :app:verifyRoborazziDebug
```

---

## 🚀 How to Run locally

1.  Clone this repository or download the source code.
2.  Open the project directory inside **Android Studio** (Koala or newer recommended).
3.  Sync Gradle dependencies.
4.  Run on any compatible physical device or Virtual Emulator running **Android API 24+** (SDK 34-36 fully supported).
