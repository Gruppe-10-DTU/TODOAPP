package com.gruppe11.todoApp.viewModel
import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.Task

class Task : ViewModel() {
    val taskList: MutableList<Task> = mutableListOf();

    fun getTaskList() : MutableList<Task>{
        return taskList;
    }
    fun getTaskListByDate(Date date): MutableList<Task>{
        MutableList<Task> = mutableListOf();
        taskList.forEach()
    }
}