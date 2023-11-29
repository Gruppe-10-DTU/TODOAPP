package com.gruppe11.todoapp.test

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert


class PrintToConsoleTest {
    @Given("I have a simple test environment")
    fun iHaveASimpleTestEnvironment() {
        println("Given step executed")
    }

    @When("I perform a simple action")
    fun iPerformASimpleAction() {
        println("When step executed")
    }

    @Then("I should get a simple result")
    fun iShouldGetASimpleResult() {
        println("Then step executed")
        Assert.assertTrue(true) // Simple assertion for demonstration

    }
}