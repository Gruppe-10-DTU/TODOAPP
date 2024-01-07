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
import com.gruppe11.todoApp.ui.screenStates.TasksScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

    private val _UIState = MutableStateFlow(
        TasksScreenState(
            selectedDate = LocalDateTime.now(),
            completeFilter = false,
            incompleteFilter = false,
            priorities = mutableSetOf(),
            sortedOption = "Priority Descending"
        )
    )

    val UIState = _UIState.asStateFlow()

    val DaysMap = _TaskState.combine(_UIState) { tasks, state -> generateMapOfDays(state.selectedDate)}.distinctUntilChanged()
    private val _filterTags = getFilterTags().toMutableSet()

    val TaskState: Flow<List<Task>> = UIState.flatMapLatest { states -> _TaskState.map { it.filter { task -> filterTask(task) } } }.map { sortTasks(it) }.distinctUntilChanged()

    val tags: Set<Tag>
        get() = _filterTags

    private fun getFilterTags() = emptySet<Tag>()

    init {
        viewModelScope.launch(viewModelScope.coroutineContext) {
            taskRepository.readAll().collect{
                tasks -> _TaskState.value = tasks
            }
        }
    }
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return _TaskState.value.filter {it.deadline.toLocalDate() == date.toLocalDate()}
    }
    fun changeDate(date: LocalDateTime) {
        viewModelScope.launch {
            _UIState.update { currentState -> currentState.copy(selectedDate = date)}
        }
    }
    fun updateTask(task: Task, subtaskList: List<SubTask>){
        viewModelScope.launch {
            taskRepository.update(task)
        }

//        addSubtasks(task, subtaskList)
    }

    fun addTask(task: Task, subtaskList: List<SubTask>){
        viewModelScope.launch {
            val tmpTask = taskRepository.createTask(task)
            addSubtasks(tmpTask, subtaskList)

        }
    }

    fun removeTask(task: Task){
        viewModelScope.launch {
            taskRepository.delete(task)
        }
    }

    @SuppressLint("NewApi")
    suspend fun generateMapOfDays(date: LocalDateTime?): MutableMap<LocalDate, Float> {
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
        }
    }

    @SuppressLint("NewApi")
    fun changeSubtaskCompletion(task: Task, subtask: SubTask) {
        viewModelScope.launch {
            subtaskRepository.update(task, subtask.copy(completed = !subtask.completed));
        }
    }

    fun countTaskCompletionsByDay( date: LocalDateTime): Float {
        if(_TaskState.value.isEmpty()){
            return 0f
        }

        val totComp = _TaskState.value.filter { it.deadline.toLocalDate() == date.toLocalDate() }.count { it.isCompleted }
        val totTask = _TaskState.value.count{ it.deadline.toLocalDate() == date.toLocalDate() }
        if (totTask == 0) return 0f
        println("Date: " + date + " had " + totComp + " completed and total amount of tasks: " + totTask)
        return totComp/totTask.toFloat()
    }

    suspend fun getSubtasks(currentTask: Task): List<SubTask> {
        return subtaskRepository.readAll(currentTask)

    }
    fun removeSubtask(task: Task, subTask: SubTask){
        viewModelScope.launch {
            subtaskRepository.delete(task, subTask)
        }
    }
    suspend fun getTask(taskId: Int): Task? {
        return taskRepository.read(taskId)
    }

    fun addSubtasks(task: Task, subtasks: List<SubTask>){
        viewModelScope.launch {
            val existingSubtasks = subtaskRepository.readAll(task)
            val newSubtasks = subtasks.filterNot { existingSubtasks.contains(it) }
            for (subtask in newSubtasks) {
                subtaskRepository.createSubtask(task, subtask)
            }
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
        }
    }

    private fun filterTask(task: Task): Boolean {
        return  (task.deadline.toLocalDate() == _UIState.value.selectedDate.toLocalDate()) &&
                ((_UIState.value.completeFilter && task.isCompleted) ||
                (_UIState.value.incompleteFilter && !task.isCompleted) ||
                (!_UIState.value.completeFilter && !_UIState.value.incompleteFilter))
                && (_UIState.value.priorities.isEmpty() || _UIState.value.priorities.contains(task.priority))
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
        }
    }
}