Feature: Edit activity
  Description: User can edit registered hours for a given activity

  Actor: User
Background:
    Given the user is logged in

  Scenario: User edits registered hours
    Given the user has registered 3 hours on an activity called "Test" in project 25002
    When the user changes the registered from 3 hours to 5 hours on the "Test" activity
    Then the system updates the registered hours to 5 hours on the "Test" activity

  Scenario: User try to edit a non existing activity
    Given the activity "Test 1" does not exist in the project 25003
    When the user tries to edit hours for the "Test 1" activity
    Then an error message happens

  Scenario: User try to edit registered time with an invalid input
    Given the user has registered 3 hours on an activity called "Test 2"
    When the user tries to change the registered hours to -2 on the activity "Test 2"
    Then an error message happens