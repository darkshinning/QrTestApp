package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.UserRepository
import com.example.qrwithjetpack.domain.usecase.CreateUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : CreateUserUseCase {
    override suspend fun execute(input: CreateUserUseCase.Input): CreateUserUseCase.Output {
        return try {
            withContext(Dispatchers.IO) {
                val result = userRepository.createUser(user = input.user)
                if (result) {
                    CreateUserUseCase.Output.Success(result = result)
                } else {
                    CreateUserUseCase.Output.Failure()
                }
            }
        } catch (e: Exception) {
            return CreateUserUseCase.Output.Failure.Conflict

        }
    }
}