package com.example.warehouseproductsearchperformancedemo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE code LIKE :searchQuery OR title LIKE :searchQuery OR barcode LIKE :searchQuery LIMIT 100")
    suspend fun searchProductsRaw(searchQuery: String): List<Product>
    
    @Transaction
    suspend fun searchProducts(searchQuery: String): List<Product> {
        return searchProductsRaw(searchQuery)
    }
    
    // For performance testing with different query types
    @Query("SELECT * FROM products WHERE code LIKE :searchQuery LIMIT 100")
    suspend fun searchByCodeOnly(searchQuery: String): List<Product>
    
    @Query("SELECT * FROM products WHERE title LIKE :searchQuery LIMIT 100")
    suspend fun searchByTitleOnly(searchQuery: String): List<Product>
    
    @Query("SELECT * FROM products WHERE barcode LIKE :searchQuery LIMIT 100")
    suspend fun searchByBarcodeOnly(searchQuery: String): List<Product>
    
    // Specialized search for exact barcode match (for scanning use case)
    @Query("SELECT * FROM products WHERE barcode = :exactBarcode LIMIT 1")
    suspend fun findProductByExactBarcode(exactBarcode: String): Product?
    
    @Insert
    suspend fun insertAll(products: List<Product>)
    
    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int
} 