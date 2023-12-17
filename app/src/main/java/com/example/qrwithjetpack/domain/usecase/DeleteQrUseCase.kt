package com.example.qrwithjetpack.domain.usecase

interface DeleteQrUseCase: UseCase<DeleteQrUseCase.Input, DeleteQrUseCase.Output> {
    class Input(val qrId: String)

    sealed class Output {
        object Success: Output()
    }
}