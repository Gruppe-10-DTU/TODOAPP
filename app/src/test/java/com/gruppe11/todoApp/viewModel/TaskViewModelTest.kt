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

class TaskViewModelTest() {

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
    fun filterTasksByPriority() = runTest {
        val deferred = CompletableDeferred<Unit>()

        every { mockTaskRepository.readAll() } returns getTasksFlow(deferred)

        val viewmodel = TaskViewModel(mockTaskRepository, SubtaskRepositoryImpl())

        val defaultTasks = viewmodel.TaskState.first()

        assertEquals(3, defaultTasks.size)

        viewmodel.addPriority(Priority.HIGH)

        val filteredTasks = viewmodel.TaskState.first()
        assertEquals(1, filteredTasks.size)
        assertEquals("test 6", filteredTasks[0].title)
    }

    @Test
    fun filterTasksByComplete() = runTest {
        val deferred = CompletableDeferred<Unit>()

        every { mockTaskRepository.readAll() } returns getTasksFlow(deferred)

        val viewmodel = TaskViewModel(mockTaskRepository, SubtaskRepositoryImpl())

        viewmodel.changeFilter("complete")

        val tasks = viewmodel.TaskState.first()
        assertEquals(1, tasks.size)
        assertEquals("test 4", tasks[0].title)
    }

    @Test
    fun filterTasksByIncomplete() = runTest {
        val deferred = CompletableDeferred<Unit>()

        every { mockTaskRepository.readAll() } returns getTasksFlow(deferred)

        val viewmodel = TaskViewModel(mockTaskRepository, SubtaskRepositoryImpl())

        viewmodel.changeFilter("incomplete")

        val tasks = viewmodel.TaskState.first()
        assertEquals(2, tasks.size)
        assertEquals("test 6", tasks[0].title)
        assertEquals("test 5", tasks[1].title)
    }



    @Test
    fun filterTasksByDateCorrectly() = runTest {
        val deferred = CompletableDeferred<Unit>()

        mockTaskRepository.apply{
            every{readAll()} returns getTasksFlow(deferred)
        }

        val viewModel = TaskViewModel(mockTaskRepository,SubtaskRepositoryImpl())

        var taskList = viewModel.TaskState.first()
        assertEquals(3, taskList.size)
        assertEquals("test 6", taskList[0].title)

        viewModel.changeDate(LocalDateTime.now().minusDays(1))
        taskList = viewModel.TaskState.first()
        assertEquals(3,taskList.size)
        assertEquals("test 3", taskList[0].title)

        viewModel.changeDate(LocalDateTime.now().minusDays(2))
        taskList = viewModel.TaskState.first()
        assertTrue(taskList.isEmpty())
    }

    @Test
    fun sortTaskByPriority() = runTest{
        val deferred = CompletableDeferred<Unit>()

        mockTaskRepository.apply{
            every{readAll()} returns getTasksFlow(deferred)
        }
        val viewModel = TaskViewModel(mockTaskRepository,SubtaskRepositoryImpl())
        var taskList = viewModel.TaskState.first()
        assertEquals(Priority.HIGH,taskList.first().priority)
        viewModel.selectSortingOption("Priority Ascending")
        taskList = viewModel.TaskState.first()

        assertEquals(Priority.LOW,taskList.first().priority)

        viewModel.selectSortingOption("Priority Descending")
        taskList = viewModel.TaskState.first()
        assertEquals(Priority.HIGH,taskList.first().priority)

    }

    @Test
    fun sortTasksByTitle() = runTest {
        val deferred = CompletableDeferred<Unit>()

        mockTaskRepository.apply{
            every{readAll()} returns getTasksFlow(deferred)
        }
        val viewModel = TaskViewModel(mockTaskRepository,SubtaskRepositoryImpl())
        viewModel.selectSortingOption("A-Z")

        var taskList = viewModel.TaskState.first()
        assertEquals("test 4",taskList.first().title)

        viewModel.selectSortingOption("Z-A")
        taskList = viewModel.TaskState.first()

        assertEquals("test 6" ,taskList.first().title)
    }
}