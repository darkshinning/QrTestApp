package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.QrRepository
import com.example.qrwithjetpack.domain.model.Qr
import com.example.qrwithjetpack.domain.usecase.GetQrDetailsUseCase
import javax.inject.Inject

class GetQrDetailsUseCaseImpl @Inject constructor(
    private val qrRepository: QrRepository,
) : GetQrDetailsUseCase {
    override suspend fun execute(input: GetQrDetailsUseCase.Input): GetQrDetailsUseCase.Output {
        val result = qrRepository.getQr(input.id)

        return GetQrDetailsUseCase.Output.Success(
            data = Qr(
                user = result.user,
                qr = result.qr,
                qrId = result.qrId
            )
        )
    }
}