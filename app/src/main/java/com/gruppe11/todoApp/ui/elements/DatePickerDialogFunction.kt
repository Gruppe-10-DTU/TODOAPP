package com.gruppe11.todoApp.ui.elements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

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
            SwitchableButton(
                text = "Ok", onClick = {
                    selectedDate?.let { onDateSelected(it) }
                    onDismiss()
                },
                isFilled = true,
                pickedColor = MaterialTheme.colorScheme.primary
            )
        },
        dismissButton = {
            SwitchableButton(text = "Cancel",
                onClick = { onDismiss() },
                isFilled = false,
                pickedColor = MaterialTheme.colorScheme.tertiary)
        },
        colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        DatePicker(
            state = datePickerState,

            )
    }
}