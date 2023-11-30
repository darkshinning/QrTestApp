package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.ProductRepository
import com.example.qrwithjetpack.domain.usecase.DeleteProductUseCase
import javax.inject.Inject

class DeleteProductUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : DeleteProductUseCase {
    override suspend fun execute(input: DeleteProductUseCase.Input): DeleteProductUseCase.Output {
        productRepository.deleteProduct(input.productId)
        return DeleteProductUseCase.Output.Success
    }
}