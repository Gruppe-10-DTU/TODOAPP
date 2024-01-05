package com.gruppe11.todoApp.ui.screens

import android.content.res.Resources.getSystem
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.ui.elements.DateSideScroller
import com.gruppe11.todoApp.ui.screenStates.CalendarScreenState
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel<CalendarViewModel>(),
) {
    val tasks = calendarViewModel.tasks.collectAsStateWithLifecycle(initialValue = emptyList())
    val timeSlotHeight = 120
    val uiState = calendarViewModel.uiState.collectAsStateWithLifecycle()
    val timeIntervals = calendarViewModel.time.collectAsStateWithLifecycle(initialValue = emptyList())
    val columnState = rememberScrollState()

    Scaffold(
        topBar = {
            DateSideScroller(
                viewModel = calendarViewModel,
                onTitleClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        columnState.scrollTo(
                            uiState.value.scrollState.toInt() -
                                    (getSystem().displayMetrics.densityDpi)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(state = columnState)
        ) {
            TimeTable(
                timeIntervals = timeIntervals,
                slotHeight = timeSlotHeight
            )

            TaskTable(
                tasks = tasks,
                slotHeight = timeSlotHeight
            )

            if (uiState.value.selectedDay == uiState.value.currentDay) {
                CurrentTimeLine(
                    slotHeight = timeSlotHeight,
                    viewModel = calendarViewModel
                )
            }
        }
        LaunchedEffect(uiState.value) {
            if (uiState.value.selectedDay == uiState.value.currentDay) {
                columnState.scrollTo(
                    uiState.value.scrollState.toInt() -
                            (getSystem().displayMetrics.densityDpi)
                )
            }
        }
    }
}

@Composable
fun TimeTable(
    timeIntervals : State<List<LocalDateTime>>,
    slotHeight : Int,
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        timeIntervals.value.forEach { time ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(slotHeight.dp),
                contentAlignment = Alignment.TopStart
            ) {
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .alpha(0.35F)
                )
                Text(
                    text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    color = MaterialTheme.colorScheme.onBackground
                )

            }
        }
    }

}

@Composable
fun TaskTable(
    tasks: State<List<Task>>,
    slotHeight: Int
){
    Box(
        modifier = Modifier.fillMaxWidth()
        //.verticalScroll(state = columnState)
    ) {
        var taskOffset = 0
        val taskHeight = 60
        val taskWidth = 150
        var sideFlip = 1
            tasks.value
            .plus(null)
            .zipWithNext { taskA, taskB ->
                CalendarTask(
                    task = taskA!!,
                    offset = 50 + taskOffset,
                    slotHeight = slotHeight,
                    taskHeight = taskHeight,
                    taskWidth = taskWidth
                )
                if (taskB != null) {
                    if (taskA.deadline <= taskB.deadline) taskOffset += taskWidth * sideFlip
                    else taskOffset -= taskWidth * sideFlip
                    CalendarTask(
                        task = taskB,
                        offset = 50 + taskOffset,
                        slotHeight = slotHeight,
                        taskHeight = taskHeight,
                        taskWidth = taskWidth
                    )
                    sideFlip *= -1
                }
            }
    }
}

@Composable
fun CalendarTask(
    task : Task,
    offset: Int,
    slotHeight: Int,
    taskHeight: Int,
    taskWidth: Int
){
    ElevatedCard(
        modifier = Modifier
            .offset(
                x = offset.dp,
                y = (
                        (task.deadline.hour.times(slotHeight)
                                + task.deadline.minute.times(slotHeight / 60))
                        ).dp - taskHeight.dp
            )
            .size(taskWidth.dp, taskHeight.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )

    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = task.title)
        }

    }
}
@Composable
fun CurrentTimeLine(
    slotHeight: Int,
    viewModel: CalendarViewModel
){
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .offset(
                x = 0.dp,
                y = (
                        (LocalDateTime.now().hour.times(slotHeight)
                                + LocalDateTime.now().minute.times(slotHeight / 60))
                        ).dp
            )
            .onGloballyPositioned { coordinates ->
                viewModel.onScrollStateChange(coordinates.positionInParent().y)
            },
        color = MaterialTheme.colorScheme.primary,
        thickness = 2.dp
    )

}

@Preview
@Composable
fun PreviewCalendarScreen(){
    CalendarScreen()
}