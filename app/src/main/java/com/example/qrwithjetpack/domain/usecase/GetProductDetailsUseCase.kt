package com.example.qrwithjetpack.domain.usecase

import com.example.qrwithjetpack.domain.model.Product

interface GetProductDetailsUseCase :
    UseCase<GetProductDetailsUseCase.Input, GetProductDetailsUseCase.Output> {
    class Input(val id: String)
    sealed class Output {
        class Success(val data: Product): Output()
        object Failure : Output()
    }
}