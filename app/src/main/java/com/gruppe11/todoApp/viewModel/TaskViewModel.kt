package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
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

@HiltViewModel
class TaskViewModel @Inject constructor (
    private val taskRepository : ITaskRepository,
    private val subtaskRepository: ISubtaskRepository
) : ViewModel() {
    private var _UIState = MutableStateFlow(listOf(Task()))
    val UIState : StateFlow<List<Task>> = _UIState.asStateFlow()
    private var taskList : MutableList<Task> = emptyList<Task>().toMutableList()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.readAll().forEach { task -> taskList.add(task) }

        }
    }
    @SuppressLint("NewApi")
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return taskRepository.readAll().filter{it.completion!!.dayOfMonth == date.dayOfMonth}
    }
    fun addTask(id: Int, title: String, completion: LocalDateTime, Prio: String, isCompleted: Boolean){
        val tmpTask = Task()
        let {
            tmpTask.id = id
            tmpTask.title=title
            tmpTask.completion = completion
            tmpTask.priority = fromString(Prio)
            tmpTask.isCompleted = isCompleted
        }
        taskRepository.createTask(tmpTask)
        _UIState.value = taskRepository.readAll()
    }

    fun getStaticSubtasks() : List<SubTask> {
        val test = ArrayList<SubTask>()
        test.add(SubTask("Subtask 1"))
        test.add(SubTask("Subtask 2"))

        return test
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
        task.isCompleted = !task.isCompleted
        taskRepository.update(task)
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

    fun getSubtasks(currentTask: Task): List<SubTask> {
        return subtaskRepository.readAll(currentTask);
    }

    fun getTask(taskId: Int): Task {
        return UIState.value.find{ task -> task.id == taskId }?:Task()
    }
}