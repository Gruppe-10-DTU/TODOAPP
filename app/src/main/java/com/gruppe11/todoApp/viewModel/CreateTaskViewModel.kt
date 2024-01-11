package com.gruppe11.todoApp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.ITimeSlotRepository
import com.gruppe11.todoApp.ui.screenStates.ExecutionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
            emptyList(),
            null
        )
    )

    val editingTask = _editingTask.asStateFlow()

    val _timeslots: MutableStateFlow<List<TimeSlot>> = MutableStateFlow(emptyList())

    val Timeslots = _timeslots.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.Default) {

            timeSlotRepository.readAll().collect{ timeslots ->
                _timeslots.value = timeslots
            }
        }
    }

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
            try {
                val task = taskRepository.read(taskId)
                if (task != null) {
                    _editingTask.emit(task)
                }
            } catch (e: Exception) {
                Log.d("getTask", e.toString())
            }
        }
    }


    // No exception handling for this function
    // as it is handled in the function that calls it
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

    private fun getTimeSlots() {
        viewModelScope.launch {
            try {
                val flow = timeSlotRepository.readAll()
                flow.collect {
                    _timeslots.value = it
                }
            } catch (e: Exception) {
                Log.d("getTimeSlots", e.toString())
            }
        }
    }

    fun addToTimeslot(timeslot: TimeSlot, task: Task) {
        _editingTask.update { task: Task -> task.copy(timeslot = timeslot) }
        try {
            timeSlotRepository.update(timeslot.copy(tasks = timeslot.tasks.plus(task)))
            _submitState.value = ExecutionState.SUCCESS
        } catch (e: Exception) {
            Log.d("addToTimeslot", e.toString())
            _submitState.value = ExecutionState.ERROR
        }
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

    suspend fun submitTask(): Task? {
        var task: Task? = null
        if (_editingTask.value.id > 0) {
            try {
                task = taskRepository.update(_editingTask.value)
                _editingTask.value.subtasks.forEach { subTask ->
                    if (subTask.id > 0) {
                        subtaskRepository.update(_editingTask.value, subTask)
                    } else {
                        subtaskRepository.createSubtask(_editingTask.value, subTask)
                    }
                }
                _submitState.value = ExecutionState.SUCCESS
            } catch (e: Exception) {
                _submitState.value = ExecutionState.ERROR
            }
        } else {
            try {
                task = taskRepository.createTask(_editingTask.value)
                if (_editingTask.value.title.isNotEmpty() && _editingTask.value.subtasks.isNotEmpty()) {
                    addSubtasks(task, _editingTask.value.subtasks)
                }
                _submitState.value = ExecutionState.SUCCESS
            } catch (e: Exception) {
                _submitState.value = ExecutionState.ERROR
            }
        }
        return task
    }

    fun editTimeslot(timeslot: TimeSlot) {
        _editingTask.update { task: Task -> task.copy(timeslot = timeslot) }
    }

}

