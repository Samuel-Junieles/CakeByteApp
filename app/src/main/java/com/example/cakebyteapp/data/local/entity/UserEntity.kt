package com.example.cakebyteapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val name: String,
    val password: String, // En un entorno real, esto iría cifrado
    val role: String,     // "Admin", "Vendedor", "Comprador"
    val createdAt: Long = System.currentTimeMillis()
)