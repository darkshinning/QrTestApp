package com.example.qrwithjetpack.presentation.feature.qrCameraPreview

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.qrwithjetpack.util.Util
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun QrCameraPreview(
    navController: NavController,
    qrCameraViewModel: QrCameraViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val scannerView = remember { CodeScannerView(context) }

    val codeScanner = remember {
        CodeScanner(context, scannerView).apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            // Callbacks
            decodeCallback = DecodeCallback {
                val scannedData = it.text
                Log.e("ERROR", scannedData)
                MainScope().launch {
//                    val bt = qrCameraViewModel.openTheQr(scannedData)
                    navController.navigate(Util.QRphoto_ROUTE + "/$scannedData")
                }
            }
            errorCallback = ErrorCallback {
            }
        }
    }

    DisposableEffect(codeScanner) {
        codeScanner.startPreview()

        onDispose {
            codeScanner.releaseResources()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(20.dp, Color.Black) //
    ) {
        AndroidView(factory = { scannerView }) {

        }

    }
}


//@Preview
//@Composable
//fun Preview_QR() {
//    QrCameraPreview()
//}