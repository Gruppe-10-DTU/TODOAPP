package com.gruppe11.todoApp.viewModel
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.model.fromString
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.YearMonth

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

    private val taskRepository : ITaskRepository = TaskRepositoryImpl()
    private val _UIState = MutableStateFlow(TaskListUIState())
    val UIState : StateFlow<TaskListUIState> = _UIState.asStateFlow()


    fun getTaskList() : List<Task>{
        return taskRepository.readAll();
    }
    @SuppressLint("NewApi")
    fun getTaskListByDate(date: LocalDateTime): List<Task>{
        return taskRepository.readAll().filter{it.completion!!.dayOfMonth == date.dayOfMonth};
    }
    fun addTask(id: Int, title: String, completion: LocalDateTime, Prio: String, isCompleted: Boolean){
        var tmpTask = Task()
        let {
            tmpTask.id = id;
            tmpTask.title=title;
            tmpTask.completion = completion;
            tmpTask.priority = fromString(Prio);
            tmpTask.isCompleted = isCompleted;
        }
        taskRepository.createTask(tmpTask)
    }

    @SuppressLint("NewApi")
    fun generateListOfDaysLeftInMonth(Date: LocalDateTime): MutableList<Int>{
        val daysInMonth = YearMonth.of(Date.year,Date.month).lengthOfMonth()
        val daysList = mutableListOf<Int>()
        for(i in 1..daysInMonth){
            if(i - LocalDateTime.now().dayOfMonth >= 0) daysList.add(i)
        }
        return daysList
    }

    fun getTaskById(taskID: Int): Task? {
        return taskRepository.read(taskID)
    }

    fun changeTaskCompletion(task: Task){
        task!!.isCompleted = !task!!.isCompleted
        taskRepository.update(task)
    }
}