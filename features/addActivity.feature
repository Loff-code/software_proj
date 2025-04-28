Feature: Add activity to project
  Description: An employee adds an activity to a project in the system.
  Actor: Employee

  Scenario: Employee adds an activity successfully
    Given the user is logged in
    And project with name "P" exists
    Then the project is created with the name "P"
    When the user adds an activity with name "A", budgeted time 1 hours, start week 1, end week 4 to project "P"
    Then the activity "A" is added to project "P"
    #And an error message happens