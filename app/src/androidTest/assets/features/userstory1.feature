Feature: Create Task
  As a user I want to create a task so that I don't forget it

  Scenario: Create Task Scenario
    Given I want to create a task
    When I click on the plus icon, a text box I can fill out should appear
    Then The task should be created under today


