package com.gruppe11.todoApp.viewModel


import com.gruppe11.todoApp.MainCoroutineRule
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.SubtaskRepositoryImpl
import com.gruppe11.todoApp.repository.TimeSlotRepositoryImpl
import com.gruppe11.todoApp.repository.ITimeSlotRepository

import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import io.mockk.every
import io.mockk.verify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException
import com.gruppe11.todoApp.model.TimeSlot
import org.junit.Assert
import java.time.LocalTime

class ScheduleViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK(relaxed = true)
    lateinit var mockTimeSlotRepository : ITimeSlotRepository


    @Before
    fun setUp() {

    }
    private fun getTimeSlotFlows() : Flow<List<TimeSlot>> {
        val timeSlotList = getTimeSlots()
        val timeSlotFlow = flow {
            emit(timeSlotList)
        }

        return timeSlotFlow
    }

    private fun getTimeSlots(): List<TimeSlot> {
        val timeslots = ArrayList<TimeSlot>()
        var time = LocalTime.of(5,0,0)
        for (i in 1..10) {
            timeslots.add(TimeSlot(
                id = 0,
                name = "Slot $i",
                start = time,
                end = time.plusHours(1L),
                tasks = emptyList()
            ))
            time.plusHours(1L)
        }

        return timeslots
    }


    @Test
    fun createTimeSlotTest() = runTest{
        val timeSlotRepository = TimeSlotRepositoryImpl()
        var timeForCreation = LocalTime.of(5,0,0)
        for (i in 1..10) {
            timeSlotRepository.create(TimeSlot(
                id = 0,
                name = "Slot $i",
                start = timeForCreation,
                end = timeForCreation.plusHours(1L),
                tasks = emptyList()
            ))
            timeForCreation.plusHours(1L)
        }
        val viewModel = ScheduleViewModel(timeSlotRepository)
        assertEquals(10,viewModel.timeSlots.first().size)
        var time = LocalTime.of(22,0,0)
        viewModel.createTimeSlot(TimeSlot(
                id = 11,
                name = "Slot $11",
                start = time,
                end = time.plusHours(1L),
                tasks = emptyList()
            ))
        Assert.assertEquals(11, viewModel.timeSlots.first().size)
    }
}