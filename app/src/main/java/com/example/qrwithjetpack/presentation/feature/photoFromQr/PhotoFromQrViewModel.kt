package com.example.qrwithjetpack.presentation.feature.photoFromQr

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrwithjetpack.domain.usecase.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotoFromQrViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel()  {

    //    lateinit var qrString: String
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
            Log.e("ERROR", "DOSHLO DO SUDA???")
            val result = getProductDetailsUseCase.execute(
                GetProductDetailsUseCase.Input(
                    id = qrId
                )
            )
            Log.e("ERROR", "A DO SUDA???")
            when (result) {
                is GetProductDetailsUseCase.Output.Success -> {

                    Log.e("ERROR", "FLEX")
                    _qr.emit(result.data.qr)
                    Log.e("ERROR", _qr.value)
                    _isLoading.value = false
                    _isLoaded.value = true
                }
                is GetProductDetailsUseCase.Output.Failure -> {
                    _isLoading.value = false
                    Log.e("ERROR", "NO FLEX :((((")
                }
            }
        }
//        Log.e("ERROR", "Check the qr: " + qrString[0].toString())
//        return qrString
    }
}