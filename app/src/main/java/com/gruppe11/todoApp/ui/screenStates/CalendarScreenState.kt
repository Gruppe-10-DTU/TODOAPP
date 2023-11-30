package com.gruppe11.todoApp.ui.screenStates

import java.time.LocalDate

data class CalendarScreenState(
    val selectedDay: LocalDate = LocalDate.now(),
    val currentDay: LocalDate = LocalDate.now(),
    val scrollState: Float = 1F
)

