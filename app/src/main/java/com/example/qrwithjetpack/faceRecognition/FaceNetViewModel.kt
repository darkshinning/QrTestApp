package com.example.qrwithjetpack.faceRecognition

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
//import com.example.qrwithjetpack.domain.usecase.UpdateUserUseCase
import com.example.qrwithjetpack.faceRecognition.model.FaceNetModel
import com.example.qrwithjetpack.faceRecognition.model.Models
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.*
import javax.inject.Inject

@HiltViewModel
class FaceNetViewModel @Inject constructor(
//    updateUserUseCase: UpdateUserUseCase
) : ViewModel()  {

    private var isSerializedDataStored = false

    // Serialized data will be stored ( in app's private storage ) with this filename.
    private val SERIALIZED_DATA_FILENAME = "image_data"

    // Shared Pref key to check if the data was stored.
    private val SHARED_PREF_IS_DATA_STORED_KEY = "is_data_stored"

    private lateinit var frameAnalyser  : FrameAnalyser
    private lateinit var faceNetModel : FaceNetModel
    private lateinit var sharedPreferences: SharedPreferences

    // <----------------------- User controls --------------------------->

    // Use the device's GPU to perform faster computations.
    // Refer https://www.tensorflow.org/lite/performance/gpu
    private val useGpu = true

    // Use XNNPack to accelerate inference.
    // Refer https://blog.tensorflow.org/2020/07/accelerating-tensorflow-lite-xnnpack-integration.html
    private val useXNNPack = true

    // You may the change the models here.
    // Use the model configs in Models.kt
    // Default is Models.FACENET ; Quantized models are faster
    private val modelInfo = Models.FACENET

    // Camera Facing
    private val cameraFacing = CameraSelector.LENS_FACING_BACK

    // <---------------------------------------------------------------->


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var logTextView : TextView

        fun setMessage( message : String ) {
            logTextView.text = message
        }
    }

    fun init(context: Context) {
        faceNetModel = FaceNetModel( context, modelInfo , useGpu , useXNNPack )
        frameAnalyser = FrameAnalyser( context, faceNetModel )
    }

    /**
     * There should be a function that must process input video to analyze and create face embeddings.
     * For so function hierarchy will look like that:
     * scanVideo(imageArray[]) -> scanImage(imageBitmap)
     */

    fun creatingFaceModel() {

    }

}