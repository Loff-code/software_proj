Feature: Employee Login
  Description: The employee logs into the system and also logs out
  Actor: Employee

  Scenario: Employee can login
    Given that an employee is not logged in
    And that an employee with the name "Bob", email "bob@world.com", ID "0002" and password "BobBob" exists
    When the employee logs in with the ID "0002" and password "BobBob"
    Then the employee with the ID "0002" is now logged in


  Scenario: Employee has the wrong password
    Given that an employee is not logged in
    And that an employee with the name "Bob", email "bob@world.com", ID "0002" and password "BobBob" exists
    When the employee logs in with the ID "0002" and password "CarlCarl"
    Then the employee is not logged in


  Scenario: Employer can login
    Given that the employer is not logged in
    And that the employer with the name "Admin", email "admin@world.com", ID "0001" and password "AdminAdmin" exists
    When the employer logs in with the ID "0001" and password "AdminAdmin"
    Then the employer with the ID "0001" is now logged in




