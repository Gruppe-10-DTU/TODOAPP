package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gruppe11.todoApp.CreateTask
import com.gruppe11.todoApp.MainDestination
import com.gruppe11.todoApp.TaskDestination
import com.gruppe11.todoApp.ui.elements.EditTaskDialog
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme
import com.gruppe11.todoApp.viewModel.TaskViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateLazyRowForDays(
    viewModel: TaskViewModel,
    selectedDay: Int,
    selectedMonth: Int,
    selectedYear: Int,
    onSelectedDay: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val formatBigDate = DateTimeFormatter.ofPattern("E d. MMMM", Locale.getDefault())
            Text(
                LocalDateTime.now().toLocalDate().format(formatBigDate).toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val formatFilterDate = DateTimeFormatter.ofPattern("E\n d.")
                var lt = LocalDateTime.of(
                    selectedYear,
                    selectedMonth,
                    selectedDay,
                    LocalDateTime.now().hour,
                    LocalDateTime.now().minute
                )
                items(viewModel.generateListOfDaysLeftInMonth(lt)) { day ->
                    FilterChip(
                        shape = MaterialTheme.shapes.small,
                        selected = selectedDay == day,
                        onClick = { onSelectedDay(day) },
                        label = { Text(lt.withDayOfMonth(day).format(formatFilterDate)) },
                        enabled = true,
                    )
                }
            }
        }
    }
}


@SuppressLint("NewApi")
@Composable
fun GenerateLazyColumnForTasks(
    viewModel: TaskViewModel,
    selectedDay: Int,
    selectedMonth: Int,
    selectedYear: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(100.dp)
    ) {
        LazyColumn(modifier = Modifier
            .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(viewModel.getTaskListByDate(LocalDateTime.of(selectedYear,selectedMonth,selectedDay,LocalDateTime.now().hour,LocalDateTime.now().minute))) { Task ->
                TaskItem(task = Task, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, viewModel: TaskViewModel){
    val taskCompletionStatus by remember {
        mutableStateOf(task!!.isCompleted)
    }
    val showDialog = remember { mutableStateOf(false) }

    val longPressHandler = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onLongPress = {
                showDialog.value = true
            }
        )
    }
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .then(longPressHandler)
    ){
        Checkbox(checked = taskCompletionStatus, onCheckedChange ={
            viewModel.changeTaskCompletion(task)
        } )
        Text(
            text = task.toString(),
            )
    }
    if (showDialog.value) {
        task.title?.let {
            EditTaskDialog(taskName = it,
                editTask = { /*TODO*/ },
                deleteTask = { viewModel.removeTask(task) },
                dismissDialog = { showDialog.value = false }
            )
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTaskList(
        viewModel : TaskViewModel = viewModel(),
        onFloatingButtonClick: () -> Unit = {}) {
    val uiState by viewModel.UIState.collectAsStateWithLifecycle()
    //Change this variable when we want to display different months.
    var selectedMonth by remember{mutableStateOf(LocalDateTime.now().monthValue)}
    var selectedDay by remember{ mutableStateOf(LocalDateTime.now().dayOfMonth) }
    var selectedYear by remember{mutableStateOf(LocalDateTime.now().year)}
    /*
    MAKE SURE TO REMOVE CODE BELOW ONCE WE DELIVER. THIS IS ONLY TO TEST
    PREVIEW, TASKS SHOULD NOT BE ADDED LIKE THIS!
    PLEASE ENSURE TO REMOVE THE BIT AFTER THE FOR LOOP AS WELL!
     */
    for(i in 1.. 20) {
        if (i % 2 != 0) {
            viewModel.addTask(i, "Task: $i", LocalDateTime.now(), "HIGH", false)
        } else {
            viewModel.addTask(i, "Task: $i", LocalDateTime.now(), "LOW", false)
        }
    }
    viewModel.addTask(6,"Task: " + "" +  6, LocalDateTime.of(LocalDateTime.now().year,LocalDateTime.now().monthValue,LocalDateTime.now().dayOfMonth.plus(1),LocalDateTime.now().hour,LocalDateTime.now().minute),"LOW",false)
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(72.dp),
                title = {
                            GenerateLazyRowForDays(
                                viewModel = viewModel,
                                selectedDay = selectedDay,
                                selectedMonth = selectedMonth,
                                selectedYear = selectedYear,
                            ) { day ->
                                selectedDay = day
                            }
                        },

            )
        },
        content = {
            GenerateLazyColumnForTasks(
                viewModel = viewModel,
                selectedDay = selectedDay,
                selectedMonth = selectedMonth,
                selectedYear = selectedYear,
            )
            uiState
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = onFloatingButtonClick) {
                Icon(Icons.Filled.Add, "Add new Task")
            }
        }
    )
}


@Preview
@Suppress("NewApi")
@Composable
fun ShowTaskListPreview() {
    TODOAPPTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            ShowTaskList()
        }
    }
}