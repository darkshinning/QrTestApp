package com.example.qrwithjetpack.presentation.feature.mainMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.qrwithjetpack.presentation.feature.cameraPreview.CameraPreviewViewModel
import com.example.qrwithjetpack.presentation.feature.mainMenu.composables.NoPermissionScreen
import com.example.qrwithjetpack.util.Util
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainMenu (
    navController: NavController,
) {
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    MainContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest,
        navController = navController
    )


}

@Composable
private fun MainContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
//    mainMenuVM: MainMenuViewModel = hiltViewModel(),
    navController: NavController
) {
    if (hasPermission) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navController.navigate(Util.QR_ROUTE)
                          },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "QR ашу")
            }
            Button(
                onClick = { navController.navigate(Util.CAMERA_ROUTE) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Камера ашу")
            }
        }
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}