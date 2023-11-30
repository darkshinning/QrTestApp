package com.example.qrwithjetpack.di

import com.example.qrwithjetpack.data.repository.ProductRepository
//import com.example.manageproducts.data.repository.impl.AuthenticationRepositoryImpl
import com.example.qrwithjetpack.data.repository.impl.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

}