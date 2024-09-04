package com.example.qrwithjetpack.presentation.feature.registration

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrwithjetpack.RegisteredUser
import com.example.qrwithjetpack.domain.model.User
import com.example.qrwithjetpack.domain.usecase.CreateUserUseCase
import com.example.qrwithjetpack.domain.usecase.LoginUserUseCase
import com.example.qrwithjetpack.faceRecognition.ImageAnalyzer
import com.example.qrwithjetpack.faceRecognition.model.FaceNetModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
open class RegistrationViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {
    private val _registerSuccess = MutableStateFlow<LoginUserUseCase.Output?>(null)
    val registerSuccess: Flow<LoginUserUseCase.Output?> =
        _registerSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading

    private val _showSuccessMessage = MutableStateFlow(false)
    val showSuccessMessage: Flow<Boolean> = _showSuccessMessage

    fun registerUser(login: String, password: String) {
        if (login.isEmpty() || password.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true

            val user = User(
                login = login,
                password = password,
            )
            when (val result =
                loginUserUseCase.execute(LoginUserUseCase.Input(user = user))) {

                is LoginUserUseCase.Output.Success -> {
                    _isLoading.value = false
                    _showSuccessMessage.emit(true)
                    _registerSuccess.value = result
                    RegisteredUser.registeredlogin.login = login
                }

                is LoginUserUseCase.Output.Failure.WrongPassword -> {
                    _isLoading.value = false
                    _registerSuccess.value = result
                }

                is LoginUserUseCase.Output.Failure.Conflict -> {
                    _isLoading.value = false
                    _registerSuccess.value = result
                }

                is LoginUserUseCase.Output.Failure -> {
                    _isLoading.value = false
                    _registerSuccess.value = result
                }

                else -> {}
            }
        }
    }

    open fun getPhotoFile(context: Context): File {
        val storageDirectory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDirectory, "temporary_photo.jpg")
    }

    open fun deleteFile(context: Context) {
        val photo = getPhotoFile(context)
        photo.delete()
    }
}