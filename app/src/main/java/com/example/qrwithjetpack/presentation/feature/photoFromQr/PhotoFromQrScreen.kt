package com.example.qrwithjetpack.presentation.feature.photoFromQr

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.qrwithjetpack.presentation.feature.additionalScreens.composables.LoadingScreen
import com.example.qrwithjetpack.ui.theme.Purple40
import com.example.qrwithjetpack.util.Util
import kotlinx.coroutines.launch
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

    val firstname = photoFromQrViewModel.firstname.collectAsState(
        initial = "Өңделуде..."
    ).value
    val lastname = photoFromQrViewModel.lastname.collectAsState(
        initial = "Өңделуде..."
    ).value
    val biometry1 = photoFromQrViewModel.biometry1.collectAsState(
        initial = "Өңделуде..."
    ).value
    val biometry2 = photoFromQrViewModel.biometry2.collectAsState(
        initial = "Өңделуде..."
    ).value
    val biometry3 = photoFromQrViewModel.biometry3.collectAsState(
        initial = "Өңделуде..."
    ).value

    Scaffold(

    ) { padding ->
        val isLoaded = photoFromQrViewModel.isLoaded.collectAsState(initial = null).value
        val isLoading = photoFromQrViewModel.isLoading.collectAsState(initial = null).value
        if (isLoading == true) {
            LoadingScreen(
                message = "QR өңделуде...",
                onCancelSelected = {
                    navController.navigateUp()
                },
                modifier = Modifier
                    .padding(0.dp, 50.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.Black),
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp, 10.dp, 2.dp, 2.dp)
                    .sizeIn(20.dp, 60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Purple40)
            ) {
                Column {
                    Text(
                        text = "Жіберуші есімі: $firstname",
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(3.dp, 1.dp, 2.dp, 1.dp)
                    )
                    Text(
                        text = "Жіберуші тегі: $lastname",
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(3.dp, 1.dp, 2.dp, 1.dp)
                    )
                    Text(
                        text = "Биометриялық ақпарат: ${"$biometry1, $biometry2, $biometry3, ..."}",
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(3.dp, 1.dp, 2.dp, 1.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(30.dp)
                    .sizeIn(20.dp, 60.dp,350.dp, 350.dp)
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
                        alignment = Alignment.Center,
                        contentDescription = "Last captured photo",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize()
//                            .border(2.dp, Purple40)
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .weight(4f)
            )
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