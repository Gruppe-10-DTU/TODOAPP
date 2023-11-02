package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.fromString
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

class TaskViewModel (
    private val taskRepository : ITaskRepository = TaskRepositoryImpl()
) : ViewModel() {
    private var _UIState = MutableStateFlow(listOf(Task()))
    val UIState : StateFlow<List<Task>> = _UIState.asStateFlow()
    @SuppressLint("NewApi")
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return taskRepository.readAll().filter{it.completion!!.dayOfYear == date.dayOfYear}
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
    fun generateMapOfDays(date: LocalDateTime): MutableMap<LocalDateTime,Float>{
        var totComp = 0f
        var totTask = 0f
        var toReturn : MutableMap<LocalDateTime,Float> = emptyMap<LocalDateTime, Float>().toMutableMap()
        var tmp = LocalDateTime.now().minusDays(30)
        for(i in 0 .. 60){
            toReturn[tmp] = countTaskCompletionsByDay(date)
            tmp = tmp.plusDays(1)
        }
        return toReturn
    }
    fun changeTaskCompletion(task: Task){
        task.isCompleted = !task.isCompleted
        taskRepository.update(task)
        _UIState.value = taskRepository.readAll()
    }

    @SuppressLint("NewApi")
    fun countTaskCompletionsByDay(date: LocalDateTime): Float {
        var totComp = 0f
        var totTask = 0f
        val completedTasks = taskRepository.readAll().toList().filter{it.completion!!.dayOfMonth == date.dayOfMonth}
        completedTasks.forEach { Task -> if(Task.isCompleted){totComp++} else{totTask++} }
        return totComp/totTask
    }
}