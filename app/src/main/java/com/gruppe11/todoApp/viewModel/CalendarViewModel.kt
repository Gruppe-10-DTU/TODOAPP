package com.gruppe11.todoApp.viewModel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.Period

class CalendarViewModel: ViewModel() {
    val cal = Calendar.getInstance()
    private val dayPeriod = Period.of(0, 0, 1)
    val currentDate = LocalDate.now()
    var selectedDay: LocalDate = LocalDate.now()
    val dates: Flow<List<LocalDate>> = getCalendarFlow()

    fun getCalendarFlow():Flow<List<LocalDate>> {
        val dates = flow {
            while (true) {
                emit(listOf(currentDate.plus(dayPeriod)))
            }
        }
        return dates
    }
    fun selectDay(day: LocalDate){
        selectedDay = day
    }
}

