package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.fromString
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.ITaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class TaskViewModel @Inject constructor (
    private val taskRepository : ITaskRepository,
    private val subtaskRepository: ISubtaskRepository
) : ViewModel() {


    init {

        viewModelScope.launch(Dispatchers.IO) {
            for(i in 1.. 20) {
                if (i % 2 != 0) {
                    addTask(i, "Task: $i", LocalDateTime.now(), "HIGH", false)
                } else {
                    addTask(i, "Task: $i", LocalDateTime.now(), "LOW", false)
                }
            }

        }
    }

    private var _UIState = MutableStateFlow(taskRepository.readAll())
    val UIState = _UIState.asStateFlow()

    @SuppressLint("NewApi")
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return taskRepository.readAll().filter{it.completion!!.dayOfMonth == date.dayOfMonth}
    }
    fun addTask(id: Int, title: String, completion: LocalDateTime, Prio: String, isCompleted: Boolean){
        val task: Task = taskRepository.createTask(Task(id = id,title = title,completion = completion, priority = fromString(Prio), isCompleted = isCompleted))
        _UIState.value = taskRepository.readAll()
    }

    fun removeTask(task: Task){
        taskRepository.delete(task)
        _UIState.value = taskRepository.readAll()
    }

    fun getTaskList(): List<Task> {
        return taskRepository.readAll()
    }

    @SuppressLint("NewApi")
    fun generateListOfDaysInMonth(Date: LocalDateTime): MutableList<Int>{
        val daysInMonth = YearMonth.of(Date.year,Date.month).lengthOfMonth()
        val daysList = mutableListOf<Int>()
        for(i in 1..daysInMonth){
            daysList.add(i)
        }
        return daysList
    }
    fun changeTaskCompletion(task: Task){
        taskRepository.update(task.copy(isCompleted = !task.isCompleted))
        _UIState.value = taskRepository.readAll()
    }

    @SuppressLint("NewApi")
    fun countTaskCompletionsByDay(date: LocalDateTime): MutableList<Int> {
        val ctc = generateListOfDaysInMonth(date)
        for (task in taskRepository.readAll()) {
            if (task.completion?.monthValue == date.monthValue) {
                val dayOfMonth = task.completion!!.dayOfMonth
                if (!task.isCompleted) {
                    ctc[dayOfMonth] = ctc[dayOfMonth] + 1
                }
            }
        }
        return ctc
    }

    fun getSubtasks(currentTask: Task): MutableList<SubTask> {
        return subtaskRepository.readAll(currentTask).toMutableList();
    }

    fun getTask(taskId: Int): Task? {
        return taskRepository.read(taskId)
    }

    fun removeSubtask(task: Task, subtask: SubTask) {
        subtaskRepository.delete(task, subtask)
    }
}