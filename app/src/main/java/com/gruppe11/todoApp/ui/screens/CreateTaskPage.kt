package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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

class CreateTaskPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOAPPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    CreateTaskContent({}, {})
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskContent(
    returnPage: () -> Unit, saveTask: () -> Unit, viewModel: TaskViewModel = hiltViewModel()
) {
    var taskName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var priority by remember { mutableStateOf("MEDIUM") }
    var date by remember {
        mutableStateOf(LocalDateTime.now())
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(60.dp, 15.dp)
        ) {
            // Cancel Button
            SwitchableButton(
                text = "Cancel",
                onClick = { returnPage() },
                isFilled = false,
                pickedColor = MaterialTheme.colorScheme.primary
            )

            // Create button
            SwitchableButton(
                text = "Create",
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (taskName.text.isNotEmpty()) {
                            viewModel.addTask(
                                0, taskName.text, date, priority, false
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
                pickedColor = MaterialTheme.colorScheme.tertiary
            )
        }

    }) { padding ->
        Column(modifier = Modifier.padding(16.dp)) {
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
            HorizDividerWithSpacer()
            Text(
                text = "Choose priority",
                fontWeight = FontWeight.Bold,
                //fontSize = ,
            )
            PriorityFC(
                selectedPriority = priority,
                onClick = { newPriority -> priority = newPriority }
            )
            HorizDividerWithSpacer()
            Text(
                text = "Select date",
                fontWeight = FontWeight.Bold,
                //fontSize = ,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp, 10.dp)
            ) {
                SwitchableButton(text = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    onClick = { showDatePicker = true },
                    isFilled = false,
                    pickedColor = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(10.dp))
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

@Preview(showBackground = true)
@Composable
fun CreateTaskPreview() {
    TODOAPPTheme {
        CreateTaskContent({}, {})
    }
}

fun dismissSnackbar(snackbarHostState: SnackbarHostState, scope: CoroutineScope) {
    if (snackbarHostState.currentSnackbarData != null) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}





