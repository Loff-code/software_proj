Feature: Create activity
  Description: The user creates an activity
  Actor: User


  Background:
    Given the user is logged in
    And project with name "P" exists

  Scenario: User adds an activity
    When the user adds an activity with name "A", budgeted time 1 hours, start week 1, end week 4 to project "P"
    Then the activity "A" is added to project "P"

  Scenario: Employee adds an activity with an empty name
    When the user adds an activity with name "", budgeted time 1 hours, start week 1, end week 4 to project "P"
    Then an error message happens

  Scenario: Employee adds an activity which is already created
    When the user adds an activity with name "A", budgeted time 1 hours, start week 1, end week 4 to project "P"
    When the user adds an activity with name "A", budgeted time 1 hours, start week 1, end week 4 to project "P"
    Then an error message happens
