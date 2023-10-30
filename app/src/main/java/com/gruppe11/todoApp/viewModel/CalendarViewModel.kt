package com.gruppe11.todoApp.viewModel

import com.gruppe11.todoApp.model.CalendarScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

class CalendarViewModel(
) {
    private val dayPeriod = Period.of(0, 0, 1)
    private val timePeriod = 30L
    val startDay = LocalDate.now().minusDays(30)
    var currentDate = startDay
    var dateList: List<LocalDate> = emptyList()
    var selectedDay: LocalDate = LocalDate.now()
    val dates: Flow<List<LocalDate>> = getCalendarFlow()
    var currentTime = LocalDateTime.MIN
    var timeIntervalList: List<LocalDateTime> = emptyList()
    val time = getTimeFlow()
    private val _uiState = MutableStateFlow(CalendarScreenState())
    val uiState = _uiState.asStateFlow()


    fun getCalendarFlow():Flow<List<LocalDate>> {
        loadDates()
        val dates = flow {
            emit(dateList)
        }
        return dates
    }
    fun chooseDay(day: LocalDate){
        selectedDay = day
    }
    fun loadDates() {
        repeat(100) {
            currentDate = currentDate.plus(dayPeriod)
            dateList = dateList.plus(currentDate)
        }
    }
    fun getTimeIntervals() {
        repeat(48) {
            timeIntervalList = timeIntervalList.plus(currentTime)
            currentTime = currentTime.plusMinutes(timePeriod)
        }
    }
    fun getTimeFlow():Flow<List<LocalDateTime>> {
        getTimeIntervals()
        val times = flow {
            emit(timeIntervalList)
        }
        return times
    }
}

