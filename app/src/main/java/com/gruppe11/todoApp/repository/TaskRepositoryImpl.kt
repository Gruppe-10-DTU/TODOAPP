package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.Task

class TaskRepositoryImpl : ITaskRepository {
    private var id = 0
    private val tasks: MutableList<Task?> = ArrayList()
    override fun createTask(task: Task): Task {
        task!!.id = id++
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

    override fun readAll(): List<Task?> {
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