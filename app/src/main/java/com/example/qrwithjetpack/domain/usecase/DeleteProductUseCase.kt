package com.example.qrwithjetpack.domain.usecase

interface DeleteProductUseCase: UseCase<DeleteProductUseCase.Input, DeleteProductUseCase.Output> {
    class Input(val productId: String)

    sealed class Output {
        object Success: Output()
    }
}