package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.network.TodoApi
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor() : ITaskRepository  {

    override suspend fun createTask(task: Task): Task {
        return TodoApi.taskServiceImpl.createTask(task)
    }

    override suspend fun read(id: Int): Task? {
        return TodoApi.taskServiceImpl.read(id)
    }

    override suspend fun readAll(): List<Task> {
        return TodoApi.taskServiceImpl.readAll()
    }

    override suspend fun update(task: Task): Task {
        return TodoApi.taskServiceImpl.update(task.id, task)
    }

    override suspend fun delete(task: Task) {
        return TodoApi.taskServiceImpl.delete(task.id)
    }
}