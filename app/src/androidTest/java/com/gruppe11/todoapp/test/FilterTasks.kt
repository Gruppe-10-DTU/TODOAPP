package com.gruppe11.todoapp.test

import androidx.hilt.navigation.compose.hiltViewModel
import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.repository.SubtaskRepositoryImpl
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import com.gruppe11.todoApp.viewModel.TaskViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import java.time.LocalDateTime
import javax.inject.Inject

class FilterTasks {

    private val viewModel: TaskViewModel = TaskViewModel(TaskRepositoryImpl(), SubtaskRepositoryImpl())
    private lateinit var selectedDate: LocalDateTime;

    @Given("I have created multiple tasks, and I want to quickly be able to group them by day.")
    fun iHaveCreatedMultipleTasksAndIWantToQuicklyBeAbleToGroupThemByDay() {
        for (i in 1.. 5){
            viewModel.addTask(i, "Task: $i", LocalDateTime.now(), "HIGH", false, listOf())
        }
        val latedate : LocalDateTime = LocalDateTime.now().minusDays(5);
        viewModel.addTask(6, "new task", latedate, "HIGH", false, listOf())
    }

    @When("I select a date in the date selector.")
    fun iSelectADateInTheDateSelector() {
        selectedDate = LocalDateTime.now();
    }

    @Then("A curated selection of tasks with the corresponding selected date should appear")
    fun aCuratedSelectionOfTasksWithTheCorrespondingSelectedDateShouldAppear() {
        val tasks : List<Task> = viewModel.getTaskListByDate(selectedDate);
        assertEquals(5, tasks.size);
        for (task : Task in tasks){
            assertEquals(task.deadline.toLocalDate(), selectedDate.toLocalDate());
        }
    }

    @And("All the tasks that do not have the associated date should not be displayed")
    fun allTheTasksThatDoNotHaveTheAssociatedDateShouldNotBeDisplayed() {

        val tasks : List<Task> = viewModel.getTaskListByDate(selectedDate);
        assertNotEquals(6, tasks.size);

    }
}