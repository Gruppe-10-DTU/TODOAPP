package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.TimeSlot
import kotlinx.coroutines.flow.Flow

interface ITimeSlotRepository {

    fun create(timeSlot: TimeSlot): TimeSlot
    fun readAll(): Flow<List<TimeSlot>>
    fun update(timeSlot: TimeSlot): TimeSlot?
    fun delete(timeSlot: TimeSlot)

}