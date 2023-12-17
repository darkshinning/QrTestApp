package com.example.qrwithjetpack.di

import com.example.qrwithjetpack.data.repository.QrRepository
import com.example.qrwithjetpack.data.repository.UserRepository
import com.example.qrwithjetpack.data.repository.impl.QrRepositoryImpl
import com.example.qrwithjetpack.data.repository.impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindQrRepository(impl: QrRepositoryImpl): QrRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}