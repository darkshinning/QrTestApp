package com.example.qrwithjetpack.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(

    @SerialName("id")
    val id: Int? = 0,

    @SerialName("user")
    val user: Int,

    @SerialName("qr")
    val qr: String,

    @SerialName("qr_id")
    val qrId: String,
)
