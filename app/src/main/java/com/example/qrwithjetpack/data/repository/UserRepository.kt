package com.example.qrwithjetpack.data.repository

import com.example.qrwithjetpack.domain.model.User

interface UserRepository {
    suspend fun createUser(user: User) : Boolean
    suspend fun updateUser(user: User) : Boolean
}