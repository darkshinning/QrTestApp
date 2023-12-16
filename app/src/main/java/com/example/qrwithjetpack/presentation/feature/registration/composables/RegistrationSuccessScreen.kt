package com.example.qrwithjetpack.presentation.feature.registration.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RegistrationSuccessScreen(
    modifier: Modifier = Modifier,
    message: String,
    onNavigateBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle, contentDescription = null,
            modifier = modifier.size(128.dp),
            tint = Color.Green
        )
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = modifier.weight(1.0f))

        Button(
            modifier = modifier
                .fillMaxWidth(),
            onClick = onNavigateBack
        ) {
            Text(text = "Басты меню")
        }
    }
}