package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.QrRepository
import com.example.qrwithjetpack.data.repository.UserRepository
import com.example.qrwithjetpack.domain.model.Qr
import com.example.qrwithjetpack.domain.model.User
import com.example.qrwithjetpack.domain.usecase.GetQrDetailsUseCase
import com.example.qrwithjetpack.domain.usecase.GetUserUseCase
import javax.inject.Inject

class GetUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : GetUserUseCase {
    override suspend fun execute(input: GetUserUseCase.Input): GetUserUseCase.Output {
        val result = userRepository.getUser(input.login)

        return GetUserUseCase.Output.Success(
            data = User(
                firstname = result!!.firstname,
                lastname = result.lastname,
                password = result.password,
                login = result.login
            )
        )
    }
}