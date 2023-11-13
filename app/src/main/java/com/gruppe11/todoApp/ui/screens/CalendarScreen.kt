package com.gruppe11.todoApp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    for(i in 1.. 5) {
        taskViewModel.addTask(
            i,
            "Task: 3$i",
            LocalDateTime.now().plusDays(1L).plusHours(i % 3L),
            "HIGH",
            false
        )
    }
    val uiState = viewModel.uiState.collectAsState()
    val timeIntervals = viewModel.time.collectAsStateWithLifecycle(initialValue = emptyList())
    val columnState = rememberLazyListState()
    Scaffold(
        topBar = {
            DateSideScroller(
                viewModel = viewModel,
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(items = timeIntervals.value, itemContent = {time ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = time.format(DateTimeFormatter.ofPattern("HH:mm")))
                    Row() {
                        taskViewModel.getTaskListByDate(uiState.value.selectedDay.atStartOfDay())
                            .filter { it.completion?.hour == time.hour }
                            .listIterator().forEach { task ->
                                ElevatedCard() {
                                    Text(text = task.title)
                                }
                            }
                    }
                }
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .alpha(0.3F))
            }
            )
            CoroutineScope(Dispatchers.Main).launch {
                columnState.scrollToItem(
                    index = uiState.value.selectedDay.atStartOfDay().plusHours(LocalDateTime.now().hour.toLong()).hour,
                    scrollOffset = 0
                )

            }
        }

    }
}

@Preview
@Composable
fun PreviewCalendarScreen(){
    CalendarScreen(CalendarViewModel(CalendarScreenState()))
}