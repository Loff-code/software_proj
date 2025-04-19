Feature: Create activity
  Description: The user creates an activity
  Actor: User




  Scenario: User adds an activity
    Given that the employee Bob is logged in
    Given that a project called "Cat"
    When an activity called "Part1" is added
    Then the activity is in the project "Cat"

  Scenario: Employee adds an activity with an empty name
    Given that the employee Bob is logged in
    Given that a project called "Cat"
    When an activity called "" is added
    Then an error message happens

  Scenario: Employee adds an activity which is already created
    Given that the employee Bob is logged in
    Given that a project called "Cat" is created
    And an activity called "part1" is already present
    When an activity called "part1" is added
    Then an error message happens
