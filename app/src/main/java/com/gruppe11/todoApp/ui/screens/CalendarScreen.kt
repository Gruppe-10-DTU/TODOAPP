package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.ui.elements.DateSideScroller
import com.gruppe11.todoApp.ui.screenStates.CalendarScreenState
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import com.gruppe11.todoApp.viewModel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel
) {
    val timeSlotHeight = 120
    val taskViewModel: TaskViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val timeIntervals = viewModel.time.collectAsStateWithLifecycle(initialValue = emptyList())
    val columnState = rememberScrollState()

    Scaffold(
        topBar = {
            DateSideScroller(
                viewModel = viewModel,
                onTitleClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        columnState.scrollTo(uiState.value.scrollState.toInt() -
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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                timeIntervals.value.forEach { time ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(timeSlotHeight.dp),
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
            Box(
                modifier = Modifier.fillMaxWidth()
                //.verticalScroll(state = columnState)
            ) {
                taskViewModel.getTaskListByDate(uiState.value.selectedDay.atStartOfDay())
                    .filter { it.deadline.toLocalDate() == uiState.value.selectedDay && !it.isCompleted}
                    .sortedBy { it.deadline }
                    .forEach() { task ->
                        val taskHeight = 60
                        ElevatedCard(
                            modifier = Modifier
                                .offset(
                                    x = 50.dp,
                                    y = (
                                            (task.deadline.hour.times(timeSlotHeight)
                                                    + task.deadline.minute.times(timeSlotHeight/60))
                                            ).dp - taskHeight.dp
                                )
                                .size(150.dp, taskHeight.dp)
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
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(
                        x = 0.dp,
                        y = (
                                (LocalDateTime.now().hour.times(timeSlotHeight)
                                        + LocalDateTime.now().minute.times(timeSlotHeight/60))
                                ).dp
                    )
                    .onGloballyPositioned { coordinates ->
                    viewModel.onScrollStateChange(coordinates.positionInParent().y)
                },
                color = MaterialTheme.colorScheme.primary,
                thickness = 2.dp
            )
            CoroutineScope(Dispatchers.Main).launch {
                if(uiState.value.selectedDay == uiState.value.currentDay) {
                    columnState.scrollTo(
                        uiState.value.scrollState.toInt() -
                                (getSystem().displayMetrics.densityDpi)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCalendarScreen(){
    CalendarScreen(CalendarViewModel(CalendarScreenState()))
}