Feature: User Login
  Description: The user logs into the system and also logs out
  Actor: User

  Scenario: User can login
    Given the user is not logged in
    And the user with the ID "bob" exists
    When the user logs in with the ID "bob"
    Then the user with the ID "bob" is now logged in
    And no error message is shown


  Scenario: User uses wrong ID
    Given the user is not logged in
    And the user with the ID "carl" does not exist
    When the user logs in with the ID "carl"
    Then an error message "User does not exist" is shown




