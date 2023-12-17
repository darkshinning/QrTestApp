package com.example.qrwithjetpack.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QRDto(

    @SerialName("id")
    val id: Int? = 0,

    @SerialName("user")
    val user: String,

    @SerialName("qr")
    val qr: String,

    @SerialName("qr_id")
    val qrId: String,
)
