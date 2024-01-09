package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import java.time.LocalDateTime

class SubtaskRepositoryImplTest {

    private lateinit var subtaskRepository: SubtaskRepositoryImpl
    private lateinit var task: Task
    @Before
    fun createRepo() {
        subtaskRepository = SubtaskRepositoryImpl()
        task =  Task(1, "test", Priority.HIGH, LocalDateTime.now(), false, emptyList())
    }

    @Test
    fun createSubtaskEmpty() {
        CoroutineScope(Dispatchers.Main).launch {

            //Arrange
            val subtask = SubTask("test", 1)

            //Act
            subtaskRepository.createSubtask(task, subtask)

            //Assert
            val subtasks = subtaskRepository.readAll(task)
            assertEquals(1, subtasks.size)
            assertEquals(subtask, subtasks[0])

        }
    }
    @Test
    fun createSubtaskSecond() {
        CoroutineScope(Dispatchers.Main).launch {

            //Arrange
            subtaskRepository.createSubtask(task, SubTask("test", 1))

            //Act
            subtaskRepository.createSubtask(task, SubTask("test", 2))

            //Assert
            val subtasks = subtaskRepository.readAll(task)
            assertEquals(2, subtasks.size)
        }
    }
    @Test
    fun readAll() {
        CoroutineScope(Dispatchers.Main).launch {
            //Arrange
            for (i in 1..5) {
                subtaskRepository.createSubtask(task, SubTask("test", i))
            }

            //Act
            val subtasks = subtaskRepository.readAll(task)

            //Assert
            assertEquals(5, subtasks.size)
            for (i in 1..5) {
                assertEquals(i, subtasks[i - 1].id)
            }
        }
    }


    @Test
    fun update() {
        CoroutineScope(Dispatchers.Main).launch {

            //Arrange
            val subtask = subtaskRepository.createSubtask(task, SubTask("EditTask", 0))
            val edited = subtask.copy(title = "NewItem")

            //Act
            subtaskRepository.update(task, edited)
            val updatedSubtask = subtaskRepository.readAll(task)[0]

            //Assert
            assertNotEquals(updatedSubtask.title, subtask.title)
            assertEquals(edited, updatedSubtask)
        }
    }

    @Test
    fun delete() {
        CoroutineScope(Dispatchers.Main).launch {

            //Arrange
            val subtask = subtaskRepository.createSubtask(task, SubTask("EditTask", 0))
            assertEquals(1, subtaskRepository.readAll(task).size)

            //Act
            subtaskRepository.delete(task, subtask)

            //Assert
            assertEquals(0, subtaskRepository.readAll(task).size)
        }
    }
}