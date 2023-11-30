package com.example.qrwithjetpack.presentation.feature.qrCameraPreview

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class QrCameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel()  {

//    lateinit var qrString: String
    private val _qr = MutableStateFlow("")
    val qr: Flow<String> = _qr

    init {
        val qrId = savedStateHandle.get<String>("qr_id")
        qrId?.let {
            getQrFromDatabase(qrId = it)
        }
    }

    private fun getQrFromDatabase(qrId: String) {
        viewModelScope.launch {
            val result = getProductDetailsUseCase.execute(
                GetProductDetailsUseCase.Input(
                    id = qrId
                )
            )
            when (result) {
                is GetProductDetailsUseCase.Output.Success -> {
                    _qr.emit(result.data.qr)
//                    Log.e("ERROR", _qr.toString())
//                    qrString = result.data.qr
                }
                is GetProductDetailsUseCase.Output.Failure -> {

                }
            }
        }
//        Log.e("ERROR", "Check the qr: " + qrString[0].toString())
//        return qrString
    }

//
//    fun openTheQr(qrId: String): ImageBitmap {
//        Log.e("ERROR", "Before var")
//        val qrString = getQrFromDatabase(qrId)
//        Log.e("ERROR", "After var")
//
//        val decoder = Base64.getDecoder()
//        val bt = decoder.decode(qrString)
//        val bm = BitmapFactory.decodeByteArray(bt, 0, bt.size)
//        return bm.asImageBitmap()
//    }

}