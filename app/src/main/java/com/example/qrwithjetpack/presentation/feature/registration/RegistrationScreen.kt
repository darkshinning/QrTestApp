package com.example.qrwithjetpack.presentation.feature.registration

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
import com.example.qrwithjetpack.domain.usecase.CreateUserUseCase
import com.example.qrwithjetpack.presentation.feature.registration.composables.RegistrationSuccessScreen
import com.example.qrwithjetpack.presentation.feature.registration.composables.RegistrationFailScreen
import com.example.qrwithjetpack.presentation.feature.registration.composables.RegCheckScreen
import com.example.qrwithjetpack.util.Util

@Composable
fun RegistrationScreen (
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = hiltViewModel(),
    navController: NavController
) {
    val registerSuccess =
        viewModel.registerSuccess.collectAsState(initial = null).value
    val isLoading =
        viewModel.isLoading.collectAsState(initial = null).value
    if (isLoading == true) {
        RegCheckScreen(message = "Тіркелу",
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
                    val login = rememberSaveable { mutableStateOf("") }
                    val password = rememberSaveable { mutableStateOf("") }
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
                                text = "Пароль",
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
                            viewModel.registerUser(login = login.value, password = password.value)
                        }) {
                        Text(text = "Тіркеу")
                    }
                }
            }

            is CreateUserUseCase.Output.Success -> {
                RegistrationSuccessScreen(
                    message = "Тіркелді!",
                    onNavigateBack = {
                        navController.navigate(Util.FIRSTLOGIN_ROUTE)
                    })
            }

            is CreateUserUseCase.Output.Failure -> {
                RegistrationFailScreen(modifier = modifier.padding(16.dp),
                    message = "Қате пайда болды. Қайтадан көріңіз!4",
//                    reason = reasonMessage.value,
                    onRetrySelected = {
                        navController.navigate(Util.REGISTRATION_ROUTE)
                    },
                    onNavigateBack = {
                        navController.navigate(Util.FIRSTLOGIN_ROUTE)
                    }
                )
            }

        }
    }
}