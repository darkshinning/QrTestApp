package com.example.qrwithjetpack.data.repository

import com.example.qrwithjetpack.data.network.dto.UserDTO
import com.example.qrwithjetpack.domain.model.User

interface UserRepository {
    suspend fun createUser(user: User) : String

    suspend fun getUser(login: String) : UserDTO?

    suspend fun getUsers() : List<UserDTO>?

    suspend fun updateUser(login: String) : Boolean
}