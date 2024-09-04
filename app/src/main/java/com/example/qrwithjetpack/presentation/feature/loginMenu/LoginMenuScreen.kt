package com.example.qrwithjetpack.presentation.feature.loginMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.qrwithjetpack.RegisteredUser
import com.example.qrwithjetpack.domain.usecase.CreateUserUseCase
import com.example.qrwithjetpack.presentation.feature.mainMenu.MainMenu
import com.example.qrwithjetpack.presentation.feature.registration.composables.RegCheckScreen
import com.example.qrwithjetpack.presentation.feature.registration.composables.RegistrationFailScreen
import com.example.qrwithjetpack.presentation.feature.registration.composables.RegistrationSuccessScreen
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
                navController.navigate(Util.FIRSTREGISTRATION_ROUTE)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Тіркелу")
        }
    }
}


@Composable
fun FirstRegistrationFormScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val registerSuccess =
        loginViewModel.registerSuccess.collectAsState(initial = null).value
    val isLoading =
        loginViewModel.isLoading.collectAsState(initial = null).value
    if (isLoading == true) {
        RegCheckScreen(message = "Тіркеу",
            onCancelSelected = {
                navController.navigateUp()
            })
    } else {
        when (registerSuccess) {
            null -> {
                Column(
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    val firstname = rememberSaveable { mutableStateOf("") }
                    val lastname = rememberSaveable { mutableStateOf("") }
                    val login = rememberSaveable { mutableStateOf("") }
                    val password = rememberSaveable { mutableStateOf("") }
                    OutlinedTextField(
                        label = {
                            Text(
                                text = "Есіміңіз",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        shape = RoundedCornerShape(32),
                        modifier = modifier.fillMaxWidth(),
                        value = firstname.value,
                        onValueChange = {
                            firstname.value = it
                        },
                    )
                    Spacer(modifier = modifier.height(12.dp))
                    OutlinedTextField(
                        label = {
                            Text(
                                text = "Тегіңіз",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        shape = RoundedCornerShape(32),
                        modifier = modifier.fillMaxWidth(),
                        value = lastname.value,
                        onValueChange = {
                            lastname.value = it
                        },
                    )
                    Spacer(modifier = modifier.height(12.dp))
                    OutlinedTextField(
                        label = {
                            Text(
                                text = "Логин",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(32),
                        modifier = modifier.fillMaxWidth(),
                        value = login.value,
                        onValueChange = {
                            login.value = it
                        },
                    )
                    Spacer(modifier = modifier.height(12.dp))
                    OutlinedTextField(
                        label = {
                            Text(
                                text = "Құпиясөз",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        maxLines = 1,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(32),
                        modifier = modifier.fillMaxWidth(),
                        value = password.value,
                        onValueChange = {
                            password.value = it
                        },
                    )
                    Spacer(modifier = modifier.height(12.dp))
                    Spacer(modifier = modifier.weight(1f))
                    OutlinedButton(
                        modifier = modifier
                            .fillMaxWidth(),
                        onClick = {
                            navController.navigate(Util.FIRSTLOGIN_ROUTE)
                        }) {
                        Text(text = "Кері қайту")
                    }
                    Spacer(modifier = modifier.height(12.dp))
                    Button(
                        modifier = modifier.fillMaxWidth(),
                        onClick = {
                            loginViewModel.loginUser(
                                firstname = firstname.value,
                                lastname = lastname.value,
                                login = login.value,
                                password = password.value)
                        }) {
                        Text(text = "Тіркеу")
                    }
                }
            }

            is CreateUserUseCase.Output.Success -> {
                RegistrationSuccessScreen(
                    message = "Тіркелдіңіз!",
                    onNavigateBack = {
                        navController.navigate(Util.FIRSTLOGIN_ROUTE)
                    })
            }

            is CreateUserUseCase.Output.Failure.Conflict -> {
                RegistrationFailScreen(modifier = modifier.padding(16.dp),
                    message = "Мұндай логин уже бар. Басқасын таңдаңыз!",
                    onRetrySelected = {
                        navController.navigate(Util.FIRSTREGISTRATION_ROUTE)
                    },
                    onNavigateBack = {
                        navController.navigate(Util.FIRSTLOGIN_ROUTE)
                    }
                )
            }
            is CreateUserUseCase.Output.Failure -> {
                RegistrationFailScreen(modifier = modifier.padding(16.dp),
                    message = "Белгісіз қате пайда болды.",
                    onRetrySelected = {
                        navController.navigate(Util.FIRSTREGISTRATION_ROUTE)
                    },
                    onNavigateBack = {
                        navController.navigate(Util.FIRSTLOGIN_ROUTE)
                    }
                )
            }
        }
    }
}