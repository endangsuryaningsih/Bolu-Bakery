package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartItem::class, Order::class], version = 1, exportSchema = false)
abstract class BakeryDatabase : RoomDatabase() {
    abstract fun bakeryDao(): BakeryDao

    companion object {
        @Volatile
        private var INSTANCE: BakeryDatabase? = null

        fun getDatabase(context: Context): BakeryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BakeryDatabase::class.java,
                    "bakery_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
