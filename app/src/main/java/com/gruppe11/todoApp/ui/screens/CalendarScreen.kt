package com.gruppe11.todoApp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.ui.elements.DateSideScroller
import com.gruppe11.todoApp.ui.elements.HourCalendarItem
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dates = viewModel.dates.collectAsStateWithLifecycle(initialValue = emptyList())
    val timeIntervals = viewModel.time.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            DateSideScroller(
                today = uiState.currentDay,
                selection = uiState.selectedDay,
                onClick = {
                    day -> viewModel.chooseDay(day)
                },
                dates = dates
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(items = timeIntervals.value, itemContent = { it: LocalDateTime ->
                //Text(text = it.hour.toString())
                HourCalendarItem(time = it.format(DateTimeFormatter.ISO_LOCAL_TIME).substring(0,5))
            })
        }

    }
}

@Preview
@Composable
fun PreviewCalendarScreen(){
    CalendarScreen(CalendarViewModel())
}