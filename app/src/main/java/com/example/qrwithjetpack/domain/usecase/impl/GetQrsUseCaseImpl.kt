package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.ProductRepository
import com.example.qrwithjetpack.domain.model.Product
import com.example.qrwithjetpack.domain.usecase.GetQrsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetQrsUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
) : GetQrsUseCase {
    override suspend fun execute(input: Unit): GetQrsUseCase.Output =
        withContext(Dispatchers.IO) {
            val result = productRepository.getProducts()
            return@withContext result?.let { it ->
                GetQrsUseCase.Output.Success(data = it.map {
                    Product(
//                        id = it.id ?: 0,
                        qr = it.qr,
                        user = it.user,
                        qrId = it.qrId
                    )
                })
            } ?: GetQrsUseCase.Output.Failure
        }
}