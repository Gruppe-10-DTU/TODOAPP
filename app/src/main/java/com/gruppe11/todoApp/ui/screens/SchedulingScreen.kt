package com.gruppe11.todoApp.ui.screens


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.ui.elements.DateSideScroller
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import com.gruppe11.todoApp.viewModel.TaskViewModel


@Composable
fun SchedulingScreen(
        viewModel: CalendarViewModel,
        taskViewModel: TaskViewModel = hiltViewModel()
) {
    val timeslots: List<IntRange> =  listOf((0 .. 5), (6..11), (12..17), (18..23))// TODO Fetch list from persistent storage
    var timeSlotHeight by remember { mutableStateOf(0) } // TODO Proper calculation of slot height
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val columnScrollState = rememberScrollState()
    val taskList = taskViewModel.getTaskListByDate(uiState.value.selectedDay.atStartOfDay())
    val taskHeight = 80.dp
    val taskWidth = 100.dp


    Scaffold(
        topBar = {
            DateSideScroller(
                viewModel = viewModel,
                onTitleClick = {  }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    timeSlotHeight = coordinates.size.height
                }
                //.verticalScroll(columnScrollState)
        ) {
            timeslots.forEach() {slot ->
                TimeSlot(
                    timeSlot = slot,
                    slotHeight = (timeSlotHeight / timeslots.size).dp
                ) {
                    LazyVerticalGrid(columns = GridCells.Adaptive(taskWidth)) {
                        items(taskList.filter { slot.contains(it.deadline.hour) }  // TODO implement timeslot data class
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
    }
}

@Composable
fun TimeSlot(
    slotHeight: Dp,
    timeSlot: IntRange,
    content: @Composable () -> Unit
){
    HorizontalDivider()
    Row(modifier = Modifier
        .defaultMinSize(minHeight = 150.dp)
    ) {
        Column(
            modifier = Modifier
                .defaultMinSize(minHeight = 150.dp)
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
    val prioColor = when (task.priority) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> MaterialTheme.colorScheme.primary
        Priority.LOW -> Color.Green
    }
    ElevatedCard(
        modifier = Modifier
            .size(width, height)
            .padding(2.dp)
            .border(
                width = 1.dp,
                color = prioColor,
                shape = RoundedCornerShape(10.dp)
            )

    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = task.title, fontSize = 18.sp)
            // TODO add relevant info such as priority etc.
        }

    }
}
