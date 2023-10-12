package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme
import com.gruppe11.todoApp.viewModel.TaskViewModel
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTaskList(viewModel : TaskViewModel = viewModel()) {
    val uiState by viewModel.UIState.collectAsStateWithLifecycle()
    /*
    MAKE SURE TO REMOVE CODE BELOW ONCE WE DELIVER. THIS IS ONLY TO TEST
    PREVIEW, CALLS TO TASK SHOULD NOT BE MADE DIRECTLY FROM MODEL!
     */
    for(i in 1.. 5){
       var lt : LocalDateTime = LocalDateTime.now()
        if(i%2 != 0)
      viewModel.addTask(i,"Task: $i", LocalDateTime.now(),"HIGH",false);
        else
            viewModel.addTask(i,"Task: $i", LocalDateTime.now(),"LOW",false);
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                            .fillMaxSize()
                        ){
                            Button(
                                shape = MaterialTheme.shapes.medium,
                                enabled = false, // TODO implement button functionality
                                onClick = { print("Do Nothing")},
                                colors = ButtonDefaults.buttonColors(
                                    MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(80.dp)
                            ) {
                                Text(
                                    text = "DAY1",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Button(
                                shape = MaterialTheme.shapes.medium,
                                enabled = false, // TODO implement button functionality
                                onClick = { print("Do Nothing") },
                                colors = ButtonDefaults.buttonColors(
                                    MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(80.dp)
                            ) {
                                Text(
                                    text = "DAY2",
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        },
            )
        },bottomBar = {
            BottomAppBar {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Button(
                        enabled = false, // TODO implement button functionality
                        onClick = { print("Eh") },
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        Text(
                            text = "Calender",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    //Spacer(modifier = Modifier.width(30.dp))
                    Button(
                        enabled = false, // TODO implement button functionality
                        onClick = { print("Do Nothing") },
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        Text(
                            text = "Task",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        enabled = false, // TODO implement button functionality
                        onClick = { print("Do Nothing") },
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        Text(
                            text = "Label",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                LazyColumn(modifier = Modifier
                    .align(Alignment.Center)
                ) {
                    items(viewModel.getTaskList()) { Task ->
                        Text(text = "" + Task.title.toString() + " " + Task.priority.toString(),
                            modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer))
                    }
                }
            }
        },
    )
}


@Preview
@Composable
fun ShowTaskListPreview() {
    TODOAPPTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ShowTaskList()
            }
        }
    }
}