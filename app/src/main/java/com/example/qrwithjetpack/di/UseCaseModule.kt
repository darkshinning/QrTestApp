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
    abstract fun bindGetProductsUseCase(impl: GetProductsUseCaseImpl): GetProductsUseCase

    @Binds
    abstract fun bindCreateProductUseCase(impl: CreateProductUseCaseImpl): CreateProductUseCase

    @Binds
    abstract fun bindGetProductDetailsUseCase(impl: GetProductDetailsUseCaseImpl): GetProductDetailsUseCase

    @Binds
    abstract fun bindDeleteProductUseCase(impl: DeleteProductUseCaseImpl): DeleteProductUseCase

//    @Binds
//    abstract fun bindAuthenticateUseCase(impl: SignInUseCaseImpl): SignInUseCase
//
//    @Binds
//    abstract fun bindSignUpUseCase(impl: SignUpUseCaseImpl): SignUpUseCase
//
//    @Binds
//    abstract fun bindSignInWithGoogleUseCase(impl: SignInWithGoogleUseCaseImpl): SignInWithGoogleUseCase
}