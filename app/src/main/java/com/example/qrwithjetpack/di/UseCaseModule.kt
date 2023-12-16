package com.example.qrwithjetpack.di

import com.example.qrwithjetpack.domain.usecase.*
import com.example.qrwithjetpack.domain.usecase.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetProductsUseCase(impl: GetQrsUseCaseImpl): GetQrsUseCase

    @Binds
    abstract fun bindCreateProductUseCase(impl: CreateQrUseCaseImpl): CreateQrUseCase

    @Binds
    abstract fun bindGetProductDetailsUseCase(impl: GetQrDetailsUseCaseImpl): GetQrDetailsUseCase

    @Binds
    abstract fun bindDeleteProductUseCase(impl: DeleteQrUseCaseImpl): DeleteQrUseCase

    @Binds
    abstract fun bindCreateUserUseCase(impl: CreateUserUseCaseImpl): CreateUserUseCase

}