package com.gruppe11.todoApp.test

import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.repository.SubtaskRepositoryImpl
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import com.gruppe11.todoApp.viewModel.TaskViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import java.time.LocalDateTime


class CreateTaskTest{
    private val viewModel: TaskViewModel = TaskViewModel(TaskRepositoryImpl(), SubtaskRepositoryImpl())
    private lateinit var task: Task
    @Given("I want to create a task")
    fun iWantToCreateATask() = runTest {
        task = Task(1, "testTask",Priority.HIGH,LocalDateTime.now(),false)
    }
    @When("I click on the plus icon, a text box I can fill out should appear")
    fun iClickOnThePlusIconATextBoxICanFillOutShouldAppear() = runTest {
        viewModel.addTask(
            task.copy(task.id,task.title,task.priority,task.deadline,task.isCompleted),
            emptyList()
        )
    }
    @Then("The task should be created under today")
    fun theTaskShouldBeCreatedUnderToday() = runTest {
        Assert.assertTrue(viewModel.getTaskListByDate(LocalDateTime.now()).size == 1)
    }
}