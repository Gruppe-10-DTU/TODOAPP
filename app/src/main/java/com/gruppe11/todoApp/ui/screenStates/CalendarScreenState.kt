package com.gruppe11.todoApp.ui.screenStates

import java.time.LocalDate
import java.time.LocalDateTime

data class CalendarScreenState(
    val selectedDay: LocalDate = LocalDate.now(),
    val currentDay: LocalDate = LocalDate.now(),
    val scollState: Int = LocalDateTime.now().hour * 300
)

