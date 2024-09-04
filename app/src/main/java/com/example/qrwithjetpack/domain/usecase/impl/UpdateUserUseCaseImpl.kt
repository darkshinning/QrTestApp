//package com.example.qrwithjetpack.domain.usecase.impl
//
//import com.example.qrwithjetpack.data.repository.UserRepository
//import com.example.qrwithjetpack.domain.usecase.CreateUserUseCase
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//
//class UpdateUserUseCaseImpl @Inject constructor(
//    private val userRepository: UserRepository,
//) : CreateUserUseCase {
//    override suspend fun execute(input: CreateUserUseCase.Input): CreateUserUseCase.Output {
//        return try {
//            withContext(Dispatchers.IO) {
//                val user = userRepository.getUser(input.user.login)
//                if (user?.login == null) {
//                    CreateUserUseCase.Output.Failure.Conflict
//                } else {
//                    userRepository.updateUser(
//                        login = input.user.login,
//                        faceModel = input.user.faceModel.toString()
//                    )
//                    CreateUserUseCase.Output.Success(result = true)
//                }
//            }
//        } catch (e: Exception) {
//            return CreateUserUseCase.Output.Failure()
//        }
//    }
//}