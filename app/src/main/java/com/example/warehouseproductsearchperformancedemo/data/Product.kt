package com.example.warehouseproductsearchperformancedemo.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices = [
        Index(value = ["code"]),
        Index(value = ["title"]),
        Index(value = ["barcode"])
    ]
)
data class Product(
    @PrimaryKey val id: Long,
    val code: String,
    val title: String,
    val barcode: String,
    val supplier: String,
    val stock: Double,
    val isActive: Boolean
) 