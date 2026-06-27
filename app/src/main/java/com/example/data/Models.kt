package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.R

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val rating: Double,
    val sold: Int,
    val category: String,
    val imageRes: Int,
    val ketahananRoom: String,
    val ketahananKulkas: String,
    val ukuranPanjang: String,
    val ukuranDiameterOrLebar: String
)

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val productId: Int,
    val quantity: Int
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val orderId: String,
    val subtotal: Double,
    val shippingFee: Double,
    val total: Double,
    val status: String, // "DITERIMA", "DISIAPKAN", "PENGIRIMAN", "SAMPAI"
    val timestamp: Long,
    val addressName: String,
    val addressDetail: String,
    val paymentMethod: String,
    val itemsJson: String // Serialized CartItem list or item summaries
)

object ProductRepository {
    val products = listOf(
        Product(
            id = 1,
            name = "Bolu Gulung Pandan",
            price = 45000.0,
            description = "Nikmati kelembutan Bolu Gulung Pandan khas Bolu Bakery yang dibuat dengan ekstrak daun pandan asli untuk aroma yang autentik. Setiap gulungan memiliki tekstur yang sangat lembut dan moist, berpadu sempurna dengan filling krim kelapa manis yang gurih dan melimpah di dalamnya.\n\nDibuat tanpa bahan pengawet dan hanya menggunakan bahan-bahan pilihan berkualitas tinggi untuk menjaga cita rasa tradisional yang premium. Cocok sebagai teman minum teh sore hari atau hantaran spesial untuk keluarga tercinta.",
            rating = 4.9,
            sold = 234,
            category = "Bolu Roll",
            imageRes = R.drawable.img_pandan_roll,
            ketahananRoom = "3 Hari (Suhu Ruang)",
            ketahananKulkas = "7 Hari (Kulkas)",
            ukuranPanjang = "Panjang 22cm",
            ukuranDiameterOrLebar = "Diameter 8cm"
        ),
        Product(
            id = 2,
            name = "Brownies Coklat Premium",
            price = 55000.0,
            description = "Nikmati kelezatan Brownies Coklat Premium yang super fudgy dan padat dengan rasa coklat Belgia premium yang intens. Dihiasi dengan limpahan chocolate chips berkualitas tinggi di bagian luar untuk memberikan tekstur renyah di setiap gigitannya.\n\nDibuat tanpa bahan pengawet untuk mempertahankan cita rasa asli yang kaya. Sangat cocok dinikmati bersama secangkir kopi hangat, teh, atau sebagai hidangan penutup mewah bersama keluarga.",
            rating = 4.8,
            sold = 189,
            category = "Brownies",
            imageRes = R.drawable.img_brownies,
            ketahananRoom = "5 Hari (Suhu Ruang)",
            ketahananKulkas = "10 Hari (Kulkas)",
            ukuranPanjang = "Panjang 20cm",
            ukuranDiameterOrLebar = "Lebar 10cm"
        ),
        Product(
            id = 3,
            name = "Bolu Kukus Merah",
            price = 35000.0,
            description = "Bolu Kukus Merah tradisional yang mekar sempurna seperti bunga sakura. Dibuat dengan resep warisan yang mempertahankan tekstur yang luar biasa lembut, ringan, dan tidak seret di tenggorokan.\n\nWarna merah yang cantik alami dipadukan dengan aroma vanila yang wangi dan manis yang pas, menjadikannya kue hantaran atau cemilan keluarga yang sangat digemari.",
            rating = 4.7,
            sold = 92,
            category = "Bolu Roll",
            imageRes = R.drawable.img_bolu_kukus,
            ketahananRoom = "2 Hari (Suhu Ruang)",
            ketahananKulkas = "5 Hari (Kulkas)",
            ukuranPanjang = "Tinggi 8cm",
            ukuranDiameterOrLebar = "Diameter 10cm"
        ),
        Product(
            id = 4,
            name = "Lapis Surabaya",
            price = 120000.0,
            description = "Lapis Surabaya premium legendaris dengan resep kuno kaya akan kuning telur yang memberikan tekstur super lembut dan rasa yang sangat gurih-manis. Terdiri dari lapisan bolu kuning vanila dan bolu coklat yang disatukan oleh selai stroberi segar premium.\n\nSetiap gigitan menawarkan kemewahan cita rasa tradisional yang tak terlupakan. Sangat cocok sebagai hantaran hari raya, ulang tahun, atau perayaan istimewa.",
            rating = 5.0,
            sold = 156,
            category = "Brownies", // Just grouping for easy filtering or list
            imageRes = R.drawable.img_lapis_surabaya,
            ketahananRoom = "4 Hari (Suhu Ruang)",
            ketahananKulkas = "8 Hari (Kulkas)",
            ukuranPanjang = "Panjang 22cm",
            ukuranDiameterOrLebar = "Lebar 22cm"
        )
    )

    fun getProductById(id: Int): Product? {
        return products.find { it.id == id }
    }
}
