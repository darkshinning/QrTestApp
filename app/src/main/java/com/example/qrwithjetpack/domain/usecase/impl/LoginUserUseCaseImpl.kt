package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.UserRepository
import com.example.qrwithjetpack.domain.usecase.CreateUserUseCase
import com.example.qrwithjetpack.domain.usecase.LoginUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : LoginUserUseCase {
    override suspend fun execute(input: LoginUserUseCase.Input): LoginUserUseCase.Output {
        return try {
            withContext(Dispatchers.IO) {
                val user = userRepository.getUser(input.user.login)
                if (user == null) {
                    LoginUserUseCase.Output.Failure()
                } else if (user.password != input.user.password) {
                    LoginUserUseCase.Output.Failure.WrongPassword
                } else {
                    LoginUserUseCase.Output.Success(result = true)
                }
            }
        } catch (e: Exception) {
            return LoginUserUseCase.Output.Failure.Conflict
        }
    }
}