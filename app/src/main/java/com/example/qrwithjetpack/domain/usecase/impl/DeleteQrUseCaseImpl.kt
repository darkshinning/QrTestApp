package com.example.qrwithjetpack.domain.usecase.impl

import com.example.qrwithjetpack.data.repository.QrRepository
import com.example.qrwithjetpack.domain.usecase.DeleteQrUseCase
import javax.inject.Inject

class DeleteQrUseCaseImpl @Inject constructor(
    private val qrRepository: QrRepository
) : DeleteQrUseCase {
    override suspend fun execute(input: DeleteQrUseCase.Input): DeleteQrUseCase.Output {
        qrRepository.deleteQr(input.qrId)
        return DeleteQrUseCase.Output.Success
    }
}