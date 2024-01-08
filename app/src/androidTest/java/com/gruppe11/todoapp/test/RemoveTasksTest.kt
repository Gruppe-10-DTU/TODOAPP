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
class RemoveTasksTest {
    private val viewModel: TaskViewModel = TaskViewModel(TaskRepositoryImpl(), SubtaskRepositoryImpl())
    private lateinit var task : Task

    @Given("A task already exists, and I want to remove it")
    fun aTaskAlreadyExistsAndIWantToRemoveIt() = runTest {
        task = Task(0,"Hej", Priority.MEDIUM,LocalDateTime.now(),false)
        viewModel.addTask(
            task = task,
            listOf()
        )

    }

    @When("Clicking the task, a menu should appear allowing me to edit or remove the task")
    fun clickingTheTaskAMenuShouldAppearAllowingMeToEditOrRemoveTheTask() = runTest {
        viewModel.removeTask(task)
    }

    @Then("The task should now disappear from the list")
    fun theMenuDisappearsACuratedSelectionOfTasksWithTheCorrespondingSelectedDateShouldAppear() = runTest {
        Assert.assertTrue(viewModel.getTask(task.id) == null)
    }
}