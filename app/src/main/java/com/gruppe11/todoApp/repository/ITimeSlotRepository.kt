package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.TimeSlot

interface ITimeSlotRepository {

    fun create(timeSlot: TimeSlot): TimeSlot
    fun read(): List<TimeSlot>
    fun update(timeSlot: TimeSlot): TimeSlot?
    fun delete(timeSlot: TimeSlot)

}