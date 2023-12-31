package com.example.qrwithjetpack.presentation.feature.registration.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegCheckScreen(
    modifier: Modifier = Modifier,
    message: String,
    onCancelSelected: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(modifier = modifier.size(36.dp))
        Text(text = message,
            style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = modifier.weight(1f))
        OutlinedButton(
            modifier = modifier
                .fillMaxWidth(),
            onClick = onCancelSelected
        ) {
            Text(text = "Қайту")
        }
    }

}