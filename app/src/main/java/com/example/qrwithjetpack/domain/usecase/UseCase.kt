package com.example.qrwithjetpack.domain.usecase

interface UseCase<InputT, OutputT> {
    suspend fun execute(input: InputT): OutputT
}