package com.example.cakebyteapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerName: String,
    val itemsSummary: String,
    val totalPrice: Double,
    val status: String, // "Pendiente", "Entregado"
    val createdAt: Long = System.currentTimeMillis()
)
