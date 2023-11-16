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
    private var _UIState = MutableStateFlow(listOf(Task()))
    private var taskList : MutableList<Task> = emptyList<Task>().toMutableList()
    init {

        viewModelScope.launch(Dispatchers.IO) {
            for(i in 1.. 20) {
                if (i % 2 != 0) {
                    addTask(i, "Task: $i", LocalDateTime.now(), "HIGH", false)
                } else {
                    addTask(i, "Task: $i", LocalDateTime.now(), "LOW", false)
                }
            }
            taskRepository.readAll().forEach { task -> taskList.add(task)
            }
        }
    }

    val UIState : StateFlow<List<Task>> = MutableStateFlow(taskList)

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
        val tmpTask2 = taskRepository.createTask(tmpTask)
        if (id == 1) {
            for (i in 1 .. 3) {
                val subtask = SubTask("subtask $i")
                subtaskRepository.createSubtask(tmpTask, subtask)
            }
        }
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

    fun getSubtasks(currentTask: Task): MutableList<SubTask> {
        return subtaskRepository.readAll(currentTask).toMutableList();
    }

    fun getTask(taskId: Int): Task {
        val text = taskRepository.read(taskId)
        return text ?:Task()
    }

    fun removeSubtask(task: Task, subtask: SubTask) {
        subtaskRepository.delete(task, subtask)
    }
}