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
    abstract fun bindGetQrsUseCase(impl: GetQrsUseCaseImpl): GetQrsUseCase

    @Binds
    abstract fun bindCreateQrUseCase(impl: CreateQrUseCaseImpl): CreateQrUseCase

    @Binds
    abstract fun bindGetQrDetailsUseCase(impl: GetQrDetailsUseCaseImpl): GetQrDetailsUseCase

    @Binds
    abstract fun bindDeleteQrUseCase(impl: DeleteQrUseCaseImpl): DeleteQrUseCase

    @Binds
    abstract fun bindCreateUserUseCase(impl: CreateUserUseCaseImpl): CreateUserUseCase

    @Binds
    abstract fun bindGetUserUseCase(impl: GetUserUseCaseImpl): GetUserUseCase

    @Binds
    abstract fun bindLoginUserUseCase(impl: LoginUserUseCaseImpl): LoginUserUseCase

//    @Binds
//    abstract fun bindUpdateUserUseCase(impl: UpdateUserUseCaseImpl): UpdateUserUseCase

}