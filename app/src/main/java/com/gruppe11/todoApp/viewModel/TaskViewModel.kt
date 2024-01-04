package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Tag
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import com.gruppe11.todoApp.ui.screenStates.TasksScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val _UIState = MutableStateFlow(
        TasksScreenState(
            selectedData = LocalDateTime.now(),
            completeFilter = false,
            incompleteFilter = false,
            priorities = mutableSetOf(),
            sortedOption = "Priority Descending"
        )
    )
    val UIState = _UIState.asStateFlow()
    val DaysMap : StateFlow<Map<LocalDate,Float>> get() = _DaysMap

    private val _filterTags = getFilterTags().toMutableSet()
    
    val tags: Set<Tag>
        get() = _filterTags

    private fun getFilterTags() = emptySet<Tag>()

    init {
        _DaysMap.value = generateMapOfDays(null)
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.readAll().collect{
                tasks -> _TaskState.value = tasks
            }
        }
    }
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return _TaskState.value.filter {it.deadline.toLocalDate() == date.toLocalDate()}
    }
    fun changeMonthDate(date:LocalDateTime){
        changeDate(date)
        _DaysMap.value = generateMapOfDays(date)
    }
    fun changeDate(date: LocalDateTime) {
        viewModelScope.launch {
            _UIState.update { currentState -> currentState.copy(selectedData = date)}
            updateTasks()
        }
    }
    fun updateTask(task: Task, subtaskList: List<SubTask>){
        taskRepository.update(task)
//        addSubtasks(task, subtaskList)
    }

    fun addTask(task: Task, subtaskList: List<SubTask>){
        viewModelScope.launch {
            val tmpTask = taskRepository.createTask(task)
            updateTasks()
            addSubtasks(tmpTask, subtaskList)
            val newDays = generateMapOfDays(null)
            _DaysMap.compareAndSet(newDays, newDays)
        }
    }

    fun removeTask(task: Task){
        taskRepository.delete(task)
    }

    @SuppressLint("NewApi")
    fun generateMapOfDays(date: LocalDateTime?): MutableMap<LocalDate, Float> {
        val toReturn : MutableMap<LocalDate,Float> = emptyMap<LocalDate, Float>().toMutableMap()
        var tmp:LocalDateTime = LocalDateTime.now().minusDays(30)
        if(date != null) {
            tmp = date.minusDays(30)
        }
        for(i in 0 .. 60){
            toReturn[tmp.toLocalDate()] = countTaskCompletionsByDay(tmp)
            tmp = tmp.plusDays(1)
        }
       return toReturn
    }

    @SuppressLint("NewApi")
    fun changeTaskCompletion(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.update(task.copy(isCompleted = !task.isCompleted))
            delay(50)
            _DaysMap.update {
                _DaysMap.value.toMutableMap().apply{
                    this[task.deadline.toLocalDate()] = countTaskCompletionsByDay(task.deadline)
                }
            }
        }
    }

    @SuppressLint("NewApi")
    fun changeSubtaskCompletion(task: Task, subtask: SubTask) {
        subtaskRepository.update(task, subtask.copy(completed = !subtask.completed));

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
    fun getTask(taskId: Int): Task? {
        return taskRepository.read(taskId)
    }

    fun addSubtasks(task: Task, subtasks: List<SubTask>){
        val existingSubtasks = subtaskRepository.readAll(task)
        val newSubtasks = subtasks.filterNot { existingSubtasks.contains(it) }
        for (subtask in newSubtasks) {
            subtaskRepository.createSubtask(task, subtask)
        }
    }

    fun changeFilter(target: String) {
        viewModelScope.launch {
            when (target) {
                "complete" -> _UIState.update { currentState -> currentState.copy(completeFilter = !currentState.completeFilter) }
                "incomplete" -> _UIState.update { currentState -> currentState.copy(incompleteFilter = !currentState.incompleteFilter) }
            }
            if (_UIState.value.completeFilter && _UIState.value.incompleteFilter) {
                _UIState.update { currentState ->
                    currentState.copy(
                        completeFilter = false,
                        incompleteFilter = false
                    )
                }
            }
            updateTasks()
        }
    }

    fun filterTask(task: Task): Boolean {
        return ((_UIState.value.completeFilter && task.isCompleted) ||
                (_UIState.value.incompleteFilter && !task.isCompleted) ||
                (!_UIState.value.completeFilter && !_UIState.value.incompleteFilter))
                && (_UIState.value.priorities.isEmpty() || _UIState.value.priorities.contains(task.priority))
    }

    suspend fun updateTasks() {
        taskRepository.readAll().map { it.filter { task -> task.deadline.toLocalDate().equals(_UIState.value.selectedData.toLocalDate()) && filterTask(task) } }.map { sortTasks(it) }.collect{
            _TaskState.emit(it)
        }
    }



    fun addPriority(priority: Priority) {
        viewModelScope.launch {
            val contains = _UIState.value.priorities.contains(priority)
            val set = _UIState.value.priorities.toMutableSet()
            if (contains) {
                set.remove(priority)
            } else {
                set.add(priority)
            }
            _UIState.update { currentState -> currentState.copy(priorities = set) }
            updateTasks()
        }
    }

    private fun sortTasks(filteredTasks: List<Task>): List<Task> {
        val sortedTasks = when (_UIState.value.sortedOption) {
            "Priority Descending" -> filteredTasks.sortedByDescending { it.priority }
            "Priority Ascending" -> filteredTasks.sortedBy { it.priority }
            "A-Z" -> filteredTasks.sortedBy { it.title }
            "Z-A" -> filteredTasks.sortedByDescending { it.title }
            else -> filteredTasks
        }
        return sortedTasks
    }
    
    fun selectSortingOption(sortOption: String) {
        viewModelScope.launch {
            _UIState.update { currentState -> currentState.copy(sortedOption = sortOption)}
            updateTasks()
        }
    }
}