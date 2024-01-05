package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.TimeSlot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TimeSlotRepositoryImpl: ITimeSlotRepository {

    private val timeslots: MutableStateFlow<List<TimeSlot>> = MutableStateFlow(emptyList())
    private var id = 1
    override fun create(timeSlot: TimeSlot): TimeSlot {
        timeslots.update {list ->
            list.plus(timeSlot.copy(id = id++))
        }
        return timeSlot
    }

    override fun readAll(): Flow<List<TimeSlot>> {
        return timeslots
    }

    override fun update(timeSlot: TimeSlot): TimeSlot? {
        val tmp = timeslots.value.filter { it.id == timeSlot.id }
        if (tmp.isNotEmpty()) {
            timeslots.update { list ->
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