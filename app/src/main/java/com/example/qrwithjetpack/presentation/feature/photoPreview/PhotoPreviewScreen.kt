package com.example.qrwithjetpack.presentation.feature.photoPreview

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.qrwithjetpack.presentation.feature.cameraPreview.CameraPreviewViewModel
import com.example.qrwithjetpack.util.Util
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PhotoPreviewScreen(
    navController: NavController,
    lastCapturedPhoto: ImageBitmap? = null,
    cameraPreviewViewModel: CameraPreviewViewModel = hiltViewModel()
) {
    val capturedPhoto: ImageBitmap = lastCapturedPhoto!!
    val context = LocalContext.current
    val imagesWithNoFace = cameraPreviewViewModel.imagesWithNoFace.collectAsState(initial = null).value
    val imageProcessionEnded = cameraPreviewViewModel.imageAnalyzer.imageProcessionEnded.collectAsState(
        initial = false
    ).value
    val imageHasFace = cameraPreviewViewModel.imageAnalyzer.imageHasFace.collectAsState(
        initial = false
    ).value
    val faceFeatures = cameraPreviewViewModel.imageAnalyzer.faceFeatures.collectAsState(
        initial = floatArrayOf()
    ).value

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SnackbarHost(snackbarHostState.value)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(14f)
                .border(10.dp, Color.Black) //
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(20.dp, 30.dp)
                    .border(10.dp, Color.Black) //
            ) {
                if (imageProcessionEnded && !imageHasFace) {
                    scope.launch {
                        snackbarHostState.value.showSnackbar("Суретте бет жоқ! Қайттан суретке түсіріңіз.")
                    }
                } else if (imageProcessionEnded && imageHasFace) {
                    Log.i("Detect Face Features",
                        (System.currentTimeMillis() - cameraPreviewViewModel.timeToDetectFaceFeatures).toString() + " мс"
                    )
                    scope.launch {
                        snackbarHostState.value.showSnackbar("Суретте бет бар! QR-код жасауға рұқсат.")
                    }
                }
            }
            Row(modifier = Modifier
                .padding(2.dp)
            ) {
                Image(
                    bitmap = capturedPhoto,
                    contentDescription = "Соңғы түсірілген сурет",
                    contentScale = androidx.compose.ui.layout.ContentScale.FillWidth,
                    modifier = Modifier.clip(RoundedCornerShape(15.dp)),

                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(12.dp)
        ) {
            if (imageProcessionEnded && imageHasFace) {
                Button(
                    onClick = {

                        val qrId = cameraPreviewViewModel.createQrFromPhoto(capturedPhoto, faceFeatures)

                        cameraPreviewViewModel.deleteFile(context)

                        navController.navigate(Util.QRpreview_ROUTE + "/$qrId")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = "QR жасау")
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    cameraPreviewViewModel.deleteFile(context = context)
                    navController.navigate(Util.CAMERA_ROUTE)
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "Қайта түсіру")
            }
        }
    }
}