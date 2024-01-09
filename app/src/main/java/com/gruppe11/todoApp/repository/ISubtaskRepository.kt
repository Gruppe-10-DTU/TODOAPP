package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task

interface ISubtaskRepository {
    suspend fun createSubtask(task: Task, subtask: SubTask): SubTask
    suspend fun readAll(task: Task): List<SubTask>
    suspend fun update(task: Task, subtask: SubTask): SubTask?
    suspend fun delete(task: Task, subtask: SubTask)
}