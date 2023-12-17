package com.example.qrwithjetpack.data.repository

import com.example.qrwithjetpack.data.network.dto.QRDto
import com.example.qrwithjetpack.domain.model.Qr

interface QrRepository {
    suspend fun createQr(qr: Qr): Boolean
    suspend fun getQrs(): List<QRDto>?
    suspend fun getQrsByUser(user: String): List<QRDto>?
    suspend fun getQr(qrId: String): QRDto
    suspend fun deleteQr(qrId: String)
}