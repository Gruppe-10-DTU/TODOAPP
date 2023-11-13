package com.gruppe11.todoApp.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.gruppe11.todoApp.model.Task
import java.time.LocalDateTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class TaskRepositoryImpl @Inject constructor() : ITaskRepository  {
    private var id = 1
    private val tasks: MutableList<Task>
    init {
        tasks = ArrayList()
        for(i in 1.. 20) {
            if (i % 2 != 0) {
                tasks.add(Task(i, "Task: $i", LocalDateTime.now(), "HIGH", false))
            } else {
                tasks.add(Task(i, "Task: $i", LocalDateTime.now(), "LOW", false))
            }
            id++
        }
    }

    override fun createTask(task: Task): Task {
        task.id = id++
        tasks.add(task)
        return task
    }

    override fun read(id: Int): Task? {
        return tasks
            .stream()
            .filter { e: Task? -> e!!.id == id }
            .findFirst()
            .orElse(null)
    }

    override fun readAll(): List<Task> {
        return tasks
    }

    override fun update(task: Task): Task {
        val index = tasks.indexOfFirst { e: Task? -> e!!.id == task.id }
        tasks[index] = task
        return task
    }

    override fun delete(task: Task) {
        tasks.removeIf { e: Task? -> e!!.id == task.id }
    }
}