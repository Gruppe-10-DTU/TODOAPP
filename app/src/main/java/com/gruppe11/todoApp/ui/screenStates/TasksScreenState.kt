package com.gruppe11.todoApp.ui.screenStates

import java.time.LocalDate
import java.time.LocalDateTime

data class TasksScreenState(
    val selectedData: LocalDateTime,
    val completeFilter: Boolean,
    val incompleteFilter: Boolean
)