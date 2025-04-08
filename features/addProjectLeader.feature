Feature: Add project leader
  Description: the user assigns a project leader to a project in the system.
  Actor: User

  Scenario: User assigns a project leader successfully
    Given the user is logged in
    Given the user "Jerry" exists
    Given that a project with ID 25001 exists
    Given the user "Jerry" is not already a leader of the project
    When the user assigns "Jerry" as the project leader to the project 25001
    Then the user "Jerry" is set as the leader of project 25001

  Scenario: User assigns a project leader who is already assigned
    Given the user is logged in
    Given the user "Jerry" is already a project leader to project 25001
    When the user "X" tries to assign "Jerry" as the project leader to project 25001 again
    Then an error message happens

  Scenario: User assigns a non existing user as project leader
    Given the user is logged in
    Given that a project with ID 25001 exists
    Given the user "Marques" does not exist
    When the user assigns "Marques" as project leader to project 25001
    Then an error message happens about the user not existing


