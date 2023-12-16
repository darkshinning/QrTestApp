package com.example.qrwithjetpack.presentation.feature.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrwithjetpack.RegisteredUser
import com.example.qrwithjetpack.domain.model.User
import com.example.qrwithjetpack.domain.usecase.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {
    private val _registerSuccess = MutableStateFlow<CreateUserUseCase.Output?>(null)
    val registerSuccess: Flow<CreateUserUseCase.Output?> =
        _registerSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading

    private val _showSuccessMessage = MutableStateFlow(false)
    val showSuccessMessage: Flow<Boolean> = _showSuccessMessage

    fun registerUser(login: String, password: String) {
        if (login.isEmpty() || password.isEmpty()) return
        RegisteredUser.registeredlogin.login = login
        viewModelScope.launch {
            _isLoading.value = true

            val user = User(
                login = login,
                password = password,
            )
            when (val result =
                createUserUseCase.execute(CreateUserUseCase.Input(user = user))) {
                is CreateUserUseCase.Output.Success -> {
                    _isLoading.value = false
                    _showSuccessMessage.emit(true)
                    _registerSuccess.value = result
                }
                is CreateUserUseCase.Output.Failure -> {
                    _isLoading.value = false
                    _registerSuccess.value = result
                }
            }

        }
    }
}