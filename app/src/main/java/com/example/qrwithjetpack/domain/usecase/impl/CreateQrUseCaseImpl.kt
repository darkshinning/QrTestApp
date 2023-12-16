package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.ProductRepository
import com.example.qrwithjetpack.domain.usecase.CreateQrUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateQrUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
) : CreateQrUseCase {
    override suspend fun execute(input: CreateQrUseCase.Input): CreateQrUseCase.Output {
        return try {
            withContext(Dispatchers.IO) {
                val result = productRepository.createProduct(product = input.product)
                if (result) {
                    CreateQrUseCase.Output.Success(result = result)
                } else {
                    CreateQrUseCase.Output.Failure()
                }
            }
        } catch (e: Exception) {
            return CreateQrUseCase.Output.Failure.Conflict

        }
    }
}