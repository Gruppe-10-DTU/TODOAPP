package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.network.TodoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TimeSlotRepositoryApi @Inject constructor(): ITimeSlotRepository {

    private val _timeslots: MutableStateFlow<List<TimeSlot>> = MutableStateFlow(emptyList())
    override val timeSlots: StateFlow<List<TimeSlot>>
        get() = _timeslots.asStateFlow()

    override suspend fun create(timeSlot: TimeSlot): TimeSlot {
        val createdTimeslot = TodoApi.timeslotService.createTimeslots(timeSlot)
        readAll()
        return createdTimeslot
    }

    override suspend fun find(id: Int): TimeSlot? {
        val timeslot = TodoApi.timeslotService.read(id)
        return timeslot
    }

    override suspend fun readAll(): Flow<List<TimeSlot>> {
        val newList = TodoApi.timeslotService.readAll()
        _timeslots.update { newList.sortedBy { it.start } }
        return timeSlots
    }

    override suspend fun update(timeSlot: TimeSlot): TimeSlot {
        val updatedTimeslot = TodoApi.timeslotService.update(timeSlot.id, timeSlot)
        readAll()
        return updatedTimeslot
    }

    override suspend fun delete(timeSlot: TimeSlot) {
        TodoApi.timeslotService.delete(timeSlot.id)
        readAll()
    }
}