package com.gruppe11.todoApp.viewModel

import com.gruppe11.todoApp.ui.screenStates.CalendarScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

class CalendarViewModel(
    state: CalendarScreenState
) {
    private val dayPeriod = Period.of(0, 0, 1)

    val startDay: LocalDate = LocalDate.now().minusDays(7)
    private var dateList: List<LocalDate> = emptyList()
    val dates: Flow<List<LocalDate>> = getCalendarFlow()

    var currentTime: LocalDateTime = LocalDateTime.MIN
    private var timeIntervalList: List<LocalDateTime> = emptyList()
    val time = getTimeFlow()

    private val _uiState = MutableStateFlow(state)
    val uiState = _uiState.asStateFlow()


    private fun getCalendarFlow():Flow<List<LocalDate>> {
        loadDates()
        val dates = flow {
            emit(dateList)
        }
        return dates
    }

    private fun loadDates() {
        var currentDate = startDay
        repeat(100) {
            currentDate = currentDate.plus(dayPeriod)
            dateList = dateList.plus(currentDate)
        }
    }
    private fun getTimeIntervals() {
        repeat(24) {
            timeIntervalList = timeIntervalList.plus(currentTime)
            currentTime = currentTime.plusHours(1L)
        }
    }
    private fun getTimeFlow():Flow<List<LocalDateTime>> {
        getTimeIntervals()
        val times = flow {
            emit(timeIntervalList)
        }
        return times
    }

    fun onSelectedDayChange(day: LocalDate){
        _uiState.update { currentState ->
            currentState.copy(
                selectedDay = day
            )
        }
    }
}

