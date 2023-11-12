package com.gruppe11.todoApp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.ui.elements.DateSideScroller
import com.gruppe11.todoApp.ui.elements.HourCalendarItem
import com.gruppe11.todoApp.ui.screenStates.CalendarScreenState
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel
) {
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
            items(items = timeIntervals.value, itemContent = {
                HourCalendarItem(time = it.format(DateTimeFormatter.ofPattern("HH:mm")))
            }
            )
            CoroutineScope(Dispatchers.Main).launch {
                columnState.scrollToItem(
                    index = 10,
                    scrollOffset = 10
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