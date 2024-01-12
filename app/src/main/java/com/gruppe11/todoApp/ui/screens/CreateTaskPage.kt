package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.ui.elements.DatePickerDialogFunction
import com.gruppe11.todoApp.ui.elements.HorizDividerWithSpacer
import com.gruppe11.todoApp.ui.elements.PriorityFC
import com.gruppe11.todoApp.ui.elements.SwitchableButton
import com.gruppe11.todoApp.ui.screenStates.ExecutionState
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme
import com.gruppe11.todoApp.viewModel.CreateTaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskContent(
    returnPage: () -> Unit, viewModel: CreateTaskViewModel = hiltViewModel(), taskId: Int? = null
) {
    if (taskId != null) {
        viewModel.getTask(taskId)
    }
    val currentTask = viewModel.editingTask.collectAsStateWithLifecycle()
    var subtaskName by remember { mutableStateOf("") }
    var showSubTaskDialog by remember { mutableStateOf(false) }
    val subtaskFocusRequester = remember { FocusRequester() }
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var hasTimeslot = false
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var scheduleChecked by remember { mutableStateOf(false) }
    var timeSlotVisible by remember { mutableStateOf(false) }
    val timeSlots = viewModel.Timeslots.collectAsStateWithLifecycle(initialValue = emptyList())
    val focusManager = LocalFocusManager.current
    val switchIcon: (@Composable () -> Unit)? = if (scheduleChecked) {
        {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        null
    }

    Scaffold(snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState
        )
    }, modifier = Modifier.fillMaxHeight(), topBar = {
        TopAppBar(
            title = {
                Text(
                    "Create Task", maxLines = 1
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
    }, bottomBar = {
        HorizontalDivider()
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            //Cancel button
            SwitchableButton(
                text = "Cancel",
                onClick = returnPage,
                isFilled = false,
                pickedColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            // Create button
            SwitchableButton(
                text = if (taskId != null) "Save" else "Create",
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (currentTask.value.title.isNotEmpty()) {
                            var message: String
                            if (taskId != null) message = "Task updated" else message =
                                "Task created"
                            val task = viewModel.submitTask()
                            if (viewModel.submitState.value == ExecutionState.SUCCESS &&
                                task != null) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(message = message)
                                }
                                returnPage()
                            } else if (viewModel.submitState.value == ExecutionState.ERROR &&
                                task != null
                            ) {
                                message = "Error: Task was saved but subtasks failed to save"
                                scope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            } else if (viewModel.submitState.value == ExecutionState.ERROR) {
                                message = "Error: Could not save task"
                                scope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        } else {
                            dismissSnackbar(snackbarHostState, scope)
                            scope.launch {
                                snackbarHostState.showSnackbar("Missing Task Name")
                            }
                        }
                    }
                },
                isFilled = true,
                pickedColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }

    }) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .noRippleClickable { focusManager?.clearFocus() }) {
            item {
                Spacer(modifier = Modifier.height(60.dp))
                // Main task Input
                OutlinedTextField(
                    value = currentTask.value.title,
                    onValueChange = { viewModel.editTitle(it) },
                    label = { Text("Task name") },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                )
                HorizDividerWithSpacer(10.dp)
                Text(
                    text = "Choose priority",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                PriorityFC(
                    selectedPriority = currentTask.value.priority, onClick = viewModel::editPriority
                )
                HorizDividerWithSpacer(10.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(10.dp, 10.dp)
                ) {

                    Text(
                        text = "Add Subtask",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
                        showSubTaskDialog = true
                        subtaskName = ""
                    }, content = {
                        Icon(
                            imageVector = Icons.Filled.AddCircleOutline,
                            contentDescription = "Add Subtask",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    })
                }

                if (showSubTaskDialog) {
                    OutlinedTextField(
                        value = subtaskName,
                        onValueChange = { subtaskName = it },
                        label = { Text("Subtask name") },
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.focusRequester(subtaskFocusRequester),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                    LaunchedEffect(Unit) {
                        subtaskFocusRequester.requestFocus()
                    }
                    Row {
                        SwitchableButton(
                            text = "Cancel", onClick = {
                                showSubTaskDialog = false
                            }, isFilled = false, pickedColor = MaterialTheme.colorScheme.tertiary
                        )
                        SwitchableButton(
                            text = "Confirm", onClick = {
                                viewModel.addSubtask(subtaskName)
                                subtaskName = ""
                                showSubTaskDialog = false
                            }, isFilled = true, pickedColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                currentTask.value.subtasks.forEachIndexed() { index, subtask ->
                    subtaskItem(
                        subtask = subtask,
                        removeSubTask = viewModel::removeSubtask,
                        viewModel::editSubtask,
                        index
                    )
                }
                HorizDividerWithSpacer(10.dp)
                Text(
                    text = "Select date",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp, 10.dp)
                ) {
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            containerColor = Color.Transparent,
                        ),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(60.dp)
                    ) {
                        Text(
                            text = currentTask.value.deadline.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            ),
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    IconButton(onClick = { showDatePicker = true }, content = {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Pick a date",
                            modifier = Modifier.scale(1.3f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    })
                }

                HorizDividerWithSpacer(dividerHeight = 10.dp)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(10.dp, 10.dp)
                ) {
                    Text(
                        text = "Schedule ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        modifier = Modifier.scale(0.8f),
                        checked = scheduleChecked,
                        onCheckedChange = {
                            scheduleChecked = it
                        },
                        thumbContent = switchIcon,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.background,
                            uncheckedTrackColor = MaterialTheme.colorScheme.background,
                            uncheckedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                if(viewModel.getScheduleState()){
                    scheduleChecked = true
                }

                var slotText = currentTask.value.timeslot?.name ?: "Select Timeslot"
                if (scheduleChecked && currentTask.value.timeslot != null) {
                    Row{
//                        Text(text = "Select Timeslot:")
                        TextButton(
                            onClick = { timeSlotVisible = !timeSlotVisible },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = (slotText),
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(20.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "See timeslots",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        DropdownMenu(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            expanded = timeSlotVisible,
                            onDismissRequest = { timeSlotVisible = false }) {
                            timeSlots.value.sortedBy { it.name }.forEach { timeSlot ->
                                DropdownMenuItem(text = { Text(text = timeSlot.name) }, onClick = {
                                    viewModel.editTimeslot(timeSlot)
                                    timeSlotVisible = !timeSlotVisible
                                })
                            }
                        }
                    }
                    if ( currentTask.value.timeslot != null ) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Period: ${
                                currentTask.value.timeslot?.start?.format(
                                    DateTimeFormatter.ofPattern("HH:mm")
                                )
                            } to ${
                                currentTask.value.timeslot?.end?.format(
                                    DateTimeFormatter.ofPattern("HH:mm")
                                )
                            }",
                            fontWeight = FontWeight.Bold
                        )

                    }
                } else {
                    viewModel.editTimeslot(null)
                    viewModel.openWithSchedule(false)
                }
                if (showDatePicker) {
                    DatePickerDialogFunction(System.currentTimeMillis(),
                        onDateSelected = viewModel::editDeadline,
                        onDismiss = { showDatePicker = false })
                }
                Box(modifier = Modifier.padding(padding))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTaskPreview() {
    TODOAPPTheme {
        CreateTaskContent({})
    }
}

fun dismissSnackbar(snackbarHostState: SnackbarHostState, scope: CoroutineScope) {
    if (snackbarHostState.currentSnackbarData != null) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.noRippleClickable(
    onClick: () -> Unit
): Modifier = composed {
    clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}


@Composable
fun subtaskItem(
    subtask: SubTask,
    removeSubTask: (SubTask) -> Unit,
    editSubTask: (Int, String, SubTask) -> Unit,
    index: Int
) {
    var subtaskName by remember { mutableStateOf(subtask.title) }
    var showConfirm by remember { mutableStateOf(false) }
    Row(
        Modifier.padding(vertical = 4.dp)
    ) {
        IconButton(onClick = {
            removeSubTask(subtask)
        }) {
            Icon(
                imageVector = Icons.Outlined.RemoveCircleOutline,
                contentDescription = "Delete Subtask",
                modifier = Modifier.scale(1.3f),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
//                        Text(text = subtask.title)
        OutlinedTextField(
            value = subtaskName, onValueChange = {
                subtaskName = it
                showConfirm = true
            }, colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
    if (showConfirm) {
        Row {
            SwitchableButton(
                text = "Cancel", onClick = {
                    subtaskName = subtask.title
                    showConfirm = false
                }, isFilled = false, pickedColor = MaterialTheme.colorScheme.tertiary
            )
            SwitchableButton(
                text = "Confirm", onClick = {
                    editSubTask(index, subtaskName, subtask)
                    showConfirm = false
                }, isFilled = true, pickedColor = MaterialTheme.colorScheme.primary
            )
        }
    }

}

