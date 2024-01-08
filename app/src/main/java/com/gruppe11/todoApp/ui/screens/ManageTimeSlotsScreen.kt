package com.gruppe11.todoApp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.ui.elements.TimePickerDialog
import com.gruppe11.todoApp.viewModel.ScheduleViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTimeSlots(
    viewModel: ScheduleViewModel = hiltViewModel(),
    returnPage: () -> Unit
) {
    val timeSlots = viewModel.timeSlots.collectAsStateWithLifecycle(initialValue = emptyList())
    val deleteModalVisible = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxHeight(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Manage time slots", maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = returnPage) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
            )
        }) {padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = timeSlots.value, itemContent = {slot ->
                EditableTimeSlot(
                    timeSlot = slot,
                    onChanges = { viewModel.updateTimeSlot(it) },
                    onDelete = {  },
                )
                HorizontalDivider(thickness = 1.dp)
            })
            item {
                TextButton(
                    modifier = Modifier.padding(vertical = 10.dp),
                    onClick = { /*TODO*/ },
                    colors = ButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Text(text = "Create new timeslot", fontSize = 20.sp)
                }
            }
        }
        if (deleteModalVisible.value){
            // TODO DELETE DIALOG
            Dialog(onDismissRequest = { /*TODO*/ }) {
                Text(text = "NOT IMPLEMENTED")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableTimeSlot(
    timeSlot: TimeSlot,
    onChanges: (TimeSlot) -> Unit,
    onDelete: () -> Unit,
){
    val changeStart = remember{ mutableStateOf(false) }
    val changeEnd = remember{ mutableStateOf(false) }

    Row(modifier = Modifier.padding(15.dp,5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onDelete() } ) {
            Icon(
                imageVector = Icons.Default.RemoveCircleOutline,
                contentDescription = null,
                modifier = Modifier.scale(1.5F))
        }
        Column(modifier = Modifier.padding(5.dp,0.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                value = timeSlot.name,
                onValueChange = { onChanges(timeSlot.copy(name = it)) },
                label = { Text(text = "Name")}
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(
                    onClick = { changeStart.value = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(140.dp)
                ) {
                    Text(
                        text = timeSlot.start.format(DateTimeFormatter.ofPattern("HH:mm")),
                        fontSize = 18.sp
                    )
                }
                OutlinedButton(
                    onClick = { changeEnd.value = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(140.dp)
                ) {
                    Text(
                        text = timeSlot.end.format(DateTimeFormatter.ofPattern("HH:mm")),
                        fontSize = 18.sp
                    )
                }
                if (changeStart.value) {
                    TimePickerDialog(
                        dismiss = { changeStart.value = false },
                        confirm = {hours, minutes ->
                            val newTime = LocalTime.of(hours, minutes)
                            onChanges(timeSlot.copy(start = newTime))
                        }
                    )
                }
                if (changeEnd.value) {
                    TimePickerDialog(
                        dismiss = { changeEnd.value = false },
                        confirm = {hours, minutes ->
                            val newTime = LocalTime.of(hours, minutes)
                            onChanges(timeSlot.copy(end = newTime))
                        }
                    )
                }

            }
        }
    }
}

