package com.gruppe11.todoapp.test

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert


class PrintToConsoleTest {
    @Given("I have a simple test environment")
    fun i_have_a_simple_test_environment( ) {
        println("Given step executed")
    }

    @When("I perform a simple action")
    fun i_perform_a_simple_action( ) {
        println("When step executed")
    }

    @Then("I should get a simple result")
    fun i_should_get_a_simple_result( ) {
        println("Then step executed")
        Assert.assertTrue(true)
    }
}