package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.Task
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class TaskRepositoryImplTest {
    private lateinit var taskRepository: TaskRepositoryImpl
    @Before
    fun createRepo() {
        taskRepository = TaskRepositoryImpl()
    }

    @Test
    fun createTask() = runTest {
        //Arrange
        val task = Task(5, "Test", Priority.HIGH,  LocalDateTime.now(),false, emptyList())

        //Act
        val taskCreated : Task = taskRepository.createTask(task)

        //Assert
        assertNotEquals(task.id, taskCreated.id)
        assertEquals(1, taskCreated.id)
        assertEquals(1, taskRepository.readAll().first().size)
    }

    @Test
    fun findTask() {
        CoroutineScope(Dispatchers.Main).launch {
            //Arrange
            val task = Task(1, "Test", Priority.HIGH, LocalDateTime.now(), false,
                emptyList()
            )
            taskRepository.createTask(task)

            //Act
            val taskFound: Task? = taskRepository.read(1)

            //Assert
            assertEquals(task.id, taskFound?.id)
        }
    }

    @Test
    fun findTaskNotFound() {
        CoroutineScope(Dispatchers.Main).launch {
            //Arrange
            val task = Task(1, "Test", Priority.HIGH, LocalDateTime.now(), false,
                emptyList()
            )
            taskRepository.createTask(task)

            //Act
            val taskFound: Task? = taskRepository.read(4)

            //Assert
            assertNull(taskFound)
        }
    }

    @Test
    fun findAllTasks() = runTest {
        //Arrange
        for (i in 1..5) {
            val task = Task(i, "Test", Priority.HIGH,  LocalDateTime.now(),false,
                emptyList()
            )
            taskRepository.createTask(task)
        }

        //Act
        val tasks : List<Task>  = taskRepository.readAll().first()

        //Assert
        assertEquals(5, tasks.size)
    }

    @Test
    fun findAllTasksEmpty() = runTest {
        //Arrange

        //Act
        val tasks : List<Task>  = taskRepository.readAll().first()

        //Assert
        assertTrue(tasks.isEmpty())
    }

    @Test
    fun deleteTask() {
        CoroutineScope(Dispatchers.Main).launch {
            var task = Task(1, "Test", Priority.HIGH, LocalDateTime.now(), false, emptyList())
            task = taskRepository.createTask(task)
            var taskFound: Task? = taskRepository.read(1)
            assertEquals(task.id, taskFound?.id)

            //Act
            taskRepository.delete(task)

            //Assert
            taskFound = taskRepository.read(1)
            assertNull(taskFound)
        }
    }

    @Test
    fun updateTask() {
        CoroutineScope(Dispatchers.Main).launch {
            val createdTask: Task = taskRepository.createTask(
                Task(
                    1,
                    "Test",
                    Priority.HIGH,
                    LocalDateTime.now(),
                    false,
                    emptyList()
                )
            )

            //Act
            val updateTask = taskRepository.update(createdTask.copy(priority = Priority.LOW))

            //Assert
            assertEquals(updateTask.priority, Priority.LOW)
            assertEquals(createdTask.id, updateTask.id)
            assertNotEquals(createdTask.priority, updateTask.priority)
        }
    }
}