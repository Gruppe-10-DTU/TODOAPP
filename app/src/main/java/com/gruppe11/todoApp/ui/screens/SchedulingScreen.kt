package com.gruppe11.todoApp.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.R
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.ui.elements.DateSideScroller
import com.gruppe11.todoApp.viewModel.ScheduleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SchedulingScreen(
    viewModel: ScheduleViewModel = hiltViewModel(),
) {
    val timeslots by viewModel.timeSlots.collectAsStateWithLifecycle(initialValue = emptyList())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val columnScrollState = rememberLazyListState()
    val minSlotHeight = 200.dp
    val minTaskHeight = 60.dp
    val minTaskWidth = 75.dp


    Scaffold(
        topBar = {
            DateSideScroller(
                currentDate = uiState.currentDay,
                selectedDay = uiState.selectedDay,
                onDateChange = { viewModel.changeSelectedDay(it) },
                dates = viewModel.dates.collectAsStateWithLifecycle(initialValue = emptyList()),
                onTitleClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        scrollToCurrentTime(state = columnScrollState, slots = timeslots)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            state = columnScrollState
        ) {
            if (timeslots.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.no_timeslots),
                            color = Color.LightGray,
                            fontStyle = FontStyle.Italic,
                            fontSize = 18.sp)
                    }
                }
            }
            items(timeslots.sortedBy { it.start }, key = { it.id }) { slot ->
                TimeSlot(
                    timeSlot = slot,
                    slotHeight = minSlotHeight
                ) {
                    FlowRow(maxItemsInEachRow = 5) {
                        slot.tasks
                            .filter { task -> task.deadline.toLocalDate() == uiState.selectedDay }
                            .sortedBy { it.priority }//.sortedBy { !it.isCompleted }
                            .reversed()
                            .forEach { task ->
                                ScheduleTask(
                                    task = task,
                                    height = minTaskHeight,
                                    width = minTaskWidth,
                                    toggleCompletion = {viewModel.toggleTaskCompletion(it)}
                                )
                            }
                    }
                }
                HorizontalDivider()
            }
        }
    }
    LaunchedEffect(key1 = LocalDateTime.now().hour) {
        if (uiState.selectedDay == LocalDate.now()) {
            scrollToCurrentTime(state = columnScrollState, slots = timeslots)
        }
    }
}

@Composable
fun TimeSlot (
    slotHeight: Dp,
    timeSlot: TimeSlot,
    content: @Composable () -> Unit
){
    HorizontalDivider()
    Row(modifier = Modifier
        .defaultMinSize(minHeight = slotHeight)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(5.dp, 5.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(5.dp))
            Text(
                text = timeSlot.name,
                fontSize = 18.sp,
                softWrap = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = timeSlot.start.toString())
            Text(text = "-")
            Text(text = timeSlot.end.toString())
            Spacer(Modifier.height(20.dp))
        }
        Column(Modifier.weight(3f)) {
            content()
        }
    }
}

@Composable
fun ScheduleTask(
    task: Task,
    height: Dp,
    width: Dp,
    toggleCompletion: (Task) -> Unit
){
    val priorityColor = when (task.priority) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> MaterialTheme.colorScheme.primary
        Priority.LOW -> Color.Green
    }
    val completionColor = when (task.isCompleted) {
        false -> MaterialTheme.colorScheme.tertiaryContainer
        true -> MaterialTheme.colorScheme.primaryContainer
    }
    ElevatedCard(
        modifier = Modifier
            .defaultMinSize(minWidth = width, minHeight = height)
            .padding(2.dp)
            .clickable { toggleCompletion(task.copy(isCompleted = !task.isCompleted)) },
//            .border(
//                width = 1.dp,
//                color = priorityColor,
//                shape = RoundedCornerShape(10.dp)
//            ),
        colors = CardColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = completionColor,
            disabledContainerColor = Color.Black,
            disabledContentColor = Color.Black)

    ) {
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Checkbox(
                modifier = Modifier.size(35.dp),
                checked = task.isCompleted,
                onCheckedChange = { toggleCompletion(task.copy(isCompleted = !task.isCompleted)) }
            )
            Column(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 2.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = task.title.uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 19.sp,
                    )
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 1.dp)) {
                    task.subtasks.forEach {
                        Text(text = it.title,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

        }
    }
}

suspend fun  scrollToCurrentTime(
    state: LazyListState,
    slots: List<TimeSlot>
){
    slots.forEach {
        if (LocalTime.now().isAfter(it.start)) {
            state.scrollToItem(slots.indexOf(it) + 1)
        }
    }
}
