package com.gruppe11.todoApp.viewModel
import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class TaskListUIState(
    val id: Int? = null,
    val name:String? = null,
    val date:Int? = null

)
class TaskViewModel : ViewModel() {
    private val taskList: MutableList<Task> = mutableListOf();
    private val _UIState = MutableStateFlow(TaskListUIState())
    val UIState : StateFlow<TaskListUIState> = _UIState.asStateFlow()

    fun getTaskList() : MutableList<Task>{
        return taskList;
    }
    fun getTaskListByDate(date: Int): List<Task>{
        return taskList.filter{it.date == date};
    }
    fun addTask(task:Task){
        taskList.add(task);
    }

}