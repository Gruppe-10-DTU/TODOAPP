package com.gruppe11.todoApp.repository

import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import javax.inject.Inject

class SubtaskRepositoryImpl @Inject constructor() : ISubtaskRepository{

    private var map: MutableMap<Int, MutableList<SubTask>> = HashMap()
    override fun createSubtask(task: Task, subtask: SubTask): SubTask {
        val list : MutableList<SubTask> = map[task.id] ?: ArrayList();
        subtask.id = list.size+1;
        list.add(subtask);
        return subtask;
    }

    override fun readAll(task: Task): List<SubTask> {
        return map[task.id] ?: ArrayList()
    }

    override fun update(task: Task, subtask: SubTask): SubTask {
        TODO("Not yet implemented")

    }

    override fun delete(task: Task, subtask: SubTask) {
        TODO("Not yet implemented")
    }

}