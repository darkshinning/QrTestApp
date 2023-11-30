package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.ProductRepository
import com.example.qrwithjetpack.domain.model.Product
import com.example.qrwithjetpack.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductsUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
) : GetProductsUseCase {
    override suspend fun execute(input: Unit): GetProductsUseCase.Output =
        withContext(Dispatchers.IO) {
            val result = productRepository.getProducts()
            return@withContext result?.let { it ->
                GetProductsUseCase.Output.Success(data = it.map {
                    Product(
//                        id = it.id ?: 0,
                        qr = it.qr,
                        user = it.user,
                        qrId = it.qrId
                    )
                })
            } ?: GetProductsUseCase.Output.Failure
        }
}