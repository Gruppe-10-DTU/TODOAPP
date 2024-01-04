package com.gruppe11.todoApp.ui.screens


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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


@Composable
fun SchedulingScreen(
        viewModel: CalendarViewModel,
        taskViewModel: TaskViewModel = hiltViewModel()
) {
    val timeslots =  (2 .. 5).toList() // TODO Fetch list from persistent storage
    val timeSlotHeight = 180.dp // TODO Proper calculation of slot height
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DateSideScroller(
                viewModel = viewModel,
                onTitleClick = {  }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(timeslots) {slot ->
                TimeSlot(
                    timeSlot = slot,
                    timeSlotHeight = timeSlotHeight
                ) {
                    LazyHorizontalGrid(rows = GridCells.Fixed(2),
                        modifier = Modifier.height(timeSlotHeight)) {
                        items(taskViewModel.getTaskListByDate(uiState.value.selectedDay.atStartOfDay())
                            .sortedBy { it.deadline }
                            .filter { it.deadline.hour <= slot && it.deadline.hour >= slot }  // TODO implement timeslot data class
                        ) { task ->
                            ScheduleTask(task = task)
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
    timeSlotHeight: Dp,
    timeSlot: Any,
    content: @Composable () -> Unit
){
    HorizontalDivider()
    Row(modifier = Modifier.height(timeSlotHeight)) {
        Column(
            modifier = Modifier
                .width(60.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Slot ".plus(timeSlot),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "start")
            Text(text = "-")
            Text(text = "end")
            Spacer(Modifier.fillMaxHeight())
        }
        content()
    }
}

@Composable
fun ScheduleTask(
    task: Task
){
    val prioColor = when (task.priority) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> MaterialTheme.colorScheme.primary
        Priority.LOW -> Color.Green
    }
    ElevatedCard(
        modifier = Modifier
            .width(80.dp)
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
