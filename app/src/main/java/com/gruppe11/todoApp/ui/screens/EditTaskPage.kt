package com.gruppe11.todoApp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    returnPage: () -> Unit,
    saveTask: () -> Unit,
) {
    var taskName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("Test", TextRange(0, 7)))
    }
    var lowSelected by remember { mutableStateOf(true) }
    var medSelected by remember { mutableStateOf(false) }
    var highSelected by remember { mutableStateOf(false) }

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
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            OutlinedTextField(
                label = { Text(text = "Task Name") },
                value = taskName,
                onValueChange = { taskName = it },
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
                Spacer(modifier = Modifier.width(20.dp))
                FilterChip(
                    selected = lowSelected,
                    onClick = { lowSelected = true
                                medSelected = false
                                highSelected = false
                                },
                    label = { Text("Low") },
                    enabled = true,
                    leadingIcon = {if (lowSelected) Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected"
                    ) }
                )
                //Spacer(modifier = Modifier.width(20.dp))
                FilterChip(
                    selected = medSelected,
                    onClick = { lowSelected = false
                        medSelected = true
                        highSelected = false
                    },
                    label = {Text(text = "Medium")},
                    enabled = true,
                    leadingIcon = {if (medSelected) Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected"
                    ) }
                )
                //Spacer(modifier = Modifier.width(20.dp))
                FilterChip(
                    selected = highSelected,
                    onClick = { lowSelected = false
                        medSelected = false
                        highSelected = true
                    },
                    label = {Text(text = "High")},
                    enabled = true,
                    leadingIcon = {if (highSelected)
                        Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected"
                    ) }
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))




            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(80.dp, 0.dp)
                )
                {
                    Button(
                        enabled = false, // TODO implement button functionality
                        onClick = returnPage,
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        enabled = false, // TODO implement button functionality
                        onClick = saveTask,
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp)
                    ) { Text(text = "SAVE") }
                }
            }
        }
    }
}

@Preview
@Composable
fun EditTaskPaePreview() {
    TODOAPPTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            EditTaskScreen({}, {})
        }
    }
}