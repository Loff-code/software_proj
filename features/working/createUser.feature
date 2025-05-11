Feature: Create User
  Description: The User creates a User
  Actor: User

  Scenario: User creates a user successfully
    Given the user is logged in
    When the user creates a user with the ID "sore"
    Then the user is created with the ID "sore"
    And no error message is shown

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




