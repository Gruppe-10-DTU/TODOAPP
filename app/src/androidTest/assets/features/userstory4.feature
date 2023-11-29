Feature: DateGrouping
  Scenario: Show tasks by dates
    Given  I have created multiple tasks, and I want to quickly be able to group them by day.
    When I select a date in the date selector.
    Then A curated selection of tasks with the corresponding selected date should appear
    And All the tasks that do not have the associated date should not be displayed
