package com.example.qrwithjetpack.domain.usecase

import com.example.qrwithjetpack.domain.model.Product

interface GetQrsUseCase : UseCase<Unit, GetQrsUseCase.Output> {
    sealed class Output {
        class Success(val data: List<Product>): Output()
        object Failure : Output()
    }

}