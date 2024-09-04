package com.example.qrwithjetpack.domain.usecase

import com.example.qrwithjetpack.domain.model.User

interface GetUserUseCase :
    UseCase<GetUserUseCase.Input, GetUserUseCase.Output> {
    class Input(val login: String)
    sealed class Output {
        class Success(val data: User): Output()
        object Failure : Output()
    }
}