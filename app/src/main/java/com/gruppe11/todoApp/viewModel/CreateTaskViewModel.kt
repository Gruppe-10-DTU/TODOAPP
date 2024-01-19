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
import com.gruppe11.todoApp.ui.screenStates.CreateTaskScreenState
import com.gruppe11.todoApp.ui.screenStates.ExecutionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
    private val _UIState = MutableStateFlow(
        CreateTaskScreenState(
            openWithSchedule = false
        )
    )

    private val _editingTask = MutableStateFlow<Task>(
        Task(
            -1, "", Priority.MEDIUM, LocalDateTime.now(), false,
            emptyList(),
            null
        )
    )

    val editingTask = _editingTask.asStateFlow()

    private val _timeslots: MutableStateFlow<List<TimeSlot>> = MutableStateFlow(emptyList())
    val Timeslots = _timeslots.asStateFlow()

    private val _submitState = MutableStateFlow(ExecutionState.RUNNING)
    val submitState = _submitState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                timeSlotRepository.readAll()
                    .collect { timeslots ->
                        _timeslots.value = timeslots
                    }
            } catch (e: Exception) {
                Log.d("init", e.toString())
            }
        }
    }

    fun getTask(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val task = taskRepository.read(taskId)
                if (task != null) {
                    _editingTask.emit(task)
                    if (task.timeslot != null) {
                        openWithSchedule(true)
                    }
                }
            } catch (e: Exception) {
                Log.d("getTask", e.toString())
            }
        }
    }

    // No exception handling for this function
    // as it is handled in the function that calls it
    private fun addSubtasks(task: Task, subtasks: List<SubTask>) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingSubtasks = subtaskRepository.readAll(task)
            val newSubtasks = subtasks.filterNot { existingSubtasks.contains(it) }
            for (subtask in newSubtasks) {
                subtaskRepository.createSubtask(task, subtask)
            }
            taskRepository.read(task.id)
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

        timeSlotRepository.readAll().catch { e -> Log.d("submitTask", e.toString()) }

        return task
    }


    fun editTimeslot(timeslot: TimeSlot?) {
        _editingTask.update { task: Task -> task.copy(timeslot = timeslot) }
    }

    fun openWithSchedule(bool: Boolean) {
        _UIState.update { it.copy(openWithSchedule = bool) }
    }

    fun getScheduleState(): Boolean {
        return _UIState.value.openWithSchedule
    }

}

