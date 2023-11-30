package com.example.qrwithjetpack.util
//
//import androidx.lifecycle.ViewModel
//import androidx.navigation.NavController
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//
//@HiltViewModel
//open class BaseViewModel @Inject constructor (
//    private val navController: NavController
//) : ViewModel() {
//
//    fun navigateTo(route: String) {
//        navController.navigate(route) {
//            popUpTo(navController.graph.startDestinationId) {
//                saveState = true
//            }
//            launchSingleTop = true
//            restoreState = true
//        }
//    }
//
//    fun onNavigateBack() {
//        navController.popBackStack()
//    }
//
//}