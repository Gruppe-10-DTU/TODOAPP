package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.network.TodoApi
import javax.inject.Inject

class SubtaskRepositoryImpl @Inject constructor() : ISubtaskRepository{

    override suspend fun createSubtask(task: Task, subtask: SubTask): SubTask {
        return TodoApi.subtaskService.createSubtask(task.id, subtask)
    }

    override suspend fun readAll(task: Task): List<SubTask> {
        return TodoApi.subtaskService.readAll(task.id)
    }

    override suspend fun update(task: Task, subtask: SubTask): SubTask? {
        return TodoApi.subtaskService.update(task.id, subtask.id, subtask)
    }

    override suspend fun delete(task: Task, subtask: SubTask) {
        return TodoApi.subtaskService.delete(task.id, subtask.id)
    }
}