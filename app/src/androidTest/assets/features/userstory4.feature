Feature: SubtaskCreated
  Scenario: Add a subtask to a task
    Given  I have created a task
    When I fill out a subtask and save it
    Then The subtask is saved to the database