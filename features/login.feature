Feature: Employee Login
  Description: The employee logs into the system and also logs out
  Actor: Employee

  Scenario: User can login
    Given that an employee is not logged in
    And that an employee with the ID "0002" and password "BobBob" exist
    When the employee logs in with the ID "0002" and password "BobBob"
    Then the employee with the ID "0002" is now logged in

  Scenario: Employee has the wrong password
    Given that an employee is not logged in
    And that an employee with the ID "0002" and password "BobBob" exist
    When the employee logs in with the ID "0002" and password "CarlCarl"
    Then the employee is not logged in