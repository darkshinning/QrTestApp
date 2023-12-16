package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.ProductRepository
import com.example.qrwithjetpack.domain.model.Product
import com.example.qrwithjetpack.domain.usecase.GetQrDetailsUseCase
import javax.inject.Inject

class GetQrDetailsUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
) : GetQrDetailsUseCase {
    override suspend fun execute(input: GetQrDetailsUseCase.Input): GetQrDetailsUseCase.Output {
        val result = productRepository.getProduct(input.id)

        return GetQrDetailsUseCase.Output.Success(
            data = Product(
                user = result.user,
                qr = result.qr,
                qrId = result.qrId
            )
        )
    }
}