package com.example.qrwithjetpack.presentation.feature.photoFromQr

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrwithjetpack.domain.usecase.GetQrDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoFromQrViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductDetailsUseCase: GetQrDetailsUseCase
) : ViewModel()  {

    private val _qr = MutableStateFlow("")
    val qr: Flow<String> = _qr
    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: Flow<Boolean> = _isLoaded

    init {
        val qrId = savedStateHandle.get<String>("qr_id")
        qrId?.let {
            getQrFromDatabase(qrId = it)
        }
    }

    fun getQrFromDatabase(qrId: String) {
        viewModelScope.launch (Dispatchers.Main) {
            _isLoading.value = true
            val result = getProductDetailsUseCase.execute(
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
    }
}