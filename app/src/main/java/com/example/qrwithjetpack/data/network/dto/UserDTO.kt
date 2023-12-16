package com.example.qrwithjetpack.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(

    @SerialName("id")
    val id: Int? = 0,

    @SerialName("login")
    val login: String,

    @SerialName("password")
    val password: String,

    @SerialName("face_model")
    val faceModel: String? = "",

    @SerialName("firstname")
    val firstname: String? = "",

    @SerialName("lastname")
    val lastname: String? = "",

    @SerialName("description")
    val description: String? = "",
)
