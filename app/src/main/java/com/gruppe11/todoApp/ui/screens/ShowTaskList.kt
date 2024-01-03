package com.gruppe11.todoApp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.ui.elements.DatePickerDialogFunction
import com.gruppe11.todoApp.ui.elements.EditTaskDialog
import com.gruppe11.todoApp.ui.elements.FilterSection
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme
import com.gruppe11.todoApp.viewModel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LinearDeterminateIndicator(progress: Float) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(60.dp)
            .padding(7.5.dp)
    ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .wrapContentSize()
                    .height(10.dp)
                    .width(50.dp)
                    .rotate(-90f),
                progress = {progress},
                trackColor = MaterialTheme.colorScheme.primaryContainer,
            )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateLazyRowForDays(
    viewModel: TaskViewModel,
    listState: LazyListState,
    selectedDate: LocalDateTime,
    onSelectedDate: (LocalDateTime) -> Unit,
    ) {
    val daysMap by viewModel.DaysMap.collectAsStateWithLifecycle()
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
                    items(viewModel.DaysMap.value.keys.toList()) { day ->
                            Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.wrapContentSize(),
                            horizontalAlignment = Alignment.Start
                        ) {
                                daysMap[day]?.let {
                                    LinearDeterminateIndicator(
                                        progress = it
                                    )
                                }
                            Spacer(Modifier.height(2.dp))
                                FilterChip(
                                    shape = MaterialTheme.shapes.small,
                                    selected = selectedDate.toLocalDate() == day,
                                    onClick = {
                                        onSelectedDate(day.atTime(LocalDateTime.now().hour,LocalDateTime.now().minute))
                                              },
                                    label = {
                                        Text(
                                            text = day.format(formatFilterDate),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .padding(0.dp)
                                                .fillMaxWidth()
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
                                        enabled = true,
                                        selected = true
                                    )
                                )
                            }
                        }
                    }
            }
        }
}


@Composable
fun GenerateLazyColumnForTasks(
    viewModel: TaskViewModel,
    selectedDate: LocalDateTime,
    editTask: (Int) -> Unit
) {
    val filteredTasks =
        viewModel.TaskState.collectAsStateWithLifecycle().value
            .filter {
                it.deadline.toLocalDate() == selectedDate.toLocalDate() && filterTaskItem(it, viewModel)
            }
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
            items(filteredTasks) { task ->
                key(task.id) {
                    TaskItem(task = task, viewModel = viewModel, editTask)
                }
            }
        }
    }
}

fun filterTaskItem(task: Task, taskViewModel: TaskViewModel) : Boolean {
    return ((taskViewModel.completeFilter.value && task.isCompleted) ||
            (taskViewModel.incompleteFilter.value && !task.isCompleted) ||
            (!taskViewModel.completeFilter.value && !taskViewModel.incompleteFilter.value))
}

@Composable
fun TaskItem(task: Task, viewModel: TaskViewModel, editTask: (Int) -> Unit){
    val taskCompletionStatus by viewModel.TaskState.collectAsStateWithLifecycle()
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
                checked = task.isCompleted,
                onCheckedChange ={
                    viewModel.changeTaskCompletion(task)
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
                for (subtask in viewModel.getSubtasks(task)){
                    HorizontalDivider()
                    ShowSubTask(viewModel, task, subtask)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTaskList (
    viewModel : TaskViewModel = hiltViewModel(),
    onFloatingButtonClick: () -> Unit,
    onEditTask: (Int) -> Unit) {
    //Change this variable when we want to display different months.
    var selectedMonth by remember{mutableIntStateOf(LocalDateTime.now().monthValue)}
    var selectedDay by remember{ mutableIntStateOf(LocalDateTime.now().dayOfMonth) }
    var selectedYear by remember{mutableIntStateOf(LocalDateTime.now().year)}
    var selectedDate by remember{mutableStateOf(LocalDateTime.of(selectedYear,selectedMonth,selectedDay,LocalDateTime.now().hour,LocalDateTime.now().minute))}
    var filterTagsVisible by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val showMonthPicker = remember{ mutableStateOf(false) }
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
                        TextButton(
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    listState.scrollToItem(LocalDateTime.now().dayOfMonth.plus(26))
                                    selectedDate = LocalDateTime.now()
                                }
                            },
                            colors = ButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground,
                                containerColor = MaterialTheme.colorScheme.background,
                                disabledContainerColor = MaterialTheme.colorScheme.background,
                                disabledContentColor = MaterialTheme.colorScheme.tertiary)
                            ) {
                            Text(
                                text = LocalDateTime.now().format(formatBigDate).toString(),
                                fontSize = 18.sp
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
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
//                    SwitchableButton(text = "test subtask adder",
//                        onClick = {
//                            if(viewModel.getTaskList().isNotEmpty()) {
//                                val task = viewModel.getTask(1)
//                                viewModel.addSubtasks(
//                                    task,
//                                    listOf(SubTask("TEST TEST", viewModel.getSubtasks(task).size+1, false))
//                                )
//                            }
//                                  },
//                        isFilled = true,
//                        pickedColor = MaterialTheme.colorScheme.tertiary)
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        GenerateLazyRowForDays(
                            viewModel = viewModel,
                            listState = listState,
                            selectedDate = selectedDate,
                        ) { date ->
                            selectedDate = date
                        }
                    }
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Column {
                            Row{
                                IconButton(onClick = { filterTagsVisible = !filterTagsVisible }) {
                                    Icon(
                                        imageVector = Icons.Filled.Tune,
                                        contentDescription = "Open filter selection",
                                        modifier = Modifier
                                            .size(44.dp)
                                            .padding(4.dp)
                                    )
                                }
                                IconButton(onClick = { showMonthPicker.value = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.DateRange,
                                        contentDescription = "Open YearMonth Picker",
                                        modifier = Modifier
                                            .size(44.dp)
                                            .padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                    Box (
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.TopCenter
                    ) {
                        Column {
                            AnimatedVisibility(
                                visible = filterTagsVisible,
                                enter = slideInVertically(),
                                exit = slideOutVertically()
                            ) {
                                FilterSection(taskViewModel = viewModel)
                            }
                            AnimatedVisibility(
                                visible = showMonthPicker.value,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                if(showMonthPicker.value) {
                                    ElevatedCard {
                                        DatePickerDialogFunction(
                                            taskDateTimeMillis = System.currentTimeMillis(),
                                            onDateSelected = {
                                                selectedDate = it
                                                             },
                                            onDismiss = { showMonthPicker.value = false }
                                        )
                                    }
                                }
                            }
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
            LaunchedEffect(true) {
                listState.scrollToItem(28)
            }
        },
    )
}

@Composable
fun ShowSubTask(viewModel: TaskViewModel,task: Task, subtask : SubTask) {
    var checked by remember { mutableStateOf(subtask.completed) }
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
                //checked = !checked
                viewModel.changeSubtaskCompletion(task, subtask)
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.tertiary,MaterialTheme.colorScheme.tertiary)
        )
    }
}
@Preview
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