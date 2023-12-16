package com.example.qrwithjetpack.presentation.feature.loginMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.qrwithjetpack.RegisteredUser
import com.example.qrwithjetpack.presentation.feature.mainMenu.MainMenu
import com.example.qrwithjetpack.util.Util

@Composable
fun LoginMenuScreen(
    navController: NavController
) {
    val user = RegisteredUser.registeredlogin.login

    if (user == "") {
        UserIsNotRegisteredScreen(navController)
    } else {
        UserIsRegisteredScreen(navController)
    }
}

@Composable
private fun UserIsRegisteredScreen(
    navController: NavController
){
    MainMenu(navController = navController)
}

@Composable
private fun UserIsNotRegisteredScreen(
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                navController.navigate(Util.REGISTRATION_ROUTE)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Кіру")
        }
        Button(
            onClick = {
                navController.navigate(Util.REGISTRATION_ROUTE)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Тіркелу")
        }
    }

}