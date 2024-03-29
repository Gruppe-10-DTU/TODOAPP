package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialTime: LocalTime,
    dismiss: () -> Unit,
    confirm: (LocalTime) -> Unit,

    ) {
    val state = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )
    Dialog(
        onDismissRequest = dismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        ElevatedCard(
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.background,
                disabledContentColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Spacer(modifier = Modifier.padding(20.dp))
            TimePicker(
                state = state,
                modifier = Modifier.padding(20.dp),
                colors = TimePickerColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    clockDialColor = MaterialTheme.colorScheme.primaryContainer,
                    clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    clockDialUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    periodSelectorBorderColor = MaterialTheme.colorScheme.onPrimary,
                    periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectorColor = MaterialTheme.colorScheme.primary,
                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                    timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                layoutType = TimePickerLayoutType.Vertical
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SwitchableButton(
                    text = "Cancel",
                    onClick = { dismiss() },
                    isFilled = false,
                    pickedColor = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(5.dp))
                SwitchableButton(
                    text = "Save",
                    onClick = {
                        confirm(LocalTime.of(state.hour, state.minute, 0))
                        dismiss()
                    },
                    isFilled = true,
                    pickedColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}