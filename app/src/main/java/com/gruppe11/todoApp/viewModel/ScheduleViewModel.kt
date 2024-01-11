package com.gruppe11.todoApp.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.ITimeSlotRepository
import com.gruppe11.todoApp.ui.screenStates.ScheduleScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val timeSlotRepository: ITimeSlotRepository,
    private val taskRepository: ITaskRepository
    ): ViewModel() {

    private val _timeSlots = MutableStateFlow<List<TimeSlot>>(emptyList())

    val timeSlots = _timeSlots.asStateFlow()
    private val _uiState = MutableStateFlow(ScheduleScreenState())
    val uiState = _uiState.asStateFlow()
    val dates = getCalendarFlow()

    init {
        viewModelScope.launch(Dispatchers.Unconfined) {
            timeSlotRepository.readAll().collect{ timeslots ->
                _timeSlots.value = timeslots
            }
        }
    }

    private fun getCalendarFlow(): Flow<List<LocalDate>> {

        val dates = flow {
            emit(loadDates())
        }
        return dates
    }
    private fun loadDates(): List<LocalDate> {
        var currentDate = LocalDate.now().minusDays(7)
        var dateList = emptyList<LocalDate>()
        repeat(100) {
            currentDate = currentDate.plusDays(1L)
            dateList = dateList.plus(currentDate)
        }
        return dateList
    }
    fun createTimeSlot(timeSlot: TimeSlot){
        viewModelScope.launch {
            timeSlotRepository.create(timeSlot)
        }
    }
    fun updateTimeSlot(timeSlot: TimeSlot) {
        viewModelScope.launch {
            timeSlotRepository.update(timeSlot)
        }
    }
    // TODO Remove before shipping
    fun generateTestingTimeSlots() {
        var time = LocalDate.now().atStartOfDay().toLocalTime().plusHours(6L)

        repeat(3) {
            createTimeSlot(
                TimeSlot(
                    id = 0,
                    name = "Slot ".plus(it + 1),
                    start = time,
                    end = time.plusHours(3L),
                    tasks = emptyList()
                )
            )
            time = time.plusHours(6L)
        }
    }

    fun deleteTimeSlot(timeSlot: TimeSlot) {
        viewModelScope.launch {
            timeSlotRepository.delete(timeSlot)
        }
    }

    suspend fun toggleTaskCompletion(task: Task) {
        taskRepository.update(task)
    }

    fun changeSelectedDay(date: LocalDate) {
        _uiState.update {
            it.copy(selectedDay = date)
        }
    }
}