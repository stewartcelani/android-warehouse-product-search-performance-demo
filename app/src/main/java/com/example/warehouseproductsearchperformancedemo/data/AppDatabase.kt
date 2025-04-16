package com.example.warehouseproductsearchperformancedemo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "warehouse_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Create additional indices for performance
                        db.execSQL("CREATE INDEX IF NOT EXISTS idx_product_code ON products(code)")
                        db.execSQL("CREATE INDEX IF NOT EXISTS idx_product_title ON products(title)")
                        db.execSQL("CREATE INDEX IF NOT EXISTS idx_product_barcode ON products(barcode)")
                        
                        // For the barcode scan use case, create a specialized index
                        db.execSQL("CREATE INDEX IF NOT EXISTS idx_product_barcode_exact ON products(barcode)")
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 