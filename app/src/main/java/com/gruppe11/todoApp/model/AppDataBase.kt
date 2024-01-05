package com.gruppe11.todoApp.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gruppe11.todoApp.repository.ITimeSlotRepository

@Database(entities = [TimeSlot::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timeSlotDao(): ITimeSlotRepository
}
