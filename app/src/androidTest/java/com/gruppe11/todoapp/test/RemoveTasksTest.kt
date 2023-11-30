package com.gruppe11.todoApp.test

import com.gruppe11.todoApp.repository.SubtaskRepositoryImpl
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import com.gruppe11.todoApp.viewModel.TaskViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert
import java.time.LocalDateTime

class RemoveTasksTest {
    private val viewModel: TaskViewModel = TaskViewModel(TaskRepositoryImpl(), SubtaskRepositoryImpl())

    @Given("A task already exists, and I want to remove it")
    fun aTaskAlreadyExistsAndIWantToRemoveIt() {
        viewModel.addTask(1,"TaskToRemove", LocalDateTime.now(),"HIGH",false, listOf())
    }

    @When("Clicking the task, a menu should appear allowing me to edit or remove the task")
    fun clickingTheTaskAMenuShouldAppearAllowingMeToEditOrRemoveTheTask() {
        viewModel.removeTask(viewModel.getTask(1))
    }

    @Then("The task should now disappear from the list")
    fun theMenuDisappearsACuratedSelectionOfTasksWithTheCorrespondingSelectedDateShouldAppear() {
        Assert.assertTrue(viewModel.getTaskList().isEmpty())
    }
}