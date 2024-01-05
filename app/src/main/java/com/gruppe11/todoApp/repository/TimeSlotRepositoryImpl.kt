package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.TimeSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TimeSlotRepositoryImpl: ITimeSlotRepository {

    private val _timeslots: MutableStateFlow<List<TimeSlot>> = MutableStateFlow(emptyList())
    val slots = _timeslots.asStateFlow()

    override fun create(timeSlot: TimeSlot): TimeSlot {
        _timeslots.update {list ->
            list.plus(timeSlot)
        }
        return timeSlot
    }

    override fun read(): List<TimeSlot> {
        return slots.value
    }

    override fun update(timeSlot: TimeSlot): TimeSlot? {
        val tmp = _timeslots.value.filter { it.id == timeSlot.id }
        if (tmp.isNotEmpty()) {
            _timeslots.update { list ->
                list.toMutableList().apply {
                    this[list.indexOfFirst { it.id == timeSlot.id }]  = timeSlot}
            }
            return timeSlot
        }
        return null
    }

    override fun delete(timeSlot: TimeSlot) {
        TODO("Not yet implemented")
    }
}