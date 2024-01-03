package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Tag
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.fromString
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.ITaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (
    private val taskRepository : ITaskRepository,
    private val subtaskRepository: ISubtaskRepository
) : ViewModel() {
    private var _TaskState = MutableStateFlow(taskRepository.readAll())
    val TaskState: StateFlow<List<Task>> get() = _TaskState
    private var _DaysMap = MutableStateFlow(emptyMap<LocalDate,Float>())
    val DaysMap : StateFlow<Map<LocalDate,Float>> get() = _DaysMap

    private val _filterTags = getFilterTags().toMutableSet()
    var completeFilter = mutableStateOf(false)
    var incompleteFilter = mutableStateOf(false)

    val tags: Set<Tag>
        get() = _filterTags

    private fun getFilterTags() = emptySet<Tag>()

    init {
        _DaysMap.value = generateMapOfDays()
    }
    @SuppressLint("NewApi")
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return _TaskState.value.filter {it.deadline.toLocalDate() == date.toLocalDate()}
    }

    fun updateTask(id: Int, title: String, deadline: LocalDateTime, Prio: String, isCompleted: Boolean, subtaskList: List<SubTask>){
        val task = taskRepository.update(Task(id = id,title = title,deadline = deadline, priority = fromString(Prio), isCompleted = isCompleted))
//        addSubtasks(task, subtaskList)
        _TaskState.value = taskRepository.readAll()
        _DaysMap.value = generateMapOfDays()

    }

    fun addTask(id: Int, title: String, deadline: LocalDateTime, Prio: String, isCompleted: Boolean, subtaskList: List<SubTask>){
        val task = taskRepository.createTask(Task(id = id,title = title,deadline = deadline, priority = fromString(Prio), isCompleted = isCompleted))
        addSubtasks(task, subtaskList)
        _TaskState.value = taskRepository.readAll()
        _DaysMap.value = generateMapOfDays()

    }

    fun removeTask(task: Task){
        taskRepository.delete(task)
        _TaskState.value = taskRepository.readAll()
    }

    fun getTaskList(): List<Task> {
        return TaskState.value
    }
    @SuppressLint("NewApi")
    fun generateMapOfDays(): MutableMap<LocalDate, Float> {
        val toReturn : MutableMap<LocalDate,Float> = emptyMap<LocalDate, Float>().toMutableMap()
        var tmp = LocalDateTime.now().minusDays(30)
        for(i in 0 .. 60){
            toReturn[tmp.toLocalDate()] = countTaskCompletionsByDay(tmp)
            tmp = tmp.plusDays(1)
        }
       return toReturn
    }

    @SuppressLint("NewApi")
    fun changeTaskCompletion(task: Task){
        taskRepository.update(task.copy(isCompleted = !task.isCompleted))
        _TaskState.update{it.map {
            if(it.id == task.id) {it.copy(isCompleted = !it.isCompleted)}
            else {it}
            }
        }
        _DaysMap.value = _DaysMap.value.toMutableMap().apply{this[task.deadline.toLocalDate()] = countTaskCompletionsByDay(task.deadline) }
    }

    @SuppressLint("NewApi")
    fun countTaskCompletionsByDay(date: LocalDateTime): Float {
        if(_TaskState.value.isEmpty()){
            return 0f
        }
        val totComp = _TaskState.value.filter { it.deadline.toLocalDate() == date.toLocalDate() }.count { it.isCompleted }
        val totTask = _TaskState.value.count{ it.deadline.toLocalDate() == date.toLocalDate() }
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
        return TaskState.value.find{ task -> task.id == taskId }!!
    }

    fun addSubtasks(task: Task, subtasks: List<SubTask>){
        val existingSubtasks = subtaskRepository.readAll(task)
        val newSubtasks = subtasks.filterNot { existingSubtasks.contains(it) }
        for (subtask in newSubtasks) {
            subtaskRepository.createSubtask(task, subtask)
        }
    }
}