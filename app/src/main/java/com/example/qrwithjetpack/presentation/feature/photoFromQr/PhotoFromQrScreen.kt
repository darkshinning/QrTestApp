package com.example.qrwithjetpack.presentation.feature.photoFromQr

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.qrwithjetpack.presentation.feature.additionalScreens.composables.LoadingScreen
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
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                photoFromQrViewModel.getQrFromDatabase(imageBitmap)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val qr = photoFromQrViewModel.qr.collectAsState(initial = "")

    Scaffold(
//        topBar = {
//            TopAppBar(
//                navigationIcon = {
//                    IconButton(onClick = {
//                        navController.navigateUp()
//                    }) {
//                        Icon(
//                            imageVector = Icons.Filled.ArrowBack,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.onPrimary
//                        )
//                    }
//                },
//                backgroundColor = MaterialTheme.colorScheme.primary,
//                title = {
//                    Text(
//                        text = ,
//                        color = MaterialTheme.colorScheme.onPrimary,
//                    )
//                },
//            )
//        }
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
                .padding(5.dp),
//            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(13f)
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
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .clickable(onClick = { })
                    .weight(1f)
                    .padding(bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
//                    photoPreviewViewModel.deleteFile(context = context)
                        navController.navigate(Util.QR_ROUTE)
                    },
                    modifier = Modifier
//                        .requiredWidth(100.dp)
                        .fillMaxHeight()
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