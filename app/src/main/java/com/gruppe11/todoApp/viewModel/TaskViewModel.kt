package com.gruppe11.todoApp.viewModel
import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.fromString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

enum class Priority{
    HIGH,
    MEDIUM,
    LOW
}
data class TaskListUIState(
    var id: Int? = null,
    var title: String? = null,
    var priority: Priority? = null,
    var completion: LocalDateTime? = null,
    var isCompleted: Boolean? = false,

)
class TaskViewModel : ViewModel() {
    private val taskList: MutableList<Task> = mutableListOf();
    private val _UIState = MutableStateFlow(TaskListUIState())
    val UIState : StateFlow<TaskListUIState> = _UIState.asStateFlow()

    fun getTaskList() : MutableList<Task>{
        return taskList;
    }
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return taskList.filter{it.completion == date};
    }
    fun addTask(id: Int, title: String, completion: LocalDateTime, Prio: String, isCompleted: Boolean){
        val Task = Task()
        var tmpTask = Task
        let {
            tmpTask.id = id;
            tmpTask.title=title;
            tmpTask.completion = completion;
            tmpTask.priority = fromString(Prio);
            tmpTask.isCompleted = isCompleted;
        }
        taskList.add(tmpTask)
    }
}