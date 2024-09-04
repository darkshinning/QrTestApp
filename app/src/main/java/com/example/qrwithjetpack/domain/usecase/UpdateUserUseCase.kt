//package com.example.qrwithjetpack.domain.usecase
//
//import com.example.qrwithjetpack.domain.model.User
//
//interface UpdateUserUseCase : UseCase<CreateUserUseCase.Input, CreateUserUseCase.Output> {
//    class Input(val user: User)
//    sealed class Output {
//        class Success(val result: Boolean) : Output()
//        open class Failure : Output() {
//            object Conflict : Failure()
//        }
//    }
//}