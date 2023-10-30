package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.fromString
import com.gruppe11.todoApp.repository.ITaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (
    private val taskRepository : ITaskRepository
) : ViewModel() {
    private val _UIState = MutableStateFlow(Task())
    val UIState : StateFlow<Task> = _UIState.asStateFlow()

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
    }

    fun getStaticSubtasks() : List<SubTask> {
        val test = ArrayList<SubTask>()
        test.add(SubTask("Subtask 1"))
        test.add(SubTask("Subtask 2"))

        return test
    }

    fun removeTask(task: Task){
        taskRepository.delete(task)
    }

    fun getTaskList(): List<Task> {
        return taskRepository.readAll()
    }

    @SuppressLint("NewApi")
    fun generateListOfDaysLeftInMonth(Date: LocalDateTime): MutableList<Int>{
        val daysInMonth = YearMonth.of(Date.year,Date.month).lengthOfMonth()
        val daysList = mutableListOf<Int>()
        for(i in 1..daysInMonth){
            if(i - LocalDateTime.now().dayOfMonth >= 0) daysList.add(i)
        }
        return daysList
    }

    fun changeTaskCompletion(task: Task){
        task.isCompleted = !task.isCompleted
        taskRepository.update(task)
    }
}