package com.gruppe11.todoApp.model

import java.time.LocalDate

data class CalendarScreenState(
    var selectedDay: LocalDate = LocalDate.now(),
    val currentDay: LocalDate = LocalDate.now()
){
    fun getSelection(): LocalDate = selectedDay
}

