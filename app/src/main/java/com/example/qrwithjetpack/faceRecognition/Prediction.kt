package com.example.qrwithjetpack.faceRecognition

import android.graphics.Rect

data class Prediction( var bbox : Rect, var label : String , var maskLabel : String = "" )