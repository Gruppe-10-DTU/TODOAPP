package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.ui.elements.DatePickerDialogFunction
import com.gruppe11.todoApp.ui.elements.HorizDividerWithSpacer
import com.gruppe11.todoApp.ui.elements.PriorityFC
import com.gruppe11.todoApp.ui.elements.SwitchableButton
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme
import com.gruppe11.todoApp.viewModel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    returnPage: () -> Unit,
    taskId : Int,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tmp = viewModel.getTask(taskId) ?: returnPage()

    if (tmp.equals(null)) {
        returnPage()
        return
    } else {

    }

    var currentTask by remember { mutableStateOf((tmp as Task).copy()) }


    var subtasks = viewModel.getSubtasks(currentTask);

    var taskName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(currentTask.title))
    }
    var subtaskName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var showSubTaskDialog by remember { mutableStateOf(false) }
    var subtaskList by remember { mutableStateOf(listOf<SubTask>()) }
    val subtaskFocusRequester = remember { FocusRequester() }
    var priority by remember { mutableStateOf(currentTask.priority.name) }
    var date by remember {
        mutableStateOf(LocalDateTime.now())
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val addSubtask: () -> Unit = {
        if (subtaskName.text.isNotEmpty()) {
            subtaskList = subtaskList + SubTask(title = subtaskName.text, id = 0, completed = false)
            subtaskName = TextFieldValue("")
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState
        )
    }, modifier = Modifier.fillMaxHeight(), topBar = {
        TopAppBar(
            title = {
                Text(
                    "Edit Task", maxLines = 1
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
                onClick = { returnPage() },
                isFilled = false,
                pickedColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            // Create button
            SwitchableButton(
                text = "Save",
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (taskName.text.isNotEmpty()) {
                            viewModel.updateTask(
                                viewModel.getTask(taskId),
                                subtaskList
                            )

                            scope.launch {
                                snackbarHostState.showSnackbar("Task created")
                            }
                            returnPage()
                        } else {
                            dismissSnackbar(snackbarHostState, scope)
                            scope.launch {
                                snackbarHostState.showSnackbar("Missing Task Name")
                            }
                        }
                    }
                },
                isFilled = true,
                pickedColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }

    }) { padding ->
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Spacer(modifier = Modifier.height(60.dp))
                // Main task Input
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task name") },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                HorizDividerWithSpacer(10.dp)
                Text(
                    text = "Choose priority",
                    fontWeight = FontWeight.Bold,
                    //fontSize = ,
                )
                PriorityFC(
                    selectedPriority = priority,
                    onClick = { newPriority -> priority = newPriority }
                )
                HorizDividerWithSpacer(10.dp)
                Row {
                    Text(text = "Add Subtask")
                    IconButton(
                        onClick = {
                            showSubTaskDialog = true
                            subtaskName = TextFieldValue("")
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.AddCircleOutline,
                                contentDescription = "Cancel",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }

                if (showSubTaskDialog) {
                    OutlinedTextField(
                        value = subtaskName,
                        onValueChange = { subtaskName = it },
                        label = { Text("Subtask name") },
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.focusRequester(subtaskFocusRequester)
                    )
                    LaunchedEffect(Unit) {
                        subtaskFocusRequester.requestFocus()
                    }
                    Row {
                        SwitchableButton(
                            text = "Cancel",
                            onClick = {
                                showSubTaskDialog = false
                            },
                            isFilled = false,
                            pickedColor = MaterialTheme.colorScheme.primary
                        )
                        SwitchableButton(
                            text = "Confirm",
                            onClick = {
                                addSubtask()
                                showSubTaskDialog = false
                            },
                            isFilled = true,
                            pickedColor = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                subtaskList.forEach() { subtask ->
                    Row(
                        Modifier.padding(vertical = 4.dp)
                    ) {
                        IconButton(onClick = {
                            subtaskList = subtaskList.filter { it != subtask }
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.RemoveCircleOutline,
                                contentDescription = "Delete Subtask",
                                modifier = Modifier.scale(1.3f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        OutlinedTextField(
                            value = subtask.title,
                            onValueChange = { subtask.title = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
                HorizDividerWithSpacer(10.dp)
                Text(
                    text = "Select date",
                    fontWeight = FontWeight.Bold,
                    //fontSize = ,
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
                            text = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    IconButton(
                        onClick = { showDatePicker = true },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = "Pick a date",
                                modifier = Modifier.scale(1.3f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
                if (showDatePicker) {
                    DatePickerDialogFunction(
                        System.currentTimeMillis(),
                        onDateSelected = { date = it },
                        onDismiss = { showDatePicker = false }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun EditTaskPaePreview() {
    TODOAPPTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            EditTaskScreen({}, 1)
        }
    }
}