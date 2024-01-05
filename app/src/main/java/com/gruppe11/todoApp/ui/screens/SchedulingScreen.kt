package com.gruppe11.todoApp.ui.screens


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.ui.elements.DateSideScroller
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import com.gruppe11.todoApp.viewModel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
fun SchedulingScreen(
        viewModel: CalendarViewModel,
        taskViewModel: TaskViewModel = hiltViewModel()
) {
    val timeslots: List<IntRange> =  listOf((0 .. 5), (6..11), (12..17), (18..23))// TODO Fetch list from persistent storage
    //var timeSlotHeight by remember { mutableStateOf(0) } // TODO Proper calculation of slot height
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val columnScrollState = rememberLazyListState()
    val taskList = taskViewModel.getTaskListByDate(uiState.value.selectedDay.atStartOfDay())
    val taskHeight = 75.dp
    val taskWidth = 75.dp


    Scaffold(
        topBar = {
            DateSideScroller(
                viewModel = viewModel,
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
            items(timeslots){slot ->
                val slotHeight = (24 / (slot.last - slot.first)).times(50).dp
                TimeSlot(
                    timeSlot = slot,
                    slotHeight = slotHeight
                ) {
                    LazyHorizontalStaggeredGrid(rows = StaggeredGridCells.Fixed(2)) {
                        items(items = taskList // TODO implement timeslot data class
                            .filter { slot.contains(it.deadline.hour) }
                            .sortedBy { it.priority }
                            .reversed()
                        ) { task ->
                            ScheduleTask(
                                task = task,
                                height = taskHeight,
                                width = taskWidth
                                )
                        }
                    }
                }
                HorizontalDivider()
            }
        }
        LaunchedEffect(key1 = LocalDateTime.now().hour) {
            if (uiState.value.selectedDay == LocalDate.now()) {
                scrollToCurrentTime(state = columnScrollState, slots = timeslots)
            }
        }
    }
}

@Composable
fun TimeSlot(
    slotHeight: Dp,
    timeSlot: IntRange, // TODO Change type to match timeslot implementation
    content: @Composable () -> Unit
){
    HorizontalDivider()
    Row(modifier = Modifier
        .height(slotHeight)
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
                text = "Slot",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = timeSlot.first.toString())
            Text(text = "-")
            Text(text = timeSlot.last.toString())
            Spacer(Modifier.height(20.dp))
        }
        content()
    }
}

@Composable
fun ScheduleTask(
    task: Task,
    height: Dp,
    width: Dp
){
    val priorityColor = when (task.priority) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> MaterialTheme.colorScheme.primary
        Priority.LOW -> Color.Green
    }
    ElevatedCard(
        modifier = Modifier
            .defaultMinSize(minWidth = width, minHeight = height)
            .padding(2.dp)
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = priorityColor,
                shape = RoundedCornerShape(10.dp)
            )

    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = task.title, fontSize = 18.sp)
            // TODO add relevant info such as priority etc.
        }

    }
}

suspend fun  scrollToCurrentTime(
    state: LazyListState,
    slots: List<IntRange> // TODO Change type to match timeslot implementation
){
    slots.forEach {
        if (it.contains(LocalDateTime.now().hour)) {
            state.scrollToItem(slots.indexOf(it))

        }
    }
}
