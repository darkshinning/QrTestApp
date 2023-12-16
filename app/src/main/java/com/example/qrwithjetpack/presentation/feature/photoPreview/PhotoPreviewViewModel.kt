package com.example.qrwithjetpack.presentation.feature.photoPreview

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrwithjetpack.domain.model.Product
import com.example.qrwithjetpack.domain.usecase.CreateQrUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.asJavaRandom

@HiltViewModel
class PhotoPreviewViewModel @Inject constructor(
    private val createProductUseCase: CreateQrUseCase
) : ViewModel()  {
    fun deleteFile(context: Context) {
        val photo = getPhotoFile(context)
        photo.delete()
    }

    private fun getPhotoFile(context: Context): File {
        val storageDirectory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDirectory, "temporary_photo.jpg")
    }

    fun createQrFromPhoto(photo: ImageBitmap, user: Int) : String {
        if (user <= 0) return ""

        val photoString = photo.asAndroidBitmap()
        val resized = Bitmap.createScaledBitmap(photoString, 300, 500, false)
        val stringsss = Base64.getEncoder()
        val stream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val damsn = stringsss.encodeToString(stream.toByteArray())

        /**
         * That crap above me must be changed to a string or some type of shit. Cause yo what the fuck is that?
         * Bitmap and shit... i don't understand it, my g.
         */

        val qrId = Random.asJavaRandom().toString()

        /**
         * qrId must contain personal information of user sharing a photo and IF it has a human face
         * then also a biometric information extracted by MLKit.
         * TODO("after finishing face detection and user registration")
         */

        viewModelScope.launch {
            val qr = Product(
                qr = damsn,
                user = user,
                qrId = qrId
            )

            createProductUseCase.execute(input = CreateQrUseCase.Input(qr))
        }
        return qrId
    }
}