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
class TaskViewModel @Inject constructor(
    private val taskRepository : ITaskRepository,
    private val taskList: MutableList<Task> =  taskRepository.readAll().toMutableList()
) : ViewModel() {
    private var _UIState = MutableStateFlow(taskRepository.readAll())
    val UIState : StateFlow<List<Task>> = _UIState.asStateFlow()
    @SuppressLint("NewApi")
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return taskRepository.readAll().filter{it.completion!!.dayOfMonth == date.dayOfMonth}
    }
    fun addTask(id: Int, title: String, completion: LocalDateTime, Prio: String, isCompleted: Boolean){
//        val tmpTask = Task()
//        let {
//            tmpTask.id = id
//            tmpTask.title=title
//            tmpTask.completion = completion
//            tmpTask.priority = fromString(Prio)
//            tmpTask.isCompleted = isCompleted
//        }
        taskRepository.createTask(Task(id = id,title = title,completion = completion, priority = fromString(Prio), isCompleted = isCompleted))
        taskList.add(tmpTask.id,tmpTask)
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
        taskList.removeAt(task.id)
        _UIState.value = taskRepository.readAll()
    }

    fun getTaskList(): List<Task> {
        return taskList
    }

    @SuppressLint("NewApi")
    fun generateMapOfDays(date: LocalDateTime): MutableMap<LocalDateTime,Float>{
        var toReturn : MutableMap<LocalDateTime,Float> = emptyMap<LocalDateTime, Float>().toMutableMap()
        var tmp = date.minusDays(30)
        for(i in 0 .. 60){
            toReturn[tmp] = countTaskCompletionsByDay(tmp)
            tmp = tmp.plusDays(1)
        }
        return toReturn
    }
    fun changeTaskCompletion(task: Task){
        task.isCompleted = !task.isCompleted
        taskList.set(task.id,task)
        taskRepository.update(task.copy(isCompleted = !task.isCompleted))
        _UIState.value = taskRepository.readAll()
    }

    @SuppressLint("NewApi")
    fun countTaskCompletionsByDay(date: LocalDateTime): Float {
        var totComp = 0f
        var totTask = 0f
        val completedTasks = taskList.filter{it.completion!!.dayOfYear == date.dayOfYear}
        completedTasks.forEach { Task ->
            if(Task.isCompleted){totComp++}
            totTask++
        }
        if(totTask > 0) return totComp/totTask
        return 0f
    }
}