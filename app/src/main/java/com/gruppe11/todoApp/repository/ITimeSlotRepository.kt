package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.TimeSlot
import kotlinx.coroutines.flow.Flow

interface ITimeSlotRepository {

    suspend fun create(timeSlot: TimeSlot): TimeSlot

    suspend fun find(id: Int) : TimeSlot?
    suspend fun readAll(): Flow<List<TimeSlot>>
    suspend fun update(timeSlot: TimeSlot): TimeSlot?
    suspend fun delete(timeSlot: TimeSlot)


}