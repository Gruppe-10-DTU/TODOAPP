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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val taskRepository: ITaskRepository,
    private val subtaskRepository: ISubtaskRepository,
    private val timeSlotRepository: ITimeSlotRepository
) : ViewModel() {
    private val _editingTask = MutableStateFlow<Task?>(null)
    val editingTask = _editingTask.asStateFlow()
    private val _subtasks = MutableStateFlow<List<SubTask>>(emptyList())
    val subtasks = _subtasks.asStateFlow()
    suspend fun getSubtasks(currentTask: Task): List<SubTask> {
        return subtaskRepository.readAll(currentTask)
    }


    fun removeSubtask(task: Task, subTask: SubTask) {
        viewModelScope.launch {
            subtaskRepository.delete(task, subTask)
        }
    }

    fun getTask(taskId: Int) {
        viewModelScope.launch {
            _editingTask.value = taskRepository.read(taskId)
        }
    }

    fun updateSubTask(subTask: SubTask) {
        viewModelScope.launch {
            subtaskRepository
        }
    }

    fun addTask(task: Task, subtaskList: List<SubTask>): Task {
        var tmpTask1 = task
        viewModelScope.launch {
            val tmpTask2 = taskRepository.createTask(task)
            if (subtaskList.isNotEmpty()){
                addSubtasks(tmpTask2, subtaskList)
            }
            tmpTask1 = tmpTask2
        }
        return tmpTask1
    }

    fun addSubtasks(task: Task, subtasks: List<SubTask>) {
        viewModelScope.launch {
            val existingSubtasks = subtaskRepository.readAll(task)
            val newSubtasks = subtasks.filterNot { existingSubtasks.contains(it) }
            for (subtask in newSubtasks) {
                subtaskRepository.createSubtask(task, subtask)
            }
            val updatedSubtasks = subtaskRepository.readAll(task)
            _subtasks.value = updatedSubtasks
        }
    }

    fun updateTask(task: Task, subtaskList: List<SubTask>) {
        viewModelScope.launch {
            taskRepository.update(task)
        }
    }

    fun getTimeSlots(): Flow<List<TimeSlot>> {
        return timeSlotRepository.readAll()
    }

    fun addToTimeslot(timeslot: TimeSlot) {
        timeSlotRepository.update(timeslot)
    }


}
