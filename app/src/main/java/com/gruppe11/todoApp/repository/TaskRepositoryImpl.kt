package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.network.TodoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor() : ITaskRepository  {

    private val tasks: MutableStateFlow<List<Task>> = MutableStateFlow(emptyList())

    override suspend fun createTask(task: Task): Task {
        val newTask = TodoApi.taskService.createTask(task)
        tasks.update { tasks -> tasks.toMutableList().apply { add(newTask) } }
        return newTask
    }

    override suspend fun refresh() {
        val tasksFromApi = TodoApi.taskService.readAll()
        tasks.emit(tasksFromApi)
    }
    override suspend fun read(id: Int): Task? {
        val task = TodoApi.taskService.read(id)
        if (task != null) {
            val index: Int = tasks.value.indexOfFirst { it.id == id }
            if (index >= 0) {
                tasks.update {
                    tasks.value.toMutableList().apply { this[index] = task }
                }
            } else {
                tasks.update { tasks -> tasks.toMutableList().apply { add(task) } }
            }
        }
        return task
    }

    override suspend fun readAll(): Flow<List<Task>> {
        val tasksFromApi = TodoApi.taskService.readAll()
        tasks.emit(tasksFromApi)
        return tasks
    }

    override suspend fun update(task: Task): Task {
        val updatedTask = TodoApi.taskService.update(task.id, task)
        val index: Int = tasks.value.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            tasks.update{
                tasks.value.toMutableList().apply { this[index] = updatedTask }
            }
        }
        return updatedTask
    }

    override suspend fun delete(task: Task) {
        TodoApi.taskService.delete(task.id)
        tasks.update {
            tasks.value.toMutableList().apply { remove(task) }
        }
    }
}