package com.example.qrwithjetpack.presentation.navigation

interface Destination {
    val route: String
    val title: String
}

object AddProductDestination : Destination {
    override val route = "add_product"
    override val title = "Add Product"
}
