package com.example.cakebyteapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val stock: Int,
    val status: String, // "Activo" or "Suspendido"
    val imageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
