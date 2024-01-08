package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.TimeSlot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TimeSlotRepositoryImplTest {



    @Test
    fun readAll() = runTest {
        val timeSlotRepository : ITimeSlotRepository = TimeSlotRepositoryImpl()
        val noTimeslots = timeSlotRepository.readAll().first()

        assertEquals(0, noTimeslots.size)
        assertTrue(noTimeslots.isEmpty())

        val startTime: LocalTime = LocalTime.of(10, 0, 0)
        val endTime = LocalTime.of(18, 0, 0)


        for( i in 0 .. 5) {
            timeSlotRepository.create(TimeSlot(20, "Timeslot $i" , startTime, endTime, emptyList()))
        }

        val timeslots = timeSlotRepository.readAll().first()
        assertEquals(6, timeslots.size)
        val ids : List<Int> = timeslots.map { e -> e.id }.toList()
        assertTrue(ids.containsAll(listOf(1,2,3,4,5, 6)))
    }

    @Test
    fun createNewTimeslotWithTasks() = runTest{
        val timeSlotRepository : ITimeSlotRepository = TimeSlotRepositoryImpl()
        val startTime: LocalTime = LocalTime.of(10, 0, 0)
        val endTime = LocalTime.of(18, 0, 0)
        val timeSlot = TimeSlot(20, "First timeslot", startTime, endTime, getTasks())

        val createdTimeSlot = timeSlotRepository.create(timeSlot)

        val timeslots = timeSlotRepository.readAll().first()
        assertEquals(1, timeslots.size)
        assertNotEquals(timeSlot.id, createdTimeSlot.id)
        assertEquals(6, createdTimeSlot.tasks.size)
    }

    private fun getTasks() : List<Task> {
        return listOf(
            Task(1, "test 1", Priority.LOW, LocalDateTime.now().minusDays(1), false),
            Task(2, "test 2", Priority.MEDIUM, LocalDateTime.now().minusDays(1), false),
            Task(3, "test 3", Priority.HIGH, LocalDateTime.now().minusDays(1), false),
            Task(4, "test 4", Priority.LOW, LocalDateTime.now(), true),
            Task(5, "test 5", Priority.MEDIUM, LocalDateTime.now(), false),
            Task(6, "test 6", Priority.HIGH, LocalDateTime.now(), false)
        )
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() = runTest{
        val timeSlotRepository = TimeSlotRepositoryImpl()
        val createdTimeslot = timeSlotRepository.create(TimeSlot(20, "Timeslot 1" , LocalTime.of(5,0,0), LocalTime.of(7,0,0), emptyList()))

        assertEquals(1, timeSlotRepository.readAll().first().size)

        timeSlotRepository.delete(createdTimeslot)
        val timeSlots = timeSlotRepository.readAll().first()
        assertNotEquals(1, timeSlots.size)
        assertTrue(timeSlots.isEmpty())
    }
}