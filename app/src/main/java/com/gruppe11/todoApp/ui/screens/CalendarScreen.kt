package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.ui.elements.DateSideScroller
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dates = viewModel.dates.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            DateSideScroller(
                viewModel = viewModel,
                today = uiState.currentDay,
                selection = uiState.selectedDay,
                onClick = {
                    day -> uiState.selectedDay = day
                },
                dates = dates
            )
        },
        bottomBar = { BottomAppBar {
        }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(text = "MERE")
        }
        uiState
    }
}

@Preview
@Composable
fun PreviewCalendarScreen(){
    CalendarScreen(CalendarViewModel())
}