package com.gruppe11.todoApp.ui.screens

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

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel
) {
    val taskViewModel: TaskViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val timeIntervals = viewModel.time.collectAsStateWithLifecycle(initialValue = emptyList())
    val columnState = rememberScrollState(initial = uiState.value.scollState)
    Scaffold(
        topBar = {
            DateSideScroller(
                viewModel = viewModel,
                jumpToCurrentTime = {
                    CoroutineScope(Dispatchers.Main).launch {
                        columnState.scrollTo(uiState.value.scollState)
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
                //.verticalScroll(state = columnState)
            ) {
                timeIntervals.value.forEach { time ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.CenterStart
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
            Column(
                modifier = Modifier.fillMaxWidth()
                //.verticalScroll(state = columnState)
            ) {
                taskViewModel.getTaskListByDate(uiState.value.selectedDay.atStartOfDay())
                    .filter { it.deadline.toLocalDate() == uiState.value.selectedDay }
                    .sortedBy { it.deadline }
                    .listIterator().forEach { task ->
                        ElevatedCard(
                            modifier = Modifier
                                .padding(10.dp)
                                .offset(
                                    x = 50.dp,
                                    y = (((task.deadline.hour * 120) + (task.deadline.minute * 2)) - 60).dp
                                )
                                .size(100.dp, 60.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(10.dp)
                                )

                        ) {
                            Column(verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = task.title.plus("\n").plus(task.priority))
                            }

                        }
                    }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(
                        x = 0.dp,
                        y = ((LocalDateTime.now().hour * 120) + (LocalDateTime.now().minute * 2)).dp
                    ),
                color = MaterialTheme.colorScheme.primary,
                thickness = 2.dp

            )

        }

    }
}

@Preview
@Composable
fun PreviewCalendarScreen(){
    CalendarScreen(CalendarViewModel(CalendarScreenState()))
}