package com.gruppe11.todoApp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity
data class TimeSlot(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "start") val start: LocalTime,
    @ColumnInfo(name = "end") val end: LocalTime,
    @ColumnInfo(name = "tasks") val tasks: List<Int>?
)
