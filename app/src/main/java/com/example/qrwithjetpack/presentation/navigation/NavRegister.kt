package com.example.qrwithjetpack.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.qrwithjetpack.presentation.feature.cameraPreview.CameraScreen
import com.example.qrwithjetpack.presentation.feature.loginMenu.LoginMenuScreen
import com.example.qrwithjetpack.presentation.feature.mainMenu.MainMenu
import com.example.qrwithjetpack.presentation.feature.photoFromQr.PhotoFromQrScreen
import com.example.qrwithjetpack.presentation.feature.photoPreview.PhotoPreviewScreen
import com.example.qrwithjetpack.presentation.feature.qrCameraPreview.QrCameraPreview
import com.example.qrwithjetpack.presentation.feature.qrPreview.QrPreviewScreen
import com.example.qrwithjetpack.presentation.feature.registration.RegistrationScreen
import com.example.qrwithjetpack.util.Util


fun NavGraphBuilder.navRegistration(navController: NavController) {


    composable(Util.CAMERA_ROUTE) {
        CameraScreen(
            navController = navController
        )
    }

    composable(Util.MENU_ROUTE) {
        MainMenu(
            navController = navController
        )
    }

    composable(Util.PHOTO_ROUTE) {
        PhotoPreviewScreen(
            navController = navController
        )
    }

    composable(Util.QR_ROUTE) {
        QrCameraPreview(
            navController = navController
        )
    }

    composable(Util.REGISTRATION_ROUTE) {
        RegistrationScreen(
            navController = navController
        )
    }

    composable(Util.FIRSTLOGIN_ROUTE) {
        LoginMenuScreen(
            navController = navController
        )
    }

    composable(Util.QRphoto_ROUTE + "/{${Util.qrId}}",
        arguments = Util.arguments) { navBackStack ->

        val bitmap = navBackStack.arguments?.getString(Util.qrId).toString()
        PhotoFromQrScreen(
            navController = navController,
            imageBitmap = bitmap
        )
    }

    composable(Util.QRpreview_ROUTE + "/{qrId}") { navBackStack ->

        val qrId = navBackStack.arguments?.getString("qrId").toString()
        QrPreviewScreen(
            navController = navController, qrId
        )
    }
//    composable(
//        route = "${ProductDetailsDestination.route}/{${ProductDetailsDestination.productId}}",
//        arguments = ProductDetailsDestination.arguments
//    ) { navBackStackEntry ->
//        val productId =
//            navBackStackEntry.arguments?.getString(ProductDetailsDestination.productId)
//        ProductDetailsScreen(
//            productId = productId,
//            navController = navController,
//        )
//    }

}