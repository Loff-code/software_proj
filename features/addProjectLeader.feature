Feature: Add project leader
  Description: An employee assigns a project leader to a project in the system.
  Actor: Employee

  Scenario: Employee assigns a project leader successfully
    Given an employee is logged in
    Given that a project with ID 25001 exists
    Given that the employee "Jerry" is not already a leader of the project
    When Tom assigns "Jerry" as the project leader to the project 25001
    Then the employee "Jerry" is set as the leader of project 25001

  Scenario: Employee assigns a project leader who is already assigned
    Given that the employee is logged in
    Given that the employee "Jerry" is already a project leader to project 25001
    When the employee assigns "Jerry" as the project leader to project 25001 again
    #And an error message happens

  Scenario: Employee assigns a non existing employee as project leader
    Given that the employee is logged in
    Given that no employee with the name "xxxx" exist in the system
    When the employee assigns "xxxx" as project leader to project 25001
    #And an error message happens