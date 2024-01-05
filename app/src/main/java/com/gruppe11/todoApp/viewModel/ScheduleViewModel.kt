package com.gruppe11.todoApp.viewModel


import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.repository.ITimeSlotRepository
import com.gruppe11.todoApp.ui.screenStates.ScheduleScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import java.time.LocalDate


class ScheduleViewModel(
    private val timeSlotRepository: ITimeSlotRepository,
    ): ScheduleApi, ViewModel() {

    val timeSlots = timeSlotRepository.readAll()
    private val _uiState = MutableStateFlow(ScheduleScreenState())
    override val uiState = _uiState.asStateFlow()
    override val dates = getCalendarFlow()

    override fun onSelectedDayChange(day: LocalDate){
        _uiState.update { currentState ->
            currentState.copy(
                selectedDay = day
            )
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
        timeSlotRepository.create(timeSlot)
    }

    // TODO Remove before shipping
    fun generateTestingTimeSlots() {
        var time = LocalDate.now().atStartOfDay().toLocalTime().plusHours(6L)

        repeat(3) {
            timeSlotRepository.create(
                TimeSlot(
                    id = 0,
                    name = "Slot $it",
                    start = time,
                    end = time.plusHours(3L),
                    tasks = emptyList()
                )
            )
            time = time.plusHours(6L)
        }
    }
}