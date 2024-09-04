package com.example.qrwithjetpack.presentation.feature.photoFromQr

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrwithjetpack.domain.usecase.GetQrDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import kotlin.experimental.xor

@HiltViewModel
class PhotoFromQrViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQrDetailsUseCase: GetQrDetailsUseCase
) : ViewModel()  {

    private var secretAESkey: Key? = null

    private val appname = "QrCodeAppDevelopedByDimashForMe!"

    private val staticAESkey = SecretKeySpec(appname.toByteArray(), "AES")

    private val _qr = MutableStateFlow("")
    val qr: Flow<String> = _qr
    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: Flow<Boolean> = _isLoaded

    private val _firstname = MutableStateFlow("Өңделуде...")
    val firstname: Flow<String> = _firstname
    private val _lastname = MutableStateFlow("Өңделуде...")
    val lastname: Flow<String> = _lastname
    private val _biometry1 = MutableStateFlow("Өңделуде...")
    val biometry1: Flow<String> = _biometry1
    private val _biometry2 = MutableStateFlow("Өңделуде...")
    val biometry2: Flow<String> = _biometry2
    private val _biometry3 = MutableStateFlow("Өңделуде...")
    val biometry3: Flow<String> = _biometry3

    /**
     * Ниже переменные для тестирования скоростей процессов алгоритма
     */
    private var timeToCreateFaceFeaturesArray : Long = 0L
    private var timeToDecodeSingleStringByAES : Long = 0L
    private var timeToDecodePasswordByAES : Long = 0L

    init {
        val qrId = savedStateHandle.get<String>("qr_id")
        qrId?.let {
            getQrFromDatabase(qrId = it)
        }
    }

    fun getQrFromDatabase(qrId: String) {
        viewModelScope.launch (Dispatchers.Main) {
            _isLoading.value = true
            val result = getQrDetailsUseCase.execute(
                GetQrDetailsUseCase.Input(
                    id = qrId
                )
            )
            when (result) {
                is GetQrDetailsUseCase.Output.Success -> {

                    _qr.emit(result.data.qr)
                    _isLoading.value = false
                    _isLoaded.value = true
                }
                is GetQrDetailsUseCase.Output.Failure -> {
                    _isLoading.value = false
                }
            }
        }

        val listOfCiphers = splitEncodedStrings(qrId)

        val encodedInformation = listOfCiphers[0]
        val encodedPassword = listOfCiphers[1]

        timeToDecodePasswordByAES = System.nanoTime()
        val password = decryptKey(encodedPassword)
        Log.i("Decode Password By AES",
            (System.nanoTime()- timeToDecodePasswordByAES).toString() + " наносекунд"
        )

        timeToDecodeSingleStringByAES = System.nanoTime()
        val information = decryptString(encodedInformation, password)
        Log.i("Decode Single String By AES",
            (System.nanoTime() - timeToDecodeSingleStringByAES).toString() + " наносекунд"
        )

        timeToCreateFaceFeaturesArray = System.nanoTime()
        val informationList = splitInformation(information!!)

        val biometryInformation = informationList[0]
        val firstnameOfUser = informationList[1]
        val lastnameOfUser = informationList[2]

        val faceFeatures = extractFaceFeatures(biometryInformation)
        Log.i("Create Face Features Array",
            (System.nanoTime() - timeToCreateFaceFeaturesArray).toString() + " наносекунд"
        )
        Log.i("Create Face Features Array",
            faceFeatures[0].toString()
        )
        _firstname.value = firstnameOfUser
        _lastname.value = lastnameOfUser
        _biometry1.value = faceFeatures[0].toString()
        _biometry2.value = faceFeatures[1].toString()
        _biometry3.value = faceFeatures[2].toString()
    }

    private fun splitEncodedStrings(qrString: String): List<String> {
        return qrString.split(".")
    }

    private fun splitInformation(information: String): List<String> {
        return information.split("+=+")
    }

    private fun extractFaceFeatures(biometryInformation: String): FloatArray {
        val arrayOfFeaturesAsString = biometryInformation.split(";")
        val arrayOfFeatures = floatArrayOf().toMutableList()
        for (n in 0..127) {
            arrayOfFeatures.add(arrayOfFeaturesAsString[n].toFloat())
        }
        return arrayOfFeatures.toFloatArray()
    }


    @SuppressLint("GetInstance")
    fun decryptString(strToDecrypt: String?, password: String): String? {
        try {

            val hashPassword = passwordToSha256(password)

            val xor = ByteArray(32)
            for (n in 0..31) {
                val flex = hashPassword[n] xor appname.toByteArray()[n]
                xor[n] = flex
            }

            secretAESkey = SecretKeySpec(xor, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretAESkey)
            return String(
                cipher.doFinal(
                    Base64.getDecoder().decode(strToDecrypt)
                )
            )
        } catch (e: Exception) {
            println("Error while decrypting: $e")
        }
        return null
    }

    @SuppressLint("GetInstance")
    private fun decryptKey(encryptedPublicKey: String): String {
        try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(appname.toByteArray(), "AES"))
            return String(
                cipher.doFinal(
                    Base64.getDecoder().decode(encryptedPublicKey)
                )
            )
        } catch (e: Exception) {
            println("Error while decrypting: $e")
        }
        return "null"
    }

    private fun passwordToSha256(password: String): ByteArray {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(password.toByteArray())
            messageDigest
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

}