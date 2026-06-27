package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BakeryDao {
    // Cart operations
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItem(productId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // Order operations
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
    fun getOrderById(orderId: String): Flow<Order?>

    @Update
    suspend fun updateOrder(order: Order)
}
