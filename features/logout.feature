Feature: Employee logout
  Description: Employee logs out from the project managing system
  Actor: Employee

  Scenario: Employeee logs out
    Given that the employee with the ID "0002" and password "BobBob" is logged in
    When the employee logs out
    Then the employee is not logged in