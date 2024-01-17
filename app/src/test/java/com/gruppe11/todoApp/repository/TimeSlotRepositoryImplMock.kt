package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.TimeSlot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TimeSlotRepositoryImplMock: ITimeSlotRepository {

    override val timeSlots: StateFlow<List<TimeSlot>>
        get() = _timeslots.asStateFlow()
    private val _timeslots: MutableStateFlow<List<TimeSlot>> = MutableStateFlow(emptyList())
    private var id = 1
    override suspend fun create(timeSlot: TimeSlot): TimeSlot {
        _timeslots.update { list ->
            list.plus(timeSlot.copy(id = id++))

        }
        return timeSlot
    }

    override suspend fun readAll(): Flow<List<TimeSlot>> {
        return _timeslots
    }

    override suspend fun find(id: Int): TimeSlot? {
        val timeslot = _timeslots.value.find { it.id == id }
        return timeslot

    }
    override suspend fun update(timeSlot: TimeSlot): TimeSlot? {
        val tmp = _timeslots.value.filter { it.id == timeSlot.id }
        if (tmp.isNotEmpty()) {
            _timeslots.update { list ->
                list.toMutableList().apply {
                    this[list.indexOfFirst { it.id == timeSlot.id }]  = timeSlot
                }.sortedBy { it.start }
            }
            return timeSlot
        }
        return null
    }

    override suspend fun delete(timeSlot: TimeSlot) {
        _timeslots.update { list ->
            list.filterNot { it == timeSlot }
        }
    }
}