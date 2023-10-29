package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.ui.elements.EditTaskDialog
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme
import com.gruppe11.todoApp.viewModel.TaskViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi", "CoroutineCreationDuringComposition")
@Composable
fun LinearDeterminateIndicator(viewModel: TaskViewModel, date: LocalDateTime, progress: MutableState<Float>) {
    var currentProgress by remember {progress}
    //TODO: Refractor progressbars, make them update automatically.
    val cirscope = rememberCoroutineScope()

    cirscope.launch {
        val completionCounts = viewModel.countTaskCompletionsByDay(date)
        currentProgress = completionCounts[date.dayOfMonth].toFloat()
    }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(40.dp)
            .width(55.dp)
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .height(5.dp)
                .width(50.dp)
                .rotate(-90f),
            progress = currentProgress,
            trackColor = MaterialTheme.colorScheme.primaryContainer,
            )
    }
}




@SuppressLint("NewApi", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateLazyRowForDays(
    viewModel: TaskViewModel,
    selectedDay: Int,
    selectedMonth: Int,
    selectedYear: Int,
    progress: MutableState<Float>,
    onSelectedDay: (Int) -> Unit,
    ) {
    val listState = rememberLazyListState()
    //val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .wrapContentSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
        ) {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = listState,
                ) {
                    val formatFilterDate = DateTimeFormatter.ofPattern("E\n d.")
                    var lt = LocalDateTime.of(
                        selectedYear,
                        selectedMonth,
                        selectedDay,
                        LocalDateTime.now().hour,
                        LocalDateTime.now().minute
                    )
                    items(viewModel.generateListOfDaysInMonth(lt)) { day ->
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.wrapContentSize(),
                            horizontalAlignment = Alignment.Start
                        ) {
                                LinearDeterminateIndicator(
                                    viewModel = viewModel,
                                    date = LocalDateTime.of(
                                        lt.year,
                                        lt.month,
                                        day,
                                        lt.hour,
                                        lt.minute
                                    ),
                                    progress
                                )
                            Spacer(Modifier.height(2.dp))
                                FilterChip(
                                    shape = MaterialTheme.shapes.small,
                                    selected = selectedDay == day,
                                    onClick = { onSelectedDay(day) },
                                    label = {
                                        Text(
                                            text = lt.withDayOfMonth(day).format(formatFilterDate),
                                            textAlign = TextAlign.Center,
                                        )
                                    },
                                    enabled = true,
                                    modifier = Modifier
                                        .background(Color.White)
                                        .width(65.dp),
                                )
                            }
                        }
                    }

                //coroutineScope.launch{listState.scrollToItem(index = selectedDay-3)}
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
    ) {
        LazyColumn(modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(viewModel.getTaskListByDate(LocalDateTime.of(selectedYear,selectedMonth,selectedDay,LocalDateTime.now().hour,LocalDateTime.now().minute))) { Task ->
                TaskItem(task = Task, viewModel = viewModel)
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TaskItem(task: Task, viewModel: TaskViewModel){
    var taskCompletionStatus by mutableStateOf(task.isCompleted)
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
            taskCompletionStatus = !taskCompletionStatus

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
    val copyProgress: MutableState<Float> = remember { mutableStateOf(0.0f) }
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
    viewModel.addTask(viewModel.getTaskList().size+1,"Task: " + "" +  viewModel.getTaskList().size+1, LocalDateTime.of(LocalDateTime.now().year,LocalDateTime.now().monthValue,LocalDateTime.now().dayOfMonth.minus(1),LocalDateTime.now().hour,LocalDateTime.now().minute),"LOW",false)
    viewModel.addTask(viewModel.getTaskList().size+1,"Task: " + "" +  viewModel.getTaskList().size+1, LocalDateTime.of(LocalDateTime.now().year,LocalDateTime.now().monthValue,LocalDateTime.now().dayOfMonth.minus(2),LocalDateTime.now().hour,LocalDateTime.now().minute),"LOW",false)
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(72.dp),
                title = {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                        ) {
                        val formatBigDate =
                            DateTimeFormatter.ofPattern("E d. MMMM", Locale.getDefault())
                        Text(
                            LocalDateTime.now().toLocalDate().format(formatBigDate).toString(),
                        )
                    }
                        },


            )
        },bottomBar = {
            BottomAppBar {
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
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        GenerateLazyRowForDays(
                            viewModel = viewModel,
                            selectedDay = selectedDay,
                            selectedMonth = selectedMonth,
                            selectedYear = selectedYear,
                            progress = copyProgress
                        ) { day ->
                            selectedDay = day
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        GenerateLazyColumnForTasks(
                            viewModel = viewModel,
                            selectedDay = selectedDay,
                            selectedMonth = selectedMonth,
                            selectedYear = selectedYear,
                        )
                    }
                }
            }
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