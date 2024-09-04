package com.example.qrwithjetpack.faceRecognition

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.example.qrwithjetpack.faceRecognition.model.FaceNetModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageAnalyzer(private var faceNetModel: FaceNetModel) {

    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .build()
    private val detector = FaceDetection.getClient(realTimeOpts)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    var imagesWithNoFace = false
    private var imageCounter = 0
    private var numImages = 0
    private var data = ArrayList<Pair<String,Bitmap>>()
    private var zero: ArrayList<Pair<String, FloatArray>> = ArrayList()

    private val _imageHasFace = MutableStateFlow(false)

    val imageHasFace: Flow<Boolean> = _imageHasFace

    private val _imageProcessionEnded = MutableStateFlow(false)

    val imageProcessionEnded: Flow<Boolean> = _imageProcessionEnded

    private val _faceFeatures = MutableStateFlow(floatArrayOf())

    val faceFeatures: Flow<FloatArray> = _faceFeatures

    private var imageData = floatArrayOf()

    fun run( data : Pair<String,Bitmap>): FloatArray {

        scanImage( data.first , data.second )

        zero.add(Pair("zero", floatArrayOf(0.0F, 0.0F)))
        return if (imagesWithNoFace) {
            floatArrayOf(0.0F, 0.0F)
        } else {
            imageData
        }
    }

    private fun scanImage( name : String , image : Bitmap )
    {
        mainScope.launch {
            val inputImage = InputImage.fromByteArray(
                BitmapUtils.bitmapToNV21ByteArray(image),
                image.width,
                image.height,
                0,
                InputImage.IMAGE_FORMAT_NV21
            )

            val timeToDetectFaces = System.currentTimeMillis()
            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    if (faces.size != 0) {
                        Log.i("Detect Faces",
                            (System.currentTimeMillis()- timeToDetectFaces).toString()  + " мс"
                        )
                        mainScope.launch {
                            val timeToDetectFaceNet = System.currentTimeMillis()

                            val embedding = getEmbedding(image, faces[0].boundingBox)
                            imageData = embedding

                            Log.i("Detect FaceNET",
                                (System.currentTimeMillis() - timeToDetectFaceNet).toString()  + " мс"
                            )

                            _imageHasFace.value = true
                            _imageProcessionEnded.value = true
                            _faceFeatures.value = imageData
                        }
                    }
                    else {
                        _imageHasFace.value = false
                        _imageProcessionEnded.value = true
                        imagesWithNoFace = true
                    }
                }
        }
    }

    // Suspend function for running the FaceNet model
    private suspend fun getEmbedding(image: Bitmap, bbox : Rect) : FloatArray =
        withContext(Dispatchers.Default) {
            return@withContext faceNetModel.getFaceEmbedding(
                BitmapUtils.cropRectFromBitmap(
                    image,
                    bbox
                )
            )
    }

    private fun reset() {
        imageCounter = 0
        numImages = 0
        imagesWithNoFace = false
        data.clear()
    }

}