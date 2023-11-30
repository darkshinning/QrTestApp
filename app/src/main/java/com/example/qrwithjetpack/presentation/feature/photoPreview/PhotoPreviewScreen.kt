package com.example.qrwithjetpack.presentation.feature.photoPreview

import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.qrwithjetpack.presentation.feature.cameraPreview.CameraScreen
import com.example.qrwithjetpack.presentation.navigation.AddProductDestination
import com.example.qrwithjetpack.util.Util

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPreviewScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    lastCapturedPhoto: ImageBitmap? = null,
    photoPreviewViewModel: PhotoPreviewViewModel = hiltViewModel()
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

                    val qrId = photoPreviewViewModel.createQrFromPhoto(capturedPhoto, 1)
                    photoPreviewViewModel.deleteFile(context)

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
                    photoPreviewViewModel.deleteFile(context = context)
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