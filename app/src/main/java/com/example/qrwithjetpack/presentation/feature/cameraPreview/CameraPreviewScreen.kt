package com.example.qrwithjetpack.presentation.feature.cameraPreview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.Surface.ROTATION_0
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_90
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.qrwithjetpack.RegisteredUser
import com.example.qrwithjetpack.presentation.feature.photoPreview.PhotoPreviewScreen
import com.example.qrwithjetpack.presentation.feature.registration.composables.RegistrationFailScreen
import com.example.qrwithjetpack.util.Util
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executor


@Composable
fun CameraScreen(
    navController: NavController
) {
    CameraPreviewScreen(
        navController = navController
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun CameraPreviewScreen(
    navController: NavController,
    cameraPreviewVM: CameraPreviewViewModel = hiltViewModel()
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    val photoFile = cameraPreviewVM.getPhotoFile(context)

    var lastCapturedPhoto: ImageBitmap? = null

    lateinit var images: Pair<String, Bitmap>

    if (photoFile.exists()) {
        val lastCapturedPhotoInBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
        lastCapturedPhoto = lastCapturedPhotoInBitmap.asImageBitmap()
    }

    val isPhotoTaken = cameraPreviewVM.isPhotoTaken.collectAsState(initial = null).value

    if (isPhotoTaken == true) {
        PhotoPreviewScreen(
            lastCapturedPhoto = lastCapturedPhoto,
            navController = navController
        )
    }
    else
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AndroidView(
                    factory = { context ->
                        PreviewView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                            setBackgroundColor(Color.Black.hashCode())
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            scaleType = PreviewView.ScaleType.FIT_CENTER

                        }.also { previewView ->
                            imageCapture = ImageCapture.Builder()
                                .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
//                                .setTargetRotation(ROTATION_90)
                                .build()

                            previewView.controller = cameraController
                            cameraController.bindToLifecycle(lifecycleOwner)

                        }
                    }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(16.dp)
                                .weight(10f)
                        )
                        Button(
                            onClick = {
                                cameraPreviewVM.deleteFile(context)
                                navController.navigate(Util.MENU_ROUTE)
                                      },
                            modifier = Modifier
                                .weight(1f)
                                .size(50.dp, 50.dp)
                        ) {
                            Text(text = "X")
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .weight(15f)
                    )

                    Button(
                        onClick = {
                            cameraPreviewVM.capturePhoto(context, cameraController, imageCapture)
                        },
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .weight(1f)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Фото түсіру")
                    }
                }
            }
        }
    }
}

