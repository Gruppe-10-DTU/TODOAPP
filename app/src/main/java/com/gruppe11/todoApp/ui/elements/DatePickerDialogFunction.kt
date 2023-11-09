package com.gruppe11.todoApp.ui.elements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogFunction(
    taskDateTimeMillis: Long,
    onDateSelected: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = taskDateTimeMillis,
        yearRange = IntRange(2000, LocalDateTime.now().year + 100)
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
    }

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    selectedDate?.let { onDateSelected(it) }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.background,
                    containerColor = MaterialTheme.colorScheme.tertiary,
                ),

                ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary,
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
            ) {
                Text(text = "Cancel")
            }
        },
        colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        DatePicker(
            state = datePickerState,

            )
    }
}