package com.gruppe11.todoApp.model

import com.gruppe11.todoApp.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Task (
    val id: Int,
    val title: String,
    val priority: Priority,

    @Serializable(with = LocalDateTimeSerializer::class)
    val deadline: LocalDateTime,

    val isCompleted: Boolean,
    val subtasks: List<SubTask>
)
