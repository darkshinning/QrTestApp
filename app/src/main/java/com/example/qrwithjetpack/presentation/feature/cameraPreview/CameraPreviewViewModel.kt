package com.example.qrwithjetpack.presentation.feature.cameraPreview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.example.qrwithjetpack.domain.model.User
import com.example.qrwithjetpack.domain.usecase.CreateQrUseCase
import com.example.qrwithjetpack.domain.usecase.GetUserUseCase
import com.example.qrwithjetpack.faceRecognition.ImageAnalyzer
import com.example.qrwithjetpack.faceRecognition.model.FaceNetModel
import com.example.qrwithjetpack.faceRecognition.model.Models
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import kotlin.experimental.xor
import kotlin.math.roundToInt

@HiltViewModel
class CameraPreviewViewModel @Inject constructor(
    private val createQrUseCase: CreateQrUseCase,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private var registeredUser = User("", "") // чтобы получить данные юзера который зарегистрирован прямо сейчас

    private val _isPhotoTaken = MutableStateFlow(false)
    val isPhotoTaken: Flow<Boolean> = _isPhotoTaken

    private val _imagesWithNoFace = MutableStateFlow(false)
    val imagesWithNoFace: Flow<Boolean> = _imagesWithNoFace

    private var secretAESkey: Key? = null
    private var hashPassword: ByteArray? = null

    private val appname = "QrCodeAppDevelopedByDimashForMe!" // статический ключ для AES
    private val staticAESkey = appname.toByteArray()

    private val useGpu = true
    private val useXNNPack = true // переменные для faceNetModel

    private lateinit var faceNetModel : FaceNetModel
    lateinit var imageAnalyzer : ImageAnalyzer

    private lateinit var images: Pair<String, Bitmap>

    /**
     * Ниже переменные для тестирования скоростей процессов алгоритма
     */
    var timeToDetectFaceFeatures : Long = 0L
    private var timeToCreateASingleString : Long = 0L
    private var timeToEncodeSingleStringByAES : Long = 0L
    private var timeToEncodePasswordByAES : Long = 0L
    private var timeForPasswordToSha256 : Long = 0L



    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    fun capturePhoto(
        context: Context,
        cameraController: LifecycleCameraController,
        imageCapture: ImageCapture?,
    ) {
        val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

        viewModelScope.launch {
            val user =
                getUserUseCase.execute(GetUserUseCase.Input(RegisteredUser.registeredlogin.login))

            when (user) {
                is GetUserUseCase.Output.Success -> {
                    registeredUser = user.data
                }
                is GetUserUseCase.Output.Failure -> {
                }
            }
        }

        cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val imageCap = imageCapture ?: return

                var correctedBitmap = image.toBitmap()
                images = Pair(RegisteredUser.registeredlogin.login, correctedBitmap)

                timeToDetectFaceFeatures = System.currentTimeMillis()
                val faceFeatures = creatingFaceModel(context, images)

                val fos = FileOutputStream(createOutputFile(context))
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

    private fun createOutputFile(context: Context): File {
        val imageFileName = "temporary_photo"
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
        _imagesWithNoFace.value = false
        _isPhotoTaken.value = false
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun createQrFromPhoto(photo: ImageBitmap, faceFeatures: FloatArray) : String {

        val photoString = photo.asAndroidBitmap()

        val resized = Bitmap.createScaledBitmap(photoString, 300, 500, false)

        val stringsss = Base64.getEncoder()

        val stream = ByteArrayOutputStream()

        resized.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        val damsn = stringsss.encodeToString(stream.toByteArray())

        /**
         * Код ниже создает объединенную строку, чтобы зашифровать с помощью AES
         */

        timeToCreateASingleString = System.nanoTime()

        var biometryAndPersonalInfoString = ""

        for (n in 0..127) {
            val biometryData = (faceFeatures[n] * 1000.0).roundToInt() / 1000.0
            biometryAndPersonalInfoString += biometryData
            biometryAndPersonalInfoString += ";"
        }

        biometryAndPersonalInfoString += "+=+"
        biometryAndPersonalInfoString += registeredUser.firstname
        biometryAndPersonalInfoString += "+=+"
        biometryAndPersonalInfoString += registeredUser.lastname

        biometryAndPersonalInfoString = biometryAndPersonalInfoString.replace("/", "%2F")

        Log.i("Create A Single String",
            (System.nanoTime() - timeToCreateASingleString).toString() + " наносекунд"
        )

        timeToEncodeSingleStringByAES = System.nanoTime()
        var qrId = encryptStringWithAES(biometryAndPersonalInfoString, registeredUser.password)
        Log.i("Encode Single String By AES",
            (System.nanoTime() - timeToEncodeSingleStringByAES).toString() + " наносекунд"
        )

        timeToEncodePasswordByAES = System.nanoTime()
        val encryptedPublicKey = encryptKeyWithAES(registeredUser.password)
        Log.i("Encode Password By AES",
            (System.nanoTime() - timeToEncodePasswordByAES).toString() + " наносекунд"
        )

        qrId += "."
        qrId += encryptedPublicKey

        qrId = qrId.replace("/", "%2F")

        Log.e("THE SIZE", qrId.length.toString())


        viewModelScope.launch {
            val qr = Qr(
                qr = damsn,
                user = registeredUser.login,
                qrId = qrId
            )
            createQrUseCase.execute(input = CreateQrUseCase.Input(qr))
        }

        _isPhotoTaken.value = false

        return qrId
    }

    private fun passwordToSha256(password: String): ByteArray {
        timeForPasswordToSha256 = System.nanoTime()

        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(password.toByteArray())

            Log.i("Password To SHA256",
                (System.nanoTime()- timeForPasswordToSha256).toString() + " наносекунд"
            )

            messageDigest
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    @SuppressLint("GetInstance")
    fun encryptStringWithAES(strToEncrypt: String, password: String): String {
        try {
            hashPassword = passwordToSha256(password)

            val xor = ByteArray(32)
            for (n in 0..31) {
                val byte = hashPassword!![n] xor staticAESkey[n]
                xor[n] = byte
            }

            secretAESkey = SecretKeySpec(xor, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretAESkey)
            return Base64
                .getEncoder()
                .encodeToString(
                    cipher.doFinal(
                        strToEncrypt
                            .toByteArray(
                                charset("UTF-8")
                            )
                    )
                )
        } catch (e: Exception) {
            println("Кодтау кезіндегі пайда болған қате: $e")
        }
        return ""
    }

    @SuppressLint("GetInstance")
    fun encryptKeyWithAES(password: String): String {
        try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(appname.toByteArray(), "AES"))
            val result = Base64
                .getEncoder()
                .encodeToString(
                    cipher.doFinal(
                        password
                            .toByteArray(charset("UTF-8")
                            )
                        )
                    )
            Log.e("DECODED STRING", result)
            return result
        } catch (e: Exception) {

            println("Error while encrypting: $e")
        }
        return "null"
    }


    fun creatingFaceModel(context: Context, images: Pair<String, Bitmap>): FloatArray {
        faceNetModel = FaceNetModel(context, Models.FACENET, useGpu, useXNNPack)
        RegisteredUser.initializedFacenet = false
        imageAnalyzer = ImageAnalyzer(faceNetModel)
        return imageAnalyzer.run(images)
    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}