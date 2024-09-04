package com.example.qrwithjetpack.presentation.feature.qrPreview

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class QrPreviewViewModel @Inject constructor(

) : ViewModel()  {

    private var qrCodeWriter = QRCodeWriter()
    private lateinit var bitMatrix: BitMatrix
    private lateinit var bitmap: Bitmap
    private var width = 1024
    var height = 1024
    private var timeForQRgeneration : Long = 0L
    fun createQrfromId(qrId: String): Bitmap {
        timeForQRgeneration = System.nanoTime()
        bitMatrix = qrCodeWriter.encode(qrId, BarcodeFormat.QR_CODE, width, height)
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x, y,
                    if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                )
            }
        }
        Log.i("Qr Generation Time",
            (System.nanoTime() - timeForQRgeneration).toString() + " наносекунд"
        )
        return bitmap
    }

}