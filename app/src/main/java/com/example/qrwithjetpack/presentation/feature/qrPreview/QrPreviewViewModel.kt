package com.example.qrwithjetpack.presentation.feature.qrPreview

import android.R
import android.R.attr.height
import android.R.attr.width
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class QrPreviewViewModel @Inject constructor(

) : ViewModel()  {

    var qrCodeWriter = QRCodeWriter()
    lateinit var bitMatrix: BitMatrix
    lateinit var bitmap: Bitmap
    var width = 1024
    var height = 1024

    fun createQrfromId(qrId: String): Bitmap {
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
        return bitmap
    }

}