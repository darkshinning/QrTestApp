package com.example.qrwithjetpack.util

import androidx.navigation.NavType
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
class Util {
    companion object {
        const val TAG = "CameraX"
        const val APP_NAME = "Qr"
        const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_DIR = "Photos"
        const val EDIT_DIR = "Edited"
        const val PHOTO_EXTENSION = ".jpg"
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0
        const val CAPTURE_FAIL = "Image capture failed."
        const val GENERAL_ERROR_MESSAGE = "Something went wrong."
        const val PHOTO_MODE = 0
        const val VIDEO_MODE = 1
        const val FILTER_MODE = 2
        const val CROP_MODE = 3
        const val FILTER_NAME = "Filter"
        const val CROP_NAME = "Crop"
        const val UNKNOWN_ORIENTATION = -1
        const val ALL_CONTENT = "ALL"
        const val PHOTO_CONTENT = "PHOTOS"
        const val VIDEO_CONTENT = "VIDEOS"
        const val EDIT_CONTENT = "EDITS"

        const val CAMERA_ROUTE = "camera_screen"
        const val PHOTO_ROUTE = "photo_screen"
        const val QR_ROUTE = "qr_screen"
        const val MENU_ROUTE = "main_menu"
        const val QRphoto_ROUTE = "qr_photo"
        const val QRpreview_ROUTE = "qr_preview"

        const val qrId = "qr_id"
        val arguments = listOf(navArgument(name = qrId) {
            type = NavType.StringType
        })
    }
}