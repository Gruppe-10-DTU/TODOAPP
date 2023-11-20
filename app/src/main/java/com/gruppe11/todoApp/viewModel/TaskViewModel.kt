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
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class TaskViewModel @Inject constructor (
    private val taskRepository : ITaskRepository,
    private val subtaskRepository: ISubtaskRepository
) : ViewModel() {
    private var _UIState = MutableStateFlow(taskRepository.readAll())
    val UIState : StateFlow<List<Task>> get() = _UIState

    private var _DaysMap = MutableStateFlow(emptyMap<LocalDate,Float>())
    val DaysMap : StateFlow<Map<LocalDate,Float>> get() = _DaysMap
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

    @SuppressLint("NewApi")
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return _UIState.value.filter {it.deadline.dayOfYear == date.dayOfYear}
    }

    fun addTask(id: Int, title: String, deadline: LocalDateTime, Prio: String, isCompleted: Boolean){
//        val tmpTask = Task()
//        let {
//            tmpTask.id = id
//            tmpTask.title=title
//            tmpTask.completion = completion
//            tmpTask.priority = fromString(Prio)
//            tmpTask.isCompleted = isCompleted
//        }
        taskRepository.createTask(Task(id = id,title = title,deadline = deadline, priority = fromString(Prio), isCompleted = isCompleted))
        _DaysMap.value = generateMapOfDays(date = deadline)
    }

    fun removeTask(task: Task){
        taskRepository.delete(task)
    }

    fun getTaskList(): List<Task> {
        return UIState.value
    }
    @SuppressLint("NewApi")
    fun generateMapOfDays(date: LocalDateTime): MutableMap<LocalDate, Float> {
        val toReturn : MutableMap<LocalDate,Float> = emptyMap<LocalDate, Float>().toMutableMap()
        var tmp = date.minusDays(30)
        for(i in 0 .. 60){
            toReturn[tmp.toLocalDate()] = countTaskCompletionsByDay(tmp)
            tmp = tmp.plusDays(1)
        }
       return toReturn
    }

    @SuppressLint("NewApi")
    fun changeTaskCompletion(task: Task){
        taskRepository.update(task.copy(isCompleted = !task.isCompleted))
        _UIState.value = taskRepository.readAll()
        _DaysMap.value = _DaysMap.value.toMutableMap().apply{ this[task.deadline.toLocalDate()] = countTaskCompletionsByDay(task.deadline) }
    }

    @SuppressLint("NewApi")
    fun countTaskCompletionsByDay(date: LocalDateTime): Float {
        val totComp = _UIState.value.filter { it.deadline.dayOfYear == date.dayOfYear }.count { completed -> completed.isCompleted}
        val totTask = _UIState.value.count{ it.deadline.dayOfYear == date.dayOfYear }
        if (totTask == 0) return 0f
        return totComp/totTask.toFloat()
    }

    fun getSubtasks(currentTask: Task): List<SubTask> {
        return subtaskRepository.readAll(currentTask)
    }
    fun removeSubtask(task: Task, subTask: SubTask){
        subtaskRepository.delete(task,subTask)
    }
    fun getTask(taskId: Int): Task {
        return UIState.value.find{ task -> task.id == taskId }!!
    }
}