package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.ui.elements.EditTaskDialog
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme
import com.gruppe11.todoApp.viewModel.TaskViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi", "CoroutineCreationDuringComposition", "RememberReturnType",
)
@Composable
fun LinearDeterminateIndicator(viewModel: TaskViewModel, date: LocalDateTime) {
    //TODO: Refractor progressbars, make them update automatically.
    val taskMap  = remember{viewModel.generateMapOfDays(date)}
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(60.dp)
            .padding(5.dp)
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .height(10.dp)
                .width(50.dp)
                .rotate(-90f),
            progress = taskMap[date]!!,
            trackColor = MaterialTheme.colorScheme.primaryContainer,
        )
    }
}




@SuppressLint("NewApi", "CoroutineCreationDuringComposition", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateLazyRowForDays(
    viewModel: TaskViewModel,
    selectedDate: LocalDateTime,
    onSelectedDate: (LocalDateTime) -> Unit,
    ) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
        ) {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondary),
                    state = listState,
                ) {
                    val formatFilterDate = DateTimeFormatter.ofPattern("E\n d.")
                    items(viewModel.generateMapOfDays(date = selectedDate).toList()) { day ->
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.wrapContentSize(),
                            horizontalAlignment = Alignment.Start
                        ) {
                                LinearDeterminateIndicator(
                                    viewModel = viewModel,
                                    date = day.first,
                                    )
                            Spacer(Modifier.height(2.dp))
                                FilterChip(
                                    shape = MaterialTheme.shapes.small,
                                    selected = selectedDate.dayOfYear == day.first.dayOfYear,
                                    onClick = {
                                        onSelectedDate(day.first)
                                              },
                                    label = {
                                        Text(
                                            text = day.first.format(formatFilterDate),
                                            textAlign = TextAlign.Center,
                                        )
                                    },
                                    enabled = true,
                                    modifier = Modifier
                                        .width(65.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = MaterialTheme.colorScheme.background,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.background
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = Color.Transparent,
                                        disabledBorderColor = Color.Transparent,
                                    )
                                )
                            }
                        }
                    }

                coroutineScope.launch{listState.scrollToItem(index = selectedDate.dayOfMonth.plus(27))}
            }
        }
}


@SuppressLint("NewApi")
@Composable
fun GenerateLazyColumnForTasks(
    viewModel: TaskViewModel,
    selectedDate: LocalDateTime,
    editTask: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp)
    ) {
        LazyColumn(modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(viewModel.getTaskListByDate(selectedDate)) { Task ->
                TaskItem(task = Task, viewModel = viewModel, editTask)
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TaskItem(task: Task, viewModel: TaskViewModel, editTask: (Int) -> Unit){
    var taskCompletionStatus by mutableStateOf(task.isCompleted)
    val showDialog = remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    val longPressHandler = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onLongPress = {
                showDialog.value = true
            }
        )
    }
    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .animateContentSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .then(longPressHandler)
            .fillMaxWidth()
            .clipToBounds()

    ){
        Row(modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(1.dp)
            .fillMaxWidth()
            .clipToBounds()) {
            Checkbox(modifier = Modifier.padding(10.dp),
                checked = taskCompletionStatus,
                onCheckedChange ={
                    viewModel.changeTaskCompletion(task)
                    taskCompletionStatus = task.isCompleted
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.tertiary,MaterialTheme.colorScheme.tertiary)
            )
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                text = task.title
            )
            Spacer(Modifier.weight(1f))
            IconButton(modifier = Modifier
                .align(Alignment.CenterVertically),
                onClick = {
                    visible = !visible
                }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "See subtasks",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        AnimatedVisibility(visible) {
            Column(modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
            ) {
                for (subtask in viewModel.getStaticSubtasks()){
                    HorizontalDivider()
                    showSubTask(subtask)
                }
            }
        }
    }
    if (showDialog.value) {
        EditTaskDialog(task = task,
            editTask = editTask,
            deleteTask = {
                showDialog.value = false
                viewModel.removeTask(task)
             },
            dismissDialog = { showDialog.value = false }
        )
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTaskList (
    viewModel : TaskViewModel = hiltViewModel(),
    onFloatingButtonClick: () -> Unit,
    onEditTask: (Int) -> Unit) {
    val uiState by viewModel.UIState.collectAsStateWithLifecycle()
    //Change this variable when we want to display different months.
    var selectedMonth by remember{mutableStateOf(LocalDateTime.now().monthValue)}
    var selectedDay by remember{ mutableStateOf(LocalDateTime.now().dayOfMonth) }
    var selectedYear by remember{mutableStateOf(LocalDateTime.now().year)}
    var selectedDate by remember{mutableStateOf(LocalDateTime.of(selectedYear,selectedMonth,selectedDay,LocalDateTime.now().hour,LocalDateTime.now().minute))}/*
    MAKE SURE TO REMOVE CODE BELOW ONCE WE DELIVER. THIS IS ONLY TO TEST
    PREVIEW, TASKS SHOULD NOT BE ADDED LIKE THIS!
    PLEASE ENSURE TO REMOVE THE BIT AFTER THE FOR LOOP AS WELL!
//     */
//    for(i in 1.. 20) {
//        if (i % 2 != 0) {
//            viewModel.addTask(i, "Task: $i", LocalDateTime.now(), "HIGH", false)
//        } else {
//            viewModel.addTask(i, "Task: $i", LocalDateTime.now(), "LOW", false)
//        }
//    }
//    viewModel.addTask(6,"Task: " + "" +  6, LocalDateTime.of(LocalDateTime.now().year,LocalDateTime.now().monthValue,LocalDateTime.now().dayOfMonth.plus(1),LocalDateTime.now().hour,LocalDateTime.now().minute),"LOW",false)
//    viewModel.addTask(viewModel.getTaskList().size+1,"Task: " + "" +  viewModel.getTaskList().size+1, LocalDateTime.of(LocalDateTime.now().year,LocalDateTime.now().monthValue,LocalDateTime.now().dayOfMonth.minus(1),LocalDateTime.now().hour,LocalDateTime.now().minute),"LOW",false)
//    viewModel.addTask(viewModel.getTaskList().size+1,"Task: " + "" +  viewModel.getTaskList().size+1, LocalDateTime.of(LocalDateTime.now().year,LocalDateTime.now().monthValue,LocalDateTime.now().dayOfMonth.minus(2),LocalDateTime.now().hour,LocalDateTime.now().minute),"LOW",false)
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(40.dp),
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                title = {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                        ) {
                        val formatBigDate =
                            DateTimeFormatter.ofPattern("E d. MMMM", Locale.getDefault())
                        Text(
                            selectedDate.toLocalDate().format(formatBigDate).toString(),
                        )
                    }
                        },


            )
        },floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.tertiary,
                onClick = onFloatingButtonClick) {
                Icon(Icons.Filled.Add, "Add new Task")
            }
        },
        content = {
            Surface(modifier = Modifier.padding(top = it.calculateTopPadding(),bottom=it.calculateBottomPadding())) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        GenerateLazyRowForDays(
                            viewModel = viewModel,
                            selectedDate = selectedDate,
                        ) { date ->
                            selectedDate = date
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        GenerateLazyColumnForTasks(
                            viewModel = viewModel,
                            selectedDate = selectedDate,
                            editTask = onEditTask
                        )
                    }
                }
            }
        },
    )
}
@SuppressLint("UnrememberedMutableState")
@Composable
fun showSubTask(subtask : SubTask) {
    var checked by mutableStateOf(subtask.completed)
    Row(modifier = Modifier
        .fillMaxWidth()
    ) {
        Text(modifier = Modifier
            .align(alignment = Alignment.CenterVertically)
            .padding(10.dp),
            text = subtask.title)
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(modifier = Modifier.padding(10.dp),
            checked = checked,
            onCheckedChange = {
                subtask.completed = !subtask.completed
                checked = subtask.completed
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.tertiary,MaterialTheme.colorScheme.tertiary)
        )
    }
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
            ShowTaskList(
                onEditTask = { println("editing") },
                onFloatingButtonClick = { println("Test") }
            )
        }
    }
}