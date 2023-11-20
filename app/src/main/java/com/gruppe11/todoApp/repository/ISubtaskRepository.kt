package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task

interface ISubtaskRepository {
    fun createSubtask(task: Task, subtask: SubTask): SubTask
    fun readAll(task: Task): List<SubTask>
    fun update(task: Task, subtask: SubTask): SubTask?
    fun delete(task: Task, subtask: SubTask)
}