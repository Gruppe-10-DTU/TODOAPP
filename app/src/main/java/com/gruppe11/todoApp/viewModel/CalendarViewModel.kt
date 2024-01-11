package com.gruppe11.todoApp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.ui.screenStates.ScheduleScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    taskRepository : ITaskRepository
) : ViewModel()  {
    private val state = ScheduleScreenState()

    private val dayPeriod = Period.of(0, 0, 1)

    val startDay: LocalDate = LocalDate.now().minusDays(7)
    private var dateList: List<LocalDate> = emptyList()
    val dates = getCalendarFlow()

    var currentTime: LocalDateTime = LocalDateTime.MIN
    private var timeIntervalList: List<LocalDateTime> = emptyList()
    val time = getTimeFlow()

    private val _uiState = MutableStateFlow(state)
    val uiState = _uiState.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    val tasks = _tasks.combine(_uiState) { tasks, uiState ->
        tasks.filter { it.deadline.toLocalDate() == uiState.selectedDay && !it.isCompleted }
            .sortedBy { it.deadline }
    }.distinctUntilChanged()

    init {
        viewModelScope.launch {
            taskRepository.readAll().collect{
                _tasks.value = it
            }
        }
    }


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

    fun onScrollStateChange(value: Float){
        _uiState.update { currentState ->
            currentState.copy(
                scrollState = value
            )
        }
    }

    fun changeSelectedDate(date: LocalDate) {
        _uiState.update {
            it.copy(selectedDay = date)
        }
    }
}

