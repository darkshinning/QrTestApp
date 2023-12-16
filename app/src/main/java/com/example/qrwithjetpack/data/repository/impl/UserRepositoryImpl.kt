package com.example.qrwithjetpack.data.repository.impl

import com.example.qrwithjetpack.data.network.dto.UserDTO
import com.example.qrwithjetpack.data.repository.UserRepository
import com.example.qrwithjetpack.domain.model.User
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : UserRepository {
    override suspend fun createUser(user: User): Boolean {
        return try {
            val userDto = UserDTO(
                login = user.login,
                password = user.password
            )
            postgrest["qrcodeUsers"].insert(userDto)
            true
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun updateUser(user: User): Boolean {
        return true
    }
}