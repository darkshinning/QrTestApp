package com.example.qrwithjetpack.presentation.feature.photoFromQr

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.qrwithjetpack.R
import com.example.qrwithjetpack.presentation.feature.addproduct.composables.LoadingScreen
import com.example.qrwithjetpack.presentation.feature.photoPreview.PhotoPreviewViewModel
import com.example.qrwithjetpack.util.Util
import java.util.Base64

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoFromQrScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    photoFromQrViewModel: PhotoFromQrViewModel = hiltViewModel(),
    imageBitmap: String
) {
    Log.e("ERROR", "DAAAAAAAAAAAAM")

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                photoFromQrViewModel.getQrFromDatabase(imageBitmap)
            }
        }
        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val qr = photoFromQrViewModel.qr.collectAsState(initial = "")
    Log.e("ERROR", "After var")

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                title = {
                    Text(
                        text = stringResource(R.string.add_product_text_screen_title),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
            )
        }
    ) { padding ->
        val isLoaded = photoFromQrViewModel.isLoaded.collectAsState(initial = null).value
        val isLoading = photoFromQrViewModel.isLoading.collectAsState(initial = null).value
        if (isLoading == true) {
            LoadingScreen(
                message = "Processing QR",
                onCancelSelected = {
                    navController.navigateUp()
                },
                modifier = Modifier
                    .padding(0.dp, 50.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .padding(1.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
//                    .border(20.dp, Color.Black) //
            ) {
                if (isLoaded == true) {
                    val decoder = Base64.getDecoder()
                    val capturedPhoto: ImageBitmap = BitmapFactory.decodeByteArray(
                        decoder.decode(qr.value),
                        0,
                        decoder.decode(qr.value).size
                    )
                        .asImageBitmap()
                    Image(
                        bitmap = capturedPhoto,
                        contentDescription = "Last captured photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//
//                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
//                    photoPreviewViewModel.deleteFile(context = context)
                        navController.navigate(Util.QR_ROUTE)
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
}

//@Preview
//@Composable
//fun Preview_Photo(
////    navController: NavController
//) {
//    PhotoPreviewScreen()
//}