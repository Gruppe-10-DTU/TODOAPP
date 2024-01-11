package com.gruppe11.todoApp.model

import com.gruppe11.todoApp.serializer.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class TimeSlot(
    val id: Int,
    val name: String,
    @Serializable(with = LocalTimeSerializer::class)
    val start: LocalTime,
    @Serializable(with = LocalTimeSerializer::class)
    val end: LocalTime,
    val tasks: List<Task>
)
