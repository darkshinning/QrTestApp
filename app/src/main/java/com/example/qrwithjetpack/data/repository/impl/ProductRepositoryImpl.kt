package com.example.qrwithjetpack.data.repository.impl

import com.example.qrwithjetpack.data.network.dto.ProductDto
import com.example.qrwithjetpack.data.repository.ProductRepository
import com.example.qrwithjetpack.domain.model.Product
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : ProductRepository {
    override suspend fun createProduct(product: Product): Boolean {
        return try {
            val productDto = ProductDto(
//                id = product.id,
                user = product.user,
                qr = product.qr,
                qrId = product.qrId
            )
            postgrest["qrcodeBase"].insert(productDto)
            true
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getProducts(): List<ProductDto>? {
        return postgrest["qrcodeBase"]
            .select().decodeList()
    }

    override suspend fun getQrsByUser(user: String): List<ProductDto>? {
        return postgrest["qrcodeBase"]
            .select {
                eq("user", user)
            }.decodeList()
    }


    override suspend fun getProduct(qrId: String): ProductDto {
        return postgrest["qrcodeBase"].select {
            eq("qr_id", qrId)
        }.decodeSingle()
    }

    override suspend fun deleteProduct(qrId: String) {
        postgrest["qrcodeBase"].delete {
            eq("qr_id", qrId)
        }
    }
}