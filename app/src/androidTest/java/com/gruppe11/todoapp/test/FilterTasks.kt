package com.gruppe11.todoApp.test

import com.gruppe11.todoApp.model.Task
import com.gruppe11.todoApp.repository.SubtaskRepositoryImpl
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import com.gruppe11.todoApp.viewModel.TaskViewModel
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import junit.framework.TestCase.assertEquals
import java.time.LocalDateTime

class FilterTasks {

    private val viewModel: TaskViewModel = TaskViewModel(TaskRepositoryImpl(), SubtaskRepositoryImpl())
    private lateinit var selectedDate: LocalDateTime
    private lateinit var task : Task

    @Given("I have created multiple tasks, and I want to quickly be able to group them by day.")
    fun iHaveCreatedMultipleTasksAndIWantToQuicklyBeAbleToGroupThemByDay() {
        for (i in 1.. 5){
            viewModel.addTask(
                task.copy(0,task.title,task.priority,selectedDate,task.isCompleted),
                emptyList()
            )
        }
        val latedate : LocalDateTime = LocalDateTime.now().minusDays(5);
        viewModel.addTask(task.copy(0,task.title,task.priority,latedate,task.isCompleted),
        emptyList())
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
            //assertEquals(task.deadline.toLocalDate(), selectedDate.toLocalDate());
        }
    }

    @And("All the tasks that do not have the associated date should not be displayed")
    fun allTheTasksThatDoNotHaveTheAssociatedDateShouldNotBeDisplayed() {
        val tasks : List<Task> = viewModel.getTaskListByDate(selectedDate)
        //assertEquals(6, tasks.size);

    }
}