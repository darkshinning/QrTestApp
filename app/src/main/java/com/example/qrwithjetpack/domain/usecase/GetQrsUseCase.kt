package com.example.qrwithjetpack.domain.usecase

import com.example.qrwithjetpack.domain.model.Qr

interface GetQrsUseCase : UseCase<Unit, GetQrsUseCase.Output> {
    sealed class Output {
        class Success(val data: List<Qr>): Output()
        object Failure : Output()
    }

}