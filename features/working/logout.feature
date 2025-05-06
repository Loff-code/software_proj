Feature: User logout
  Description: Employee logs out from the project managing system
  Actor: user

  Scenario: User logs out
    Given the user is logged in
    When the user logs out
    Then the user is not logged in
