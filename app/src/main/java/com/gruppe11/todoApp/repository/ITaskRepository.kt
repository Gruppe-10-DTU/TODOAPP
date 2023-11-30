package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.Task

interface ITaskRepository {
    fun createTask(task: Task): Task
    fun read(id: Int): Task?
    fun readAll(): List<Task>
    fun update(task: Task): Task
    fun delete(task: Task)
}