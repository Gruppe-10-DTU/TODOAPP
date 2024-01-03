package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Tag
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.fromString
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.ITaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (
    private val taskRepository : ITaskRepository,
    private val subtaskRepository: ISubtaskRepository
) : ViewModel() {
    private var _TaskState = MutableStateFlow<List<Task>>(emptyList())
    val TaskState: StateFlow<List<Task>> get() = _TaskState.asStateFlow()

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
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.readAll().collect{
                tasks -> _TaskState.value = tasks
            }
        }
    }
    @SuppressLint("NewApi")
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return _TaskState.value.filter {it.deadline.toLocalDate() == date.toLocalDate()}
    }

    fun addTask(id: Int, title: String, deadline: LocalDateTime, Prio: String, isCompleted: Boolean, subtaskList: List<SubTask>){
        val task = taskRepository.createTask(Task(id = id,title = title,deadline = deadline, priority = fromString(Prio), isCompleted = isCompleted))
        addSubtasks(task, subtaskList)
        val newDays = generateMapOfDays()
        if (_DaysMap.compareAndSet(newDays, newDays)) {
            println("Updated")
        } else {
            println("Not updated")
        }
    }

    fun removeTask(task: Task){
        taskRepository.delete(task)
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
        val completed = countTaskCompletionsByDay(task.deadline);
        println("Completed: " + completed)
        _DaysMap.update {
            _DaysMap.value.toMutableMap().apply{this.put(task.deadline.toLocalDate(), completed) }
        }
    }

    @SuppressLint("NewApi")
    fun countTaskCompletionsByDay(date: LocalDateTime): Float {
        if(_TaskState.value.isEmpty()){
            return 0f
        }
        val totComp = _TaskState.value.filter { it.deadline.toLocalDate() == date.toLocalDate() }.count { it.isCompleted }
        val totTask = _TaskState.value.count{ it.deadline.toLocalDate() == date.toLocalDate() }
        println("Completed days: " + totComp + " and total days: " + totTask)
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
        return _TaskState.value.find{ task -> task.id == taskId }!!
    }

    fun addSubtasks(task: Task, subtasks: List<SubTask>){
        val existingSubtasks = subtaskRepository.readAll(task)
        val newSubtasks = subtasks.filterNot { existingSubtasks.contains(it) }
        for (subtask in newSubtasks) {
            subtaskRepository.createSubtask(task, subtask)
        }
    }
}