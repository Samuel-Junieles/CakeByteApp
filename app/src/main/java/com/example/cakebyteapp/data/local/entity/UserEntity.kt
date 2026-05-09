package com.example.cakebyteapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val name: String,
    @SerialName("password")
    val password: String = "",
    val role: String,
    val address: String = "",
    val phone: String = "",
    @SerialName("created_at")
    val createdAt: String? = null
)
