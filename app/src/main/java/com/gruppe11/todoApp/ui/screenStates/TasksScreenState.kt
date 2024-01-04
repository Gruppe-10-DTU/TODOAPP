package com.gruppe11.todoApp.ui.screenStates

import com.gruppe11.todoApp.model.Priority
import java.time.LocalDateTime

data class TasksScreenState(
    val selectedData: LocalDateTime,
    val completeFilter: Boolean,
    val incompleteFilter: Boolean,
    val priorities: MutableSet<Priority>,
    val sortedOption: String
)