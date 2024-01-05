package com.gruppe11.todoApp.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.TimeSlot
import javax.inject.Qualifier

@Dao
interface ITimeSlotRepository {

    @Insert
    fun create(timeSlot: TimeSlot): TimeSlot

    @Query("SELECT * FROM timeslot")
    fun read(): List<TimeSlot>

    @Update
    fun update(timeSlot: TimeSlot): TimeSlot?

    @Delete
    fun delete(timeSlot: TimeSlot)

}