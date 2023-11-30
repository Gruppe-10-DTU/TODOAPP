Feature: Remove Task
  As a user I want to delete a task after it has been created, so that I correct my schedule.

  Scenario:
    Given A task already exists, and I want to remove it
    When Clicking the task, a menu should appear allowing me to edit or remove the task
    Then The task should now disappear from the list