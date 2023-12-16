package com.example.qrwithjetpack.presentation.feature.cameraPreview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
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
//    val cameraState: CameraState by viewModel.state.replayCa

    if (RegisteredUser.registeredlogin.login == "") {
        Log.e("NO LOG", RegisteredUser.registeredlogin.login)
        RegistrationFailScreen(message = "damnNigga", onRetrySelected = { }) {
        }
    } else {
        Log.e("LOG", RegisteredUser.registeredlogin.login)
        CameraPreviewScreen(
//        onPhotoCaptured = viewModel::storePhotoInGallery,
//        lastCapturedPhoto = cameraState.capturedImage,
            navController = navController
        )
    }
}

@Composable
fun CameraPreviewScreen(
//    onPhotoCaptured: (Bitmap) -> Unit,

//    modifier: Modifier = Modifier,
    navController: NavController,
//    cameraPreviewVM: CameraPreviewViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
//    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//    var imageProxy by remember { mutableStateOf<ImageProxy?>(null) }
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val photoFile = getPhotoFile(context)
    var lastCapturedPhoto: ImageBitmap? = null

    if (photoFile.exists()) {
        val lastCapturedPhotoInBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
        lastCapturedPhoto = lastCapturedPhotoInBitmap.asImageBitmap()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
//                .border(20.dp, Color.Black) //
        ) {

            AndroidView(
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.Black.hashCode())
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START


                    }.also { previewView ->
                        imageCapture = ImageCapture.Builder()
                            .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
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
//                    .offset(y = 200.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    Spacer(modifier = Modifier
                        .height(16.dp)
                        .weight(10f))
                    Button(
                        onClick = { navController.navigate(Util.MENU_ROUTE) },
                        modifier = Modifier
                            .weight(1f)
//                        .padding(50.dp)
                            .size(50.dp, 30.dp)
                    ) {
                        Text(text = "X")
                    }
                }

                Spacer(modifier = Modifier
                    .height(16.dp)
                    .weight(15f))

                Button(
                    onClick = {
                        capturePhoto(context, cameraController, imageCapture)
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
    if (lastCapturedPhoto != null) {
        PhotoPreviewScreen(
            lastCapturedPhoto = lastCapturedPhoto,
            navController = navController
        )
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
//    onPhotoCaptured: (Bitmap) -> Unit,
    imageCapture: ImageCapture?,
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val imageCap = imageCapture ?: return

            val correctedBitmap = image.toBitmap()
            val imageFileName = "temporary_photo"

            val fos = FileOutputStream(createOutputFile(context, imageFileName))
            correctedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos)
            fos.flush()
            fos.close()

            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}

private fun createOutputFile(context: Context, imageFileName: String): File {

    val storageDirectory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File(storageDirectory, "$imageFileName.jpg")
}

fun getPhotoFile(context: Context): File {
    val storageDirectory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(storageDirectory, "temporary_photo.jpg")
}
