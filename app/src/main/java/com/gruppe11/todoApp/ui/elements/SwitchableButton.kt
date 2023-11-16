package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SwitchableButton(
    text: String,
    onClick: () -> Unit,
    isFilled: Boolean,
    pickedColor: Color,
    modifier: Modifier = Modifier
) {
    if (isFilled) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.background,
                containerColor = pickedColor,
            ),
            modifier = modifier
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = pickedColor,
                containerColor = Color.Transparent,
            ),
            border = BorderStroke(2.dp, pickedColor),
            modifier = modifier
        ) {
            Text(text)
        }
    }
}