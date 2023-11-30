package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.network.dto.ProductDto
import com.example.qrwithjetpack.data.repository.ProductRepository
import com.example.qrwithjetpack.domain.model.Product
import com.example.qrwithjetpack.domain.usecase.GetProductDetailsUseCase
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import javax.inject.Inject

class GetProductDetailsUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
) : GetProductDetailsUseCase {
    override suspend fun execute(input: GetProductDetailsUseCase.Input): GetProductDetailsUseCase.Output {
        val result = productRepository.getProduct(input.id)

        return GetProductDetailsUseCase.Output.Success(
            data = Product(
                user = result.user,
                qr = result.qr,
                qrId = result.qrId
            )
        )
    }
}