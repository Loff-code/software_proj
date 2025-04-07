Feature: Employee logout
  Description: Employee logs out from the project managing system
  Actor: Employee

  Scenario: Employeee logs out
    Given that an employee with the name "Bob", email "bob@world.com", ID "0002" and password "BobBob" exists
    And the employee logs in with the ID "0002" and password "BobBob"
    Then the employee with the ID "0002" is now logged in
    When the employee logs out
    Then the employee is not logged in

  Scenario: Employeer logs out
    And that the employer with the name "Admin", email "admin@world.com", ID "0001" and password "AdminAdmin" exists
    When the employer logs in with the ID "0001" and password "AdminAdmin"
    Then the employer with the ID "0001" is now logged in
    When the employer logs out
    Then the employer is not logged in
