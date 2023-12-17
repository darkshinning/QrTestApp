package com.example.qrwithjetpack.domain.usecase

import com.example.qrwithjetpack.domain.model.Qr

interface CreateQrUseCase : UseCase<CreateQrUseCase.Input, CreateQrUseCase.Output> {
    class Input(val qr: Qr)
    sealed class Output {
        class Success(val result: Boolean) : Output()
        open class Failure : Output() {
            object Conflict : Failure()
            object Unauthorized : Failure()
            object BadRequest : Failure()
            object InternalError : Failure()
        }
    }
}