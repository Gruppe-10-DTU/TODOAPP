package com.gruppe11.todoApp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.R
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.ui.elements.DatePickerDialogFunction
import com.gruppe11.todoApp.ui.elements.EditTaskDialog
import com.gruppe11.todoApp.ui.elements.FilterSection
import com.gruppe11.todoApp.ui.elements.LoadingIndicator
import com.gruppe11.todoApp.ui.elements.SearchBar
import com.gruppe11.todoApp.ui.elements.SwitchableButton
import com.gruppe11.todoApp.ui.screenStates.ExecutionState
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme
import com.gruppe11.todoApp.viewModel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ShowTaskList (
    viewModel : TaskViewModel = hiltViewModel<TaskViewModel>(),
    onFloatingButtonClick: () -> Unit,
    onEditTask: (Int) -> Unit) {
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val screenState by viewModel.UIState.collectAsStateWithLifecycle()
    var filterTagsVisible by remember { mutableStateOf(false) }
    var sortingVisible by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val sortingList = listOf("Priority Descending","Priority Ascending", "A-Z", "Z-A")
    val focusManager = LocalFocusManager.current
    val tasks by viewModel.TaskState.collectAsStateWithLifecycle(initialValue = emptyList())
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
                                    viewModel.changeDate(LocalDateTime.now())
                                    listState.scrollToItem(LocalDateTime.now().dayOfMonth.plus(26))
                                }
                            },
                            colors = ButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.background,
                                disabledContainerColor = MaterialTheme.colorScheme.background,
                                disabledContentColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Text(
                                text = screenState.selectedDate.format(formatBigDate).toString(),
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
            Surface(modifier = Modifier.padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .noRippleClickable { focusManager?.clearFocus() },
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
                            listState = listState,
                            selectedDate = screenState.selectedDate,
                            onSelectedDate = viewModel::changeDate
                        )
                    }
                    Scaffold(content = {
                        Surface(
                            modifier = Modifier.padding(
                                top = it.calculateTopPadding(),
                                bottom = it.calculateBottomPadding()
                            )
                        ) {
                            CollapsingLayout(
                                refresh = viewModel::refreshTasks,
                                state = loadingState,
                                collapsingTop = {
                                Box(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    Column {
                                        Row {
                                            SearchBar(screenState)
                                            IconButton(onClick = {
                                                sortingVisible = !sortingVisible
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Filled.SortByAlpha,
                                                    contentDescription = "Open sorting",
                                                    modifier = Modifier
                                                        .size(44.dp)
                                                        .padding(4.dp),
                                                    tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                                if (sortingVisible) {
                                                    DropdownMenu(
                                                        expanded = sortingVisible,
                                                        onDismissRequest = {
                                                            sortingVisible = false
                                                        },
                                                        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                                                    ) {
                                                        sortingList.forEach { optionLabel ->
                                                            DropdownMenuItem(
                                                                text = { Text(text = optionLabel) },
                                                                onClick = {
                                                                    viewModel.selectSortingOption(
                                                                        optionLabel
                                                                    )
                                                                    sortingVisible = false
                                                                },
                                                                trailingIcon = {
                                                                    Icon(
                                                                        imageVector = if (optionLabel == "Priority Descending" || optionLabel == "Z-A") Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                                                                        contentDescription = "Trailing icon",
                                                                        modifier = Modifier
                                                                            .size(30.dp)
                                                                            .padding(4.dp)
                                                                    )
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            IconButton(onClick = {
                                                filterTagsVisible = !filterTagsVisible
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Filled.Tune,
                                                    contentDescription = "Open filter selection",
                                                    modifier = Modifier
                                                        .size(44.dp)
                                                        .padding(4.dp),
                                                    tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }
                                            IconButton(onClick = {
                                                showMonthPicker.value = true
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Filled.DateRange,
                                                    contentDescription = "Open YearMonth Picker",
                                                    modifier = Modifier
                                                        .size(44.dp)
                                                        .padding(4.dp),
                                                    tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }
                                        }
                                        HorizontalDivider(modifier = Modifier.padding(2.dp))
                                        Box(modifier= Modifier
                                            .wrapContentHeight()
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.background),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column {
                                                AnimatedVisibility(
                                                    visible = filterTagsVisible,
                                                    enter = expandVertically(),
                                                    exit = shrinkVertically()
                                                ) {
                                                    FilterSection(
                                                        taskViewModel = viewModel,
                                                        state = screenState
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }, bodyContent = {
                                Box(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    Column {
                                        AnimatedVisibility(
                                            visible = showMonthPicker.value,
                                            enter = fadeIn(),
                                            exit = fadeOut()
                                        ) {
                                            if (showMonthPicker.value) {
                                                ElevatedCard {
                                                    DatePickerDialogFunction(
                                                        taskDateTimeMillis = System.currentTimeMillis(),
                                                        onDateSelected = viewModel::changeDate,
                                                        onDismiss = {
                                                            showMonthPicker.value = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                when (loadingState) {
                                    ExecutionState.ERROR -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.background),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column {
                                                Text(stringResource(id = R.string.error_task_loading))
                                                IconButton(
                                                    onClick = viewModel::loadTaskList,
                                                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Sync,
                                                        contentDescription = "Retry loading tasks",
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    ExecutionState.RUNNING -> {
                                        LoadingIndicator()
                                    }
                                    ExecutionState.SUCCESS -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.background),
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            GenerateLazyColumnForTasks(
                                                viewModel = viewModel,
                                                filteredTasks = tasks,
                                                editTask = onEditTask
                                            )
                                        }
                                    }
                                }
                            })
                        }
                    })
                }
            }
            LaunchedEffect(true) {
                listState.scrollToItem(28)
            }
        },
    )
}

//Yoinked from: https://github.com/droidyayu/CollapsingLayout.git
//Original Author: Ayushi Gupta
@Composable
internal fun CollapsingLayout(
    collapsingTop: @Composable BoxScope.() -> Unit,
    bodyContent: @Composable BoxScope.() -> Unit,
    refresh: () -> Unit,
    state: ExecutionState,
    modifier: Modifier = Modifier,
) {
    var collapsingTopHeight by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(0f) }

    fun calculateOffset(delta: Float): Offset {
        val oldOffset = offset
        val newOffset = (oldOffset + delta).coerceIn(-collapsingTopHeight, 0f)
        offset = newOffset
        return Offset(0f, newOffset - oldOffset)
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset
                 {
                    if (available.y >= 0) {
                        if (available.y > 50 && state == ExecutionState.SUCCESS && offset == 0f) {
                            refresh()
                        }
                        return Offset.Zero
                    }
                    else if(offset == -collapsingTopHeight) {
                        return Offset.Zero
                    }
                    else {
                        return calculateOffset(available.y)
                    }
                }
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                if (available.y <= 0) {
                    return Offset.Zero
                }
                else if(offset == 0f) {
                    return Offset.Zero
                }
                else {
                    return calculateOffset(available.y)
                }
            }

        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
    ) {
        Box(
            modifier = Modifier
                .onSizeChanged { size ->
                    collapsingTopHeight = size.height.toFloat()
                }
                .offset { IntOffset(x = 0, y = offset.roundToInt()) },
            content = collapsingTop,
        )
        Box(
            modifier = Modifier.offset {
                IntOffset(
                    x = 0,
                    y = (collapsingTopHeight + offset).roundToInt()
                )
            },
            content = bodyContent,
        )
    }
}

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

@Composable
fun GenerateLazyRowForDays(
    viewModel: TaskViewModel,
    listState: LazyListState,
    selectedDate: LocalDateTime,
    onSelectedDate: (LocalDateTime) -> Unit,
) {
    val daysMap by viewModel.DaysMap.collectAsStateWithLifecycle(initialValue = emptyMap<LocalDate, Float>())
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
                val formatFilterDate = DateTimeFormatter.ofPattern("E\nd/MM")
                items(daysMap.keys.toList()) { day ->
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
                            selected = selectedDate.toLocalDate().equals(day),
                            onClick = {
                                onSelectedDate(day.atTime(LocalDateTime.now().hour,LocalDateTime.now().minute))
                            },
                            label = {
                                Text(
                                    text = day.format(formatFilterDate),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            enabled = true,
                            modifier = Modifier
                                .width(65.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                labelColor = MaterialTheme.colorScheme.onPrimary,
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.background
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = false,
                                borderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                            )
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenerateLazyColumnForTasks(
    viewModel: TaskViewModel,
    filteredTasks: List<Task>,
    editTask: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp)
    ) {

        if (filteredTasks.isEmpty()) {
            Text(
                text = "No tasks to display",
                modifier = Modifier.align(Alignment.Center)
            )
        }

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



@Composable
fun TaskItem(task: Task, viewModel: TaskViewModel, editTask: (Int) -> Unit){
    val showDialog = remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    val showDeleteConf = remember{mutableStateOf(false)}
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
                colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.tertiary,MaterialTheme.colorScheme.tertiary,MaterialTheme.colorScheme.background)
            )
            Text(
                buildAnnotatedString (
                ) {
                    val nrOfCharactersInSearch : Int = viewModel.UIState.value.searchText.length
                    var startOfText = 0
                    var startOfSearch : Int = task.title.indexOf(viewModel.UIState.value.searchText, ignoreCase = true)
                    var endOfSearchPlus1 : Int = startOfSearch + nrOfCharactersInSearch
                    var moreInstances = true

                    while (moreInstances) {

                        append(task.title.substring(startOfText,startOfSearch))

                        withStyle(style = SpanStyle(
                            background = MaterialTheme.colorScheme.secondary)) {
                            append(task.title.substring(startOfSearch,endOfSearchPlus1))
                        }

                        startOfSearch = task.title.indexOf(viewModel.UIState.value.searchText, endOfSearchPlus1, ignoreCase = true)

                        if (startOfSearch > 0) {
                            startOfText = endOfSearchPlus1
                            endOfSearchPlus1 = startOfSearch + nrOfCharactersInSearch
                        } else {
                            moreInstances = false
                        }

                    }

                    append(task.title.substring(endOfSearchPlus1))
                },
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            )
            Spacer(Modifier.weight(1f))
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                text = task.priority.name.lowercase().replaceFirstChar { x -> x.uppercaseChar()}
            )
            IconButton(modifier = Modifier
                .align(Alignment.CenterVertically),
                onClick = {
                    visible = !visible
                }) {
                if (task.subtasks.isNotEmpty()) {
                    Icon(
                        imageVector = (if (visible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown),
                        contentDescription = "See subtasks",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        AnimatedVisibility(visible) {
            Column(modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
            ) {
                for (subtask in task.subtasks){
                    HorizontalDivider()
                    ShowSubTask(viewModel::changeSubtaskCompletion, task, subtask)
                }
            }
        }
    }

    if (showDialog.value) {
        EditTaskDialog(task = task,
            editTask = editTask,
            deleteTask = {
                showDialog.value = false
                showDeleteConf.value = true
            },
            dismissDialog = { showDialog.value = false }
        )
    }
    if(showDeleteConf.value){
        AlertDialog(
            title = { Text(style = MaterialTheme.typography.bodyMedium,text  = "Are you sure you want to remove this task?") },
            dismissButton = { SwitchableButton(
                text = "Cancel",
                onClick = {
                    showDeleteConf.value = false
                    showDialog.value = true
                          },
                isFilled = false,
                pickedColor = MaterialTheme.colorScheme.primary
            )},
            onDismissRequest = {
                showDeleteConf.value = false
                showDialog.value = true
                               },
            confirmButton = {
                SwitchableButton(
                text = "Confirm",
                onClick = {
                    viewModel.removeTask(task)
                    showDeleteConf.value = false},
                isFilled = true,
                pickedColor = MaterialTheme.colorScheme.tertiary
            )},
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun ShowSubTask(changeSubtaskCompletion: (task: Task, subtask: SubTask) -> Unit,task: Task, subtask : SubTask) {
    Row(modifier = Modifier
        .fillMaxWidth()
    ) {
        Text(modifier = Modifier
            .align(alignment = Alignment.CenterVertically)
            .padding(10.dp),
            text = subtask.title)
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(modifier = Modifier.padding(10.dp),
            checked = subtask.completed,
            onCheckedChange = {
                changeSubtaskCompletion(task, subtask)
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.tertiary,MaterialTheme.colorScheme.tertiary,MaterialTheme.colorScheme.background)
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