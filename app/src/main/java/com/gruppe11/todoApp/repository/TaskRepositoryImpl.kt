package com.gruppe11.todoApp.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.gruppe11.todoApp.model.Task
import java.time.LocalDateTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class TaskRepositoryImpl @Inject constructor() : ITaskRepository  {
    private var id = 1
    private val tasks: MutableList<Task> = ArrayList();

    override fun createTask(task: Task): Task {
        tasks.add(task.copy(id = ++id))
        return task
    }

    override fun read(id: Int): Task? {
        return tasks.find { task: Task -> task.id == id }
    }

    override fun readAll(): List<Task> {
        return tasks
    }

    override fun update(task: Task): Task {
        val index = tasks.indexOfFirst { e -> e.id == task.id }
        if (index >= 0) {
            tasks[index] = task
        }

        return task
    }

    override fun delete(task: Task) {
        tasks.removeIf { e -> e.id == task.id }
    }
}