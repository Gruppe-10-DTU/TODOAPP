package com.gruppe11.todoApp.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.gruppe11.todoApp.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class TaskRepositoryImpl @Inject constructor() : ITaskRepository  {
    private var id = 1
    private val tasks: MutableStateFlow<List<Task>> = MutableStateFlow(emptyList());

    override fun createTask(task: Task): Task {
        val newTask = task.copy(id = id++)
        tasks.update { tasks -> tasks.toMutableList().apply { add(newTask) } }
        return newTask
    }

    override fun read(id: Int): Task? {
        return tasks.value.find { task: Task -> task.id == id }
    }

    override fun readAll(): Flow<List<Task>> {
        return tasks
    }

    override fun update(task: Task): Task {
        val index: Int = tasks.value.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            tasks.update{
                tasks.value.toMutableList().apply { this[index] = task }
            }
        }

        return task
    }

    override fun delete(task: Task) {
        tasks.update {
            tasks.value.toMutableList().apply { remove(task) }
        }
    }
}