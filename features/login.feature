Feature: Login and Logout
  Description: The employee logs into the system and also logs out
  Actor: Employee

  Scenario: Employee can login
    Given that the employee is not logged in
    And the employee username is "Bob"
    And the employee password is "BobBob"
    Then the employee login succeeds
    And the Employee is logged in
