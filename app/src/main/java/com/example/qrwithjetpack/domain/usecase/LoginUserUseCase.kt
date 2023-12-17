package com.example.qrwithjetpack.domain.usecase

import com.example.qrwithjetpack.domain.model.User

interface LoginUserUseCase : UseCase<LoginUserUseCase.Input, LoginUserUseCase.Output> {
    class Input(val user: User)
    sealed class Output {
        class Success(val result: Boolean) : Output()
        open class Failure : Output() {
            object Conflict : Failure()
            object WrongPassword : Failure()
        }
    }
}