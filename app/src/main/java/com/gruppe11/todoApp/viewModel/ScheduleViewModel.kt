package com.gruppe11.todoApp.viewModel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.ITimeSlotRepository
import com.gruppe11.todoApp.ui.screenStates.ExecutionState
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

    private val _loadingState = MutableStateFlow(ExecutionState.RUNNING)
    val loadingState = _loadingState.asStateFlow()

    init {
        loadTimeslots()
    }

    fun loadTimeslots() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.value = ExecutionState.RUNNING
            try {
                val flow = timeSlotRepository.readAll()
                _loadingState.value = ExecutionState.SUCCESS
                flow.collect { timeslots ->
                    _timeSlots.value = timeslots
                }
            } catch (e: Exception) {
                _loadingState.value = ExecutionState.ERROR
                Log.d("timeslot", e.toString())
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
    suspend fun createTimeSlot(timeSlot: TimeSlot): TimeSlot? {
        var timeslot: TimeSlot? = null
        try {
            timeslot = timeSlotRepository.create(timeSlot)
        } catch (e: Exception) {
            Log.d("timeslot", e.toString())
        }
        return timeslot
    }

    fun updateTimeSlot(timeSlot: TimeSlot) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                timeSlotRepository.update(timeSlot)
            } catch (e: Exception) {
                Log.d("timeslot", e.toString())
            }
        }
    }

    fun deleteTimeSlot(timeSlot: TimeSlot) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                timeSlot.tasks.forEach{
                    taskRepository.update(it.copy(timeslot = null))
                }
                timeSlotRepository.delete(timeSlot)
            } catch (e: Exception) {
                Log.d("timeslot", e.toString())
            }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.update(task)
                timeSlotRepository.readAll()
            } catch (e: Exception) {
                Log.d("timeslot", e.toString())
            }
        }
    }

    fun changeSelectedDay(date: LocalDate) {
        _uiState.update {
            it.copy(selectedDay = date)
        }
    }
}