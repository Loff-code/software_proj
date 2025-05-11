Feature: Create User
  Description: The User assigns a user to an activity
  Actor: User

  Background:
    Given the user "sore" exists
    And that a project with ID 25000 exists
    And

  Scenario: User assigns a user to an activity successfully
    Given the user is logged in
    When the user assigns "sore" to the activity {string} in project {int}
    Then the user is created with the ID "sore"

  Scenario: User fails to create user with empty ID
    Given the user is logged in
    When the user creates a user with the ID ""
    Then an error message "User ID must be between 1 and 4 characters" is shown

  Scenario: User fails to create user when not logged in
    Given the user is not logged in
    When the user creates a user with the ID "sore"
    Then an error message "User must be logged in" is shown


  Scenario: User fails to create the same user twice
    Given the user is logged in
    When the user creates a user with the ID "sore"
    Then the user is created with the ID "sore"
    When the user creates a user with the ID "sore"
    Then an error message "User ID is already taken" is shown




