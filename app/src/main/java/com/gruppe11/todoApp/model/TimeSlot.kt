package com.gruppe11.todoApp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

data class TimeSlot(
    val id: Int,
    val name: String,
    val start: LocalTime,
    val end: LocalTime,
    val tasks: List<Int>?
)
