package com.gruppe11.todoApp.viewModel

import com.gruppe11.todoApp.MainCoroutineRule
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.SubtaskRepositoryImpl
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

class CalendarViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK(relaxed = true)
    lateinit var mockTaskRepository: ITaskRepository

    @Before
    fun setUp() {

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


    private fun getTasksFlow(
        deferred: CompletableDeferred<Unit>? = null
    ) : Flow<List<Task>> {
        val tasksList = getTasks()
        val tasksFlow = flow {
            emit(tasksList)
        }

        return tasksFlow
    }

    @Test
    fun loadDatesTest() = runTest{
        val viewModel = CalendarViewModel(mockTaskRepository)
        val dates = viewModel.dates.first()
        assertEquals(100, dates.size)
    }

    @Test
    fun timeAllocation() = runTest{
        val viewModel = CalendarViewModel(mockTaskRepository)
        val times = viewModel.time.first()
        assertEquals(24,times.size)
    }

    @Test
    fun updateScrollState() = runTest {
        val viewModel = CalendarViewModel(mockTaskRepository)
        val newFloatState = 0.4F

        assertEquals(1F, viewModel.uiState.value.scrollState)
        viewModel.onScrollStateChange(newFloatState)

        assertEquals(newFloatState, viewModel.uiState.value.scrollState)
    }

}