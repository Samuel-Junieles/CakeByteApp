package com.example.cakebyteapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val quantity: Int,
    val size: String,
    val note: String? = null,
    val imageUrl: String? = null
)
