package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.Task

interface ITaskRepository {
    suspend fun createTask(task: Task): Task
    suspend fun read(id: Int): Task?
    suspend fun readAll(): List<Task>
    suspend fun update(task: Task): Task
    suspend fun delete(task: Task)
}