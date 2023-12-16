package com.example.qrwithjetpack.domain.model

data class User(
    var login: String,
    val password: String,
    val faceModel: String? = "",
    val firstname: String? = "",
    val lastname: String? = "",
    val description: String? = ""
)
