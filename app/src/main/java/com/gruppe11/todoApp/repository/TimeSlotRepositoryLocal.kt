package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.TimeSlot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TimeSlotRepositoryLocal @Inject constructor() : ITimeSlotRepository {

    private val timeslots: MutableStateFlow<List<TimeSlot>> = MutableStateFlow(emptyList())
    private var id = 1
    override suspend fun create(timeSlot: TimeSlot): TimeSlot {
        timeslots.update {list ->
            list.plus(timeSlot.copy(id = id++))

        }
        return timeSlot
    }

    override suspend fun readAll(): Flow<List<TimeSlot>> {
        return timeslots
    }

    override suspend fun find(id: Int): TimeSlot? {
        val timeslot = timeslots.value.find { it.id == id }
        return timeslot

    }
    override suspend fun update(timeSlot: TimeSlot): TimeSlot? {
        val tmp = timeslots.value.filter { it.id == timeSlot.id }
        if (tmp.isNotEmpty()) {
            timeslots.update { list ->
                list.toMutableList().apply {
                    this[list.indexOfFirst { it.id == timeSlot.id }]  = timeSlot
                }.sortedBy { it.start }
            }
            return timeSlot
        }
        return null
    }

    override suspend fun delete(timeSlot: TimeSlot) {
        timeslots.update {list ->
            list.filterNot { it == timeSlot }
        }
    }
}