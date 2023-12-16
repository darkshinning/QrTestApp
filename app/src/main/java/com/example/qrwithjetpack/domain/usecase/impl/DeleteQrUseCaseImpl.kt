package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.ProductRepository
import com.example.qrwithjetpack.domain.usecase.DeleteQrUseCase
import javax.inject.Inject

class DeleteQrUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : DeleteQrUseCase {
    override suspend fun execute(input: DeleteQrUseCase.Input): DeleteQrUseCase.Output {
        productRepository.deleteProduct(input.productId)
        return DeleteQrUseCase.Output.Success
    }
}