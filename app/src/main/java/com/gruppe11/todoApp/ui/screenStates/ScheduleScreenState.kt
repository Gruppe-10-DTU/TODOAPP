package com.gruppe11.todoApp.ui.screenStates

import java.time.LocalDate
import java.time.LocalTime

data class ScheduleScreenState(
    val selectedDay: LocalDate = LocalDate.now(),
    val currentDay: LocalDate = LocalDate.now(),
    val currentTime: LocalTime = LocalTime.now(),
    val scrollState: Float = 1F
)

