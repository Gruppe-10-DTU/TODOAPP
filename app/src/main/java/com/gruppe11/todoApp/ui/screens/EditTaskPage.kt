package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme
import com.gruppe11.todoApp.viewModel.TaskViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    returnPage: () -> Unit,
    saveTask: () -> Unit,
    taskId : Int,
    viewModel: TaskViewModel = hiltViewModel()
) {
    var currentTask = viewModel.getTask(taskId)

    currentTask.priority = currentTask.priority ?: Priority.LOW
    var subtasks = viewModel.getSubtasks(currentTask);


    Scaffold(
        modifier = Modifier.fillMaxHeight(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Task",
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick =  returnPage ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
            )
        },
        bottomBar = {
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(60.dp, 15.dp)
            )
            {
                Button(
                    enabled = true,
                    onClick = returnPage,
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                //Spacer(modifier = Modifier.width(30.dp))
                Button(
                    enabled = false, // TODO implement button functionality
                    onClick = saveTask,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp)
                ) { Text(
                    text = "Save",
                    color = MaterialTheme.colorScheme.primary
                    )
                }
            }

        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(10.dp),

        ) {
            var title by remember{ mutableStateOf(currentTask.title) }
            Spacer(modifier = Modifier.height(60.dp))
            OutlinedTextField(
                label = { Text("Title") },
                value = title,
                onValueChange = { title = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Choose priority",
                fontWeight = FontWeight.Bold,
                //fontSize = ,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                var prio by remember{ mutableStateOf(currentTask.priority) }

                Spacer(modifier = Modifier.width(20.dp))
                for (priority : Priority in Priority.entries) {
                    FilterChip(
                        selected = prio  == priority,
                        onClick = { prio = priority },
                        label = { priority.name },
                        enabled = true,
                        leadingIcon = { Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected"
                        ) }
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add Subtask",
                    fontWeight = FontWeight.Bold,
                    //fontSize = ,
                )
                IconButton(
                    onClick = { println("NOT IMPLEMENTED") },
                    content = {Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = "Add subtask",
                        modifier = Modifier
                            .scale(0.7f)
                            .rotate(45f)
                    )}
                )
            }
            Column(
                modifier = Modifier.padding(10.dp, 10.dp)
            ) {
                for (subtask in subtasks){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.removeSubtask(currentTask, subtask)
                                subtasks.remove(subtask)
                            },
                            content = {Icon(
                                imageVector = Icons.Outlined.Cancel,
                                contentDescription = "Remove Subtask",
                                modifier = Modifier.scale(0.7f)
                            )}
                        )
                        Text(
                            text = "Subtask Name",
                            //fontSize = ,
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Select date",
                fontWeight = FontWeight.Bold,
                //fontSize = ,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp, 10.dp)
            ) {

                OutlinedTextField(
                    //label = { Text(text = "Date") },
                    value = currentTask.completion.toString(),
                    onValueChange = { println(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = { /*TODO*/ },
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Pick a date",
                            modifier = Modifier.scale(1.3f)
                        )
                    }
                )
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
            EditTaskScreen({}, {}, 1)
        }
    }
}