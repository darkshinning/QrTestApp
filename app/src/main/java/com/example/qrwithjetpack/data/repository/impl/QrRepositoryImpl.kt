package com.example.qrwithjetpack.data.repository.impl

import com.example.qrwithjetpack.data.network.dto.QRDto
import com.example.qrwithjetpack.data.repository.QrRepository
import com.example.qrwithjetpack.domain.model.Qr
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class QrRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : QrRepository {
    override suspend fun createQr(qr: Qr): Boolean {
        return try {
            val qrDto = QRDto(
                user = qr.user,
                qr = qr.qr,
                qrId = qr.qrId
            )
            postgrest["qrcodeBase"].insert(qrDto)
            true
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getQrs(): List<QRDto>? {
        return postgrest["qrcodeBase"]
            .select().decodeList()
    }

    override suspend fun getQrsByUser(user: String): List<QRDto>? {
        return postgrest["qrcodeBase"]
            .select {
                eq("user", user)
            }.decodeList()
    }


    override suspend fun getQr(qrId: String): QRDto {
        return postgrest["qrcodeBase"].select {
            eq("qr_id", qrId)
        }.decodeSingle()
    }

    override suspend fun deleteQr(qrId: String) {
        postgrest["qrcodeBase"].delete {
            eq("qr_id", qrId)
        }
    }
}