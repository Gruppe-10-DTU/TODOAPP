package com.gruppe11.todoApp.viewModel

import com.gruppe11.todoApp.ui.screenStates.ScheduleScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface ScheduleApi {

    val dates: Flow<List<LocalDate>>
    val uiState: StateFlow<ScheduleScreenState>
    fun onSelectedDayChange(day: LocalDate)
}