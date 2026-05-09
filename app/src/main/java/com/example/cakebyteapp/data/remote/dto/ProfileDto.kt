package com.example.cakebyteapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("role")
    val role: String,
    @SerialName("address")
    val address: String = "",
    @SerialName("phone")
    val phone: String = ""
)
