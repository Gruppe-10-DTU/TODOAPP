package com.gruppe11.todoApp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.Priority
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val taskRepository: ITaskRepository,
    private val subtaskRepository: ISubtaskRepository,
    private val timeSlotRepository: ITimeSlotRepository
) : ViewModel() {
    private val _editingTask = MutableStateFlow<Task>(
        Task(
            -1, "", Priority.MEDIUM, LocalDateTime.now(), false,
            emptyList()
        )
    )
    val editingTask = _editingTask.asStateFlow()


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
            val task = taskRepository.read(taskId)
            if (task != null) {
                _editingTask.emit(task)
            }
        }
    }


    fun addTask(task: Task, subtaskList: List<SubTask>): Task {
        var tmpTask1 = task
        viewModelScope.launch {
            val tmpTask2 = taskRepository.createTask(task)
            if (subtaskList.isNotEmpty()) {
                addSubtasks(tmpTask2, subtaskList)
            }
            tmpTask1 = tmpTask2
        }
        return tmpTask1
    }

    private fun addSubtasks(task: Task, subtasks: List<SubTask>) {
        viewModelScope.launch {
            val existingSubtasks = subtaskRepository.readAll(task)
            val newSubtasks = subtasks.filterNot { existingSubtasks.contains(it) }
            for (subtask in newSubtasks) {
                subtaskRepository.createSubtask(task, subtask)
            }
            taskRepository.read(task.id)
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

    fun editTitle(title: String) {
        _editingTask.update { task: Task -> task.copy(title = title) }
    }

    fun editPriority(priority: Priority) {
        _editingTask.update { task: Task -> task.copy(priority = priority) }
    }

    fun editDeadline(deadline: LocalDateTime) {
        _editingTask.update { task: Task -> task.copy(deadline = deadline) }
    }

    fun editSubtasks(subtaskList: List<SubTask>) {
    }

    fun editSubtask(index: Int, newSubtaskTitle: String, newSubtask: SubTask) {
        _editingTask.update { task ->
            task.copy(
                subtasks = task.subtasks.toMutableList().apply {
                    this[index] = newSubtask.copy(title = newSubtaskTitle)
                }
            )
        }
    }

    fun addSubtask(subtaskTitle: String) {
        val subtask = _editingTask.value.subtasks.toMutableList()
            .apply { add(SubTask(subtaskTitle, 0, false)) }
        _editingTask.update { task: Task -> task.copy(subtasks = subtask) }
    }

    fun removeSubtask(subtask: SubTask) {
        val subTaskList = _editingTask.value.subtasks
        _editingTask.update { task: Task -> task.copy(subtasks = subTaskList.filter { it != subtask }) }

    }

    suspend fun submitTask() {
        if (_editingTask.value.id > 0) {
            taskRepository.update(_editingTask.value)
            val existingSubtasks = subtaskRepository.readAll(_editingTask.value)
            _editingTask.value.subtasks.forEach{
                subTask ->
                if(subTask.id > 0){
                    subtaskRepository.update(_editingTask.value, subTask)
                } else{
                    subtaskRepository.createSubtask(_editingTask.value, subTask)
                }
            }

        } else {
            val task = taskRepository.createTask(_editingTask.value)
            if (_editingTask.value.title.isNotEmpty() && _editingTask.value.subtasks.isNotEmpty()) {
                addSubtasks(task, _editingTask.value.subtasks)
            }
//                if (selectedTimeSlot.name != "") {
//                    viewModel.addToTimeslot(selectedTimeSlot.copy(tasks = selectedTimeSlot.tasks + taskHolder))
//                }
//                scope.launch {
//                    snackbarHostState.showSnackbar("Task created")
        }
    }
}

