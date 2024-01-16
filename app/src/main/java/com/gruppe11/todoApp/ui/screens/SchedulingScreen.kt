package com.gruppe11.todoApp.ui.screens


import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    val timeslots = viewModel.timeSlots.collectAsStateWithLifecycle(initialValue = emptyList())
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val columnScrollState = rememberLazyListState()
    val minSlotHeight = 200.dp
    val minTaskHeight = 75.dp
    val minTaskWidth = 75.dp


    Scaffold(
        topBar = {
            DateSideScroller(
                currentDate = uiState.value.currentDay,
                onDateChange = { viewModel.changeSelectedDay(it) },
                dates = viewModel.dates.collectAsStateWithLifecycle(initialValue = emptyList())
            ) {
                CoroutineScope(Dispatchers.Main).launch {
                    scrollToCurrentTime(state = columnScrollState, slots = timeslots.value)
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            state = columnScrollState
        ) {
            if (timeslots.value.isEmpty()) {
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
            items(timeslots.value.sortedBy { it.start }) { slot ->
                TimeSlot(
                    timeSlot = slot,
                    slotHeight = minSlotHeight
                ) {
                    FlowRow(maxItemsInEachRow = 5) {
                        slot.tasks
                            .filter { task -> task.deadline.toLocalDate() == uiState.value.selectedDay }
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
        if (uiState.value.selectedDay == LocalDate.now()) {
            scrollToCurrentTime(state = columnScrollState, slots = timeslots.value)
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
                .defaultMinSize(minHeight = slotHeight, minWidth = 50.dp)
                .padding(5.dp, 10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(5.dp))
            Text(
                text = timeSlot.name,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = timeSlot.start.toString())
            Text(text = "-")
            Text(text = timeSlot.end.toString())
            Spacer(Modifier.height(20.dp))
        }
        content()
    }
}

@Composable
fun ScheduleTask(
    task: Task,
    height: Dp,
    width: Dp,
    toggleCompletion: (Task) -> Unit
){
    //TODO Change colors to match ColorScheme
    val priorityColor = when (task.priority) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> MaterialTheme.colorScheme.primary
        Priority.LOW -> Color.Green
    }
    //TODO Change colors to match ColorScheme
    val completionColor = when (task.isCompleted) {
        false -> MaterialTheme.colorScheme.tertiaryContainer
        true -> MaterialTheme.colorScheme.primaryContainer
    }
    ElevatedCard(
        modifier = Modifier
            .defaultMinSize(minWidth = width, minHeight = height)
            .padding(2.dp)
            .border(
                width = 1.dp,
                color = priorityColor,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = completionColor,
            disabledContainerColor = Color.Black,
            disabledContentColor = Color.Black)

    ) {
        Row() {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = task.title.uppercase(),color = MaterialTheme.colorScheme.onPrimary,fontSize = 19.sp)
                Column(modifier = Modifier.padding(10.dp, 1.dp)) {
                    task.subtasks.forEach {
                        Text(text = it.title,color = MaterialTheme.colorScheme.onPrimary,fontSize = 14.sp)
                    }
                // TODO add more relevant info such as priority etc.
                }

            }
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { toggleCompletion(task.copy(isCompleted = !task.isCompleted)) }
            )
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
