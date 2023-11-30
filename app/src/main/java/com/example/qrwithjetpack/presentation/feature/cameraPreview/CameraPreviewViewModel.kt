package com.example.qrwithjetpack.presentation.feature.cameraPreview

import androidx.lifecycle.ViewModel
import com.example.qrwithjetpack.domain.usecase.impl.SavePhotoToGallery
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope

@HiltViewModel
class CameraPreviewViewModel @Inject constructor(
//    private val navController: NavController
    private val savePhotoToGalleryUseCase: SavePhotoToGallery
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun storePhotoInGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            savePhotoToGalleryUseCase.call(bitmap)
            updateCapturedPhotoState(bitmap)
        }
    }

    private fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }
}