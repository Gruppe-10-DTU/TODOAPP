package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.ui.screenStates.ExecutionState
import com.gruppe11.todoApp.ui.screenStates.TasksScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
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
            sortedOption = "Priority Descending",
            searchText = ""
        )
    )

    private val _loadingState = MutableStateFlow(ExecutionState.RUNNING)

    val loadingState = _loadingState.asStateFlow()

    val UIState = _UIState.asStateFlow()

    val DaysMap = _TaskState.combine(_UIState) { tasks, state -> generateMapOfDays(state.selectedDate)}.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    val TaskState: Flow<List<Task>> = UIState
        .flatMapLatest { states ->
            _TaskState.map {
                it.filter {
                    task -> filterTask(task) && doesMatchSearchQuery(task)
                }
            }
        }
        .map { sortTasks(it) }
        .distinctUntilChanged()

    private fun doesMatchSearchQuery(task: Task): Boolean {
        val matchingCombinations = listOf(
            task.title
        )

        return matchingCombinations.any{
            it.contains(_UIState.value.searchText, ignoreCase = true)
        }
    }

    fun onSearchTextChange(text: String) {
        viewModelScope.launch {
            _UIState.update { currentState -> currentState.copy(searchText = text) }
        }
    }

    init {
        loadTaskList()
    }

    fun loadTaskList() {
        _loadingState.value = ExecutionState.RUNNING
        viewModelScope.launch(viewModelScope.coroutineContext) {
            try {
                val flow = taskRepository.readAll()
                _loadingState.value = ExecutionState.SUCCESS
                flow.collect{
                    tasks -> _TaskState.value = tasks
                }
            } catch (e: Exception) {
                _loadingState.value = ExecutionState.ERROR
            }
        }
    }

    fun changeDate(date: LocalDateTime) {
        viewModelScope.launch {
            _UIState.update { currentState -> currentState.copy(selectedDate = date)}
        }
    }

    fun updateTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.refresh()
            } catch (exception: Exception) {
                _loadingState.update { ExecutionState.ERROR }
            }
        }
    }

    fun removeTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.delete(task)
            } catch (e: Exception) {
                Log.d("taskRepository", e.toString())
            }
        }
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
            try {
                taskRepository.update(task.copy(isCompleted = !task.isCompleted))
            } catch (e: Exception) {
                Log.d("taskRepository", e.toString())
            }
        }
    }

    @SuppressLint("NewApi")
    fun changeSubtaskCompletion(task: Task, subtask: SubTask) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val subtaskasync = async {
                    subtaskRepository.update(task, subtask.copy(completed = !subtask.completed))
                }.await()
                //val tmp = taskRepository.read(task.id)
                if (subtaskasync != null && task.subtasks.all { (it.id == subtaskasync.id && subtaskasync.completed) || (it.id != subtaskasync.id && it.completed) } && !task.isCompleted) {
                    changeTaskCompletion(task)
                } else if (subtaskasync != null && !task.subtasks.all { (it.id == subtaskasync.id && subtaskasync.completed) || (it.id != subtaskasync.id && it.completed) } && task.isCompleted) {
                    changeTaskCompletion(task)
                } else {
                    taskRepository.read(task.id)
                }
            } catch (e: Exception) {
                Log.d("changeSubtaskCompletion", e.toString())
            }
        }
    }

    private fun countTaskCompletionsByDay(date: LocalDateTime): Float {
        if(_TaskState.value.isEmpty()){
            return 0f
        }
        val totComp = _TaskState.value.filter { it.deadline.toLocalDate() == date.toLocalDate() }.count { it.isCompleted }
        val totTask = _TaskState.value.count{ it.deadline.toLocalDate() == date.toLocalDate() }
        if (totTask == 0) return 0f
        return totComp/totTask.toFloat()
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
            if (set.size == Priority.entries.size) {
                set.clear()
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