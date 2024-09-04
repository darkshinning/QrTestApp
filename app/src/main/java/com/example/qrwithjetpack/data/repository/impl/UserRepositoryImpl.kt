package com.example.qrwithjetpack.data.repository.impl

import android.util.Log
import com.example.qrwithjetpack.data.network.dto.UserDTO
import com.example.qrwithjetpack.data.repository.UserRepository
import com.example.qrwithjetpack.domain.model.User
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.PostgrestResult
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : UserRepository {
    override suspend fun createUser(user: User): String {
        return try {
            val result = getUser(user.login)
            if (result == null) {
                val userDto = UserDTO(
                    firstname = user.firstname,
                    lastname = user.lastname,
                    login = user.login,
                    password = user.password
                )
                postgrest["qrcodeUsers"].insert(userDto)
                "Success"
            } else {
                "Conflict"
            }
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getUser(login: String): UserDTO? {
        return postgrest["qrcodeUsers"]
            .select{
                eq("login", login)
            }.decodeSingleOrNull()
    }

    override suspend fun getUsers(): List<UserDTO>? {
        return postgrest["qrcodeUsers"]
            .select().decodeList()
    }

    override suspend fun updateUser(login: String, faceModel: String) {
        postgrest["qrcodeUsers"].update({
            set("face_model", faceModel.toString())
        }) {
            eq("login", login)
        }
    }
}