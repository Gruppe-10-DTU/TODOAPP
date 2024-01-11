package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.network.TodoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TimeSlotRepositoryApi @Inject constructor(): ITimeSlotRepository {

    private val timeslots: MutableStateFlow<List<TimeSlot>> = MutableStateFlow(emptyList())

    override suspend fun create(timeSlot: TimeSlot): TimeSlot {
        val createdTimeslot = TodoApi.timeslotService.createTimeslots(timeSlot)
        timeslots.update { timeslots -> timeslots.toMutableList().apply { add(timeSlot) } }
        return createdTimeslot
    }

    override suspend fun find(id: Int): TimeSlot? {
        val timeslot = TodoApi.timeslotService.read(id)
        if (timeslot != null) {
            val index: Int = timeslots.value.indexOfFirst { it.id == id }
            if (index >= 0) {
                timeslots.update { timeslots ->
                    timeslots.toMutableList().apply { this[index] = timeslot }
                }
            } else {
                timeslots.update { timeslots -> timeslots.toMutableList().apply { add(timeslot) } }
            }
        }
        return timeslot
    }

    override suspend fun readAll(): Flow<List<TimeSlot>> {
        val newList = TodoApi.timeslotService.readAll()
        timeslots.value = newList
        return timeslots
    }

    override suspend fun update(timeSlot: TimeSlot): TimeSlot? {
        val updatedTimeslot = TodoApi.timeslotService.update(timeSlot.id, timeSlot)
        val index: Int = timeslots.value.indexOfFirst { it.id == timeSlot.id }
        if (index >= 0) {
            timeslots.update { timeslots ->
                timeslots.toMutableList().apply { this[index] = updatedTimeslot }
            }
        }
        return updatedTimeslot
    }

    override suspend fun delete(timeSlot: TimeSlot) {
        TodoApi.timeslotService.delete(timeSlot.id)
        timeslots.update { timeSlots ->
            timeSlots.toMutableList().apply { remove(timeSlot) }
        }
    }
}