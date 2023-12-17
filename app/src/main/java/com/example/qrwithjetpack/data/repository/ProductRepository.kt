package com.example.qrwithjetpack.data.repository

import com.example.qrwithjetpack.data.network.dto.ProductDto
import com.example.qrwithjetpack.domain.model.Product

interface ProductRepository {
    suspend fun createProduct(product: Product): Boolean
    suspend fun getProducts(): List<ProductDto>?
    suspend fun getQrsByUser(user: String): List<ProductDto>?
    suspend fun getProduct(qrId: String): ProductDto
    suspend fun deleteProduct(qrId: String)
}