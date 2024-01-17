package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingErrorIndicator(
    labelText: String,
    onRetry: () -> Unit
) {
    Box(
    modifier = Modifier
    .fillMaxSize()
    .background(MaterialTheme.colorScheme.background),
    contentAlignment = Alignment.Center
    ) {
        Column {
            Text(labelText)
            IconButton(
                onClick = onRetry,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Filled.Sync,
                    contentDescription = "Retry",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}