package com.gruppe11.todoApp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.R
import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.ui.elements.LoadingErrorIndicator
import com.gruppe11.todoApp.ui.elements.LoadingIndicator
import com.gruppe11.todoApp.ui.elements.SwitchableButton
import com.gruppe11.todoApp.ui.elements.TimePickerDialog
import com.gruppe11.todoApp.ui.screenStates.ExecutionState
import com.gruppe11.todoApp.viewModel.ScheduleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTimeSlotsScreen(
    viewModel: ScheduleViewModel = hiltViewModel(),
    returnPage: () -> Unit
) {
    val timeSlots by viewModel.timeSlots.collectAsStateWithLifecycle(initialValue = emptyList())
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()

    Scaffold(snackbarHost = {
        SnackbarHost( hostState = snackbarHostState) },
        modifier = Modifier
            .fillMaxHeight()
            .noRippleClickable { focusManager.clearFocus() },
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
        when (loadingState) {
            ExecutionState.RUNNING -> {
                LoadingIndicator()
            }
            ExecutionState.ERROR -> {
                LoadingErrorIndicator(
                    labelText = stringResource(id = R.string.error_timeslot_loading),
                    onRetry = viewModel::loadTimeslots
                )
            }
            ExecutionState.SUCCESS -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(key = { it.id }, items = timeSlots, itemContent = {slot ->
                        EditableTimeSlot(
                            timeSlot = slot,
                            onChanges = { viewModel.updateTimeSlot(it) },
                            onDelete = { viewModel.deleteTimeSlot(it) },
                        )
                        HorizontalDivider(thickness = 1.dp)
                    })
                    item {
                        TextButton(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val createdTimeslot = viewModel.createTimeSlot(
                                        TimeSlot(
                                            id = 0,
                                            name = "New timeslot",
                                            start = LocalTime.of(LocalTime.now().hour, 0, 0),
                                            end = LocalTime.of(
                                                LocalTime.now().plusHours(1).hour,
                                                0,
                                                0
                                            ),
                                            emptyList()
                                        )
                                    )

                                    if (createdTimeslot == null) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Error: Could not create new timeslot")
                                        }
                                    }
                                }
                            },
                            colors = ButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background)
                            Text(text = "Create new timeslot",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditableTimeSlot(
    timeSlot: TimeSlot,
    onChanges: (TimeSlot) -> Unit,
    onDelete: (TimeSlot) -> Unit,
) {
    val changeStart = remember { mutableStateOf(false) }
    val changeEnd = remember { mutableStateOf(false) }
    val deleteModalVisible = remember { mutableStateOf(false) }
    var timeslotName by remember { mutableStateOf(timeSlot.name) }
    var showConfirm by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(15.dp, 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { deleteModalVisible.value = true }) {
            Icon(
                imageVector = Icons.Outlined.RemoveCircleOutline,
                contentDescription = null,
                modifier = Modifier.scale(1.5F),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
        Column(modifier = Modifier.padding(5.dp, 0.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                value = timeslotName,
                onValueChange = {
                    timeslotName = it
                    showConfirm = true
                },
                label = { Text(text = "Name") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.DarkGray,
                    unfocusedLabelColor = Color.DarkGray,
                    unfocusedTextColor = Color.Black,
                    unfocusedTrailingIconColor = Color.DarkGray
                )
            )
            if (showConfirm) {
                Row {
                    SwitchableButton(
                        text = "Cancel", onClick = {
                            timeslotName = timeSlot.name
                            showConfirm = false
                        }, isFilled = false, pickedColor = MaterialTheme.colorScheme.tertiary
                    )
                    SwitchableButton(
                        text = "Confirm", onClick = {
                            onChanges(timeSlot.copy(name = timeslotName))
                            showConfirm = false
                        }, isFilled = true, pickedColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .width(140.dp)
                        .clickable { changeStart.value = true },
                    value = timeSlot.start.format(DateTimeFormatter.ofPattern("HH:mm")),
                    enabled = false,
                    onValueChange = { },
                    label = { Text(text = "Start") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color.DarkGray,
                        disabledLabelColor = Color.DarkGray,
                        disabledTextColor = Color.Black,
                        disabledTrailingIconColor = Color.DarkGray
                    )
                )
                OutlinedTextField(
                    modifier = Modifier
                        .clickable { changeEnd.value = true }
                        .width(140.dp),
                    value = timeSlot.end.format(DateTimeFormatter.ofPattern("HH:mm")),
                    enabled = false,
                    onValueChange = { },
                    label = { Text(text = "End") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color.DarkGray,
                        disabledLabelColor = Color.DarkGray,
                        disabledTextColor = Color.Black,
                        disabledTrailingIconColor = Color.DarkGray
                    )
                )

                if (changeStart.value) {
                    TimePickerDialog(
                        initialTime = timeSlot.start,
                        dismiss = { changeStart.value = false },
                        confirm = { newTime ->
                            onChanges(timeSlot.copy(start = newTime))
                        }
                    )
                }
                if (changeEnd.value) {
                    TimePickerDialog(
                        initialTime = timeSlot.end,
                        dismiss = { changeEnd.value = false },
                        confirm = { newTime ->
                            onChanges(timeSlot.copy(end = newTime))
                        }
                    )
                }
                if (deleteModalVisible.value) {
                    Dialog(onDismissRequest = { deleteModalVisible.value = false }) {
                        ElevatedCard(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Column(modifier = Modifier.padding(15.dp)) {
                                Text(text = "Are you sure you want to delete this timeslot?")
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    SwitchableButton(
                                        text = "Cancel",
                                        onClick = { deleteModalVisible.value = false },
                                        isFilled = false,
                                        pickedColor = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    SwitchableButton(
                                        text = "Delete",
                                        onClick = {
                                            onDelete(timeSlot)
                                            deleteModalVisible.value = false
                                        },
                                        isFilled = true,
                                        pickedColor = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

