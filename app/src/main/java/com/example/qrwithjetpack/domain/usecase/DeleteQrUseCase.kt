package com.example.qrwithjetpack.domain.usecase

interface DeleteQrUseCase: UseCase<DeleteQrUseCase.Input, DeleteQrUseCase.Output> {
    class Input(val productId: String)

    sealed class Output {
        object Success: Output()
    }
}