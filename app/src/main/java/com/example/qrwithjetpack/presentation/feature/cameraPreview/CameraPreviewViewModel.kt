package com.example.qrwithjetpack.presentation.feature.cameraPreview

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrwithjetpack.RegisteredUser
import com.example.qrwithjetpack.domain.model.Qr
import com.example.qrwithjetpack.domain.usecase.CreateQrUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.Base64
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CameraPreviewViewModel @Inject constructor(
    private val createQrUseCase: CreateQrUseCase
) : ViewModel() {
    private val _isPhotoTaken = MutableStateFlow(false)
    val isPhotoTaken: Flow<Boolean> = _isPhotoTaken

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    fun capturePhoto(
        context: Context,
        cameraController: LifecycleCameraController,
        imageCapture: ImageCapture?,
    ) {
        val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

        cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val imageCap = imageCapture ?: return

                val correctedBitmap = image.toBitmap()
                val imageFileName = "temporary_photo"

                val fos = FileOutputStream(createOutputFile(context, imageFileName))
                correctedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos)
                fos.flush()
                fos.close()

                image.close()

                _isPhotoTaken.value = true
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraContent", "Error capturing image", exception)
            }
        })
    }

    private fun createOutputFile(context: Context, imageFileName: String): File {

        val storageDirectory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File(storageDirectory, "$imageFileName.jpg")
    }

    fun getPhotoFile(context: Context): File {
        val storageDirectory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDirectory, "temporary_photo.jpg")
    }

    fun deleteFile(context: Context) {
        val photo = getPhotoFile(context)
        photo.delete()

        _isPhotoTaken.value = false
    }

    fun createQrFromPhoto(photo: ImageBitmap) : String {
//        if (user <= 0) return ""

        val user = RegisteredUser.registeredlogin

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

        val qrId = user.login + " at " + LocalDate.now() + (1..145500).random().toString()

        /**
         * qrId must contain personal information of user sharing a photo and IF it has a human face
         * then also a biometric information extracted by MLKit.
         * TODO("after finishing face detection and user registration")
         */

        viewModelScope.launch {
            val qr = Qr(
                qr = damsn,
                user = user.login,
                qrId = qrId
            )

            createQrUseCase.execute(input = CreateQrUseCase.Input(qr))
        }

        _isPhotoTaken.value = false

        return qrId
    }
}