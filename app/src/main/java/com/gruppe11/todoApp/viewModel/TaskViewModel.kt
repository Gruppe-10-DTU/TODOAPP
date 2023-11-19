package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.fromString
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.ITaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import javax.inject.Inject
@HiltViewModel
class TaskViewModel @Inject constructor (
    private val taskRepository : ITaskRepository,
    private val subtaskRepository: ISubtaskRepository
) : ViewModel() {
    private var _UIState = MutableStateFlow(taskRepository.readAll())
    val UIState : StateFlow<List<Task>> get() = _UIState

    private var _DaysMap = MutableStateFlow(emptyMap<LocalDateTime,Float>())
    val DaysMap : StateFlow<Map<LocalDateTime,Float>> get() = _DaysMap
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
        return UIState.value
    }

    @SuppressLint("NewApi")
    fun generateMapOfDays(date: LocalDateTime): MutableMap<LocalDateTime, Float> {
        val toReturn : MutableMap<LocalDateTime,Float> = emptyMap<LocalDateTime, Float>().toMutableMap()
        var tmp = date.minusDays(30)
        for(i in 0 .. 60){
            toReturn[tmp] = countTaskCompletionsByDay(tmp)
            tmp = tmp.plusDays(1)
        }
       return toReturn
    }

    @SuppressLint("NewApi")
    fun changeTaskCompletion(task: Task){
        taskRepository.update(task.copy(isCompleted = !task.isCompleted))
        _UIState.value = taskRepository.readAll()
        updateDaysMap(task.deadline)
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
    fun getTask(taskId: Int): Task {
        return UIState.value.find{ task -> task.id == taskId }!!
    }

    fun updateDaysMap(date: LocalDateTime){
        _DaysMap.value = generateMapOfDays(date)
    }

}