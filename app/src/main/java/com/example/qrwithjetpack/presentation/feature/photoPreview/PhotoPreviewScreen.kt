package com.example.qrwithjetpack.presentation.feature.photoPreview

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.qrwithjetpack.presentation.feature.cameraPreview.CameraPreviewViewModel
import com.example.qrwithjetpack.util.Util


@Composable
fun PhotoPreviewScreen(
    navController: NavController,
    lastCapturedPhoto: ImageBitmap? = null,
    cameraPreviewViewModel: CameraPreviewViewModel = hiltViewModel()
) {
    val capturedPhoto: ImageBitmap = lastCapturedPhoto!!
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .border(20.dp, Color.Black) //
        ) {
            Image(
                bitmap = capturedPhoto,
                contentDescription = "Last captured photo",
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
//
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Button(
                onClick = {
//                    capturePhoto(context, cameraController, imageCapture)

                    val qrId = cameraPreviewViewModel.createQrFromPhoto(capturedPhoto)
                    cameraPreviewViewModel.deleteFile(context)

                    navController.navigate(Util.QRpreview_ROUTE + "/$qrId")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "QR-ға айналдыру")
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

//@Preview
//@Composable
//fun Preview_Photo(
////    navController: NavController
//) {
//    PhotoPreviewScreen()
//}