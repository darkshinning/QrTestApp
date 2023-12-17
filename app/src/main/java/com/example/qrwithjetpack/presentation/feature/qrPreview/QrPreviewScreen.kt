package com.example.qrwithjetpack.presentation.feature.qrPreview

import android.widget.ImageView
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
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.qrwithjetpack.util.Util

@Composable
fun QrPreviewScreen(
    navController: NavController,
    qrId: String,
    qrPreviewViewModel: QrPreviewViewModel = hiltViewModel()
) {
    val context = LocalContext.current
//    val qrView: ImageView = ImageView(context)
//    qrView.maxHeight = 150
//    qrView.maxWidth = 150
//    qrView.scaleType = ImageView.ScaleType.CENTER
    val bitmap = qrPreviewViewModel.createQrfromId(qrId).asImageBitmap()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(200.dp)
//                .weight(1f)
//                .size(width = 1024.dp, height = 1024.dp)
                .border(20.dp, Color.Black)
                .background(Color.Black)
        ) {
            Image(
                bitmap = bitmap,
                contentDescription = "Last captured photo",
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .background(Color.Black)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
//                    photoPreviewViewModel.deleteFile(context = context)
                    navController.navigate(Util.CAMERA_ROUTE)
                },
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = "Қайта түсіру")
            }
        }
    }
}