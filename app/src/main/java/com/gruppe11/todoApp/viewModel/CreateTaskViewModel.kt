package com.gruppe11.todoApp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.ITimeSlotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor (
    private val taskRepository : ITaskRepository,
    private val subtaskRepository: ISubtaskRepository,
    private val timeSlotRepository: ITimeSlotRepository
) : ViewModel() {
    fun getSubtasks(currentTask: Task): List<SubTask> {
        return subtaskRepository.readAll(currentTask)
    }
    fun removeSubtask(task: Task, subTask: SubTask){
        subtaskRepository.delete(task,subTask)
    }
    fun getTask(taskId: Int): Task? {
        return taskRepository.read(taskId)
    }

    fun addSubtasks(task: Task, subtasks: List<SubTask>){
        val existingSubtasks = subtaskRepository.readAll(task)
        val newSubtasks = subtasks.filterNot { existingSubtasks.contains(it) }
        for (subtask in newSubtasks) {
            subtaskRepository.createSubtask(task, subtask)
        }
    }
    fun updateTask(task: Task){
        taskRepository.update(task)
    }
    fun addTask(task: Task, subtaskList: List<SubTask>){
        viewModelScope.launch {
            val tmpTask = taskRepository.createTask(task)
            addSubtasks(tmpTask, subtaskList)

        }
    }
    fun getTimeSlots(): Flow<List<TimeSlot>> {
        return timeSlotRepository.readAll()
    }
}
