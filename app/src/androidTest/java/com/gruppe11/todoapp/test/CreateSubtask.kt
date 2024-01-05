package com.gruppe11.todoApp.test

import com.gruppe11.todoApp.model.SubTask
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.repository.SubtaskRepositoryImpl
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import com.gruppe11.todoApp.viewModel.TaskViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import junit.framework.TestCase.assertEquals
import java.time.LocalDateTime

class CreateSubtask {

    private val viewModel: TaskViewModel = TaskViewModel(TaskRepositoryImpl(), SubtaskRepositoryImpl())
    private lateinit var subtask : SubTask;
    private lateinit var task : Task;
    @Given("I have created a task")
    fun iHaveCreatedATask() {
        viewModel.addTask(
            task.copy(task.id,task.title,task.priority,task.deadline,task.isCompleted),
            emptyList()
        )
        viewModel.getTaskListByDate(LocalDateTime.now())
    }

    @When("I fill out a subtask and save it")
    fun iFillOutASubtaskAndSaveIt() {
        subtask = SubTask("Subtask", 1)
        viewModel.addSubtasks(viewModel.getTaskListByDate(LocalDateTime.now())[0], listOf(subtask))
    }

    @Then("The subtask is saved to the database")
    fun theSubtaskIsSavedToTheDatabase() {
        val subtasks : List<SubTask> =viewModel.getSubtasks(task)
        assertEquals(1, subtasks.size)
    }
}