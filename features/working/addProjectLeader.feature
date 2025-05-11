Feature: Add project leader
  Description: the user assigns a project leader to a project in the system.
  Actor: User

  Background:
    Given the user is logged in
    Given the user "huba" exists
    Given the user "Hamz" exists

  Scenario: User assigns a project leader successfully
    Given that a project with ID 25001 exists
    Given the user "huba" is not already a leader of the project
    When the user assigns "huba" as the project leader to the project 25001
    Then the user "huba" is set as the leader of project 25001
    And no error message is shown

  Scenario: User assigns a project leader who is already assigned
    Given the user "huba" is already a project leader to project 25001
    When the user "Hamz" tries to assign "huba" as the project leader to project 25001 again
    Then an error message happens

  Scenario: User assigns a non existing user as project leader
    Given that a project with ID 25001 exists
    Given the user "Ekke" does not exist
    When the user assigns "Ekke" as project leader to project 25001
   Then an error message happens about the user not existing

  Scenario: User assigns a project leader to a non existing project
    Given the project with ID 25002 does not exist
    When the user assigns "huba" as project leader to project with ID 25002 that does not exist
    Then an error message happens about the project not existing

    Scenario: User tries to assign a project leader with an empty ID
      Given that a project with ID 25001 exists
      Given the user "huba" is not already a leader of the project
      When the user assigns "" as the project leader to the project 25001
      Then an error message happens

  Scenario: User is not the project leader and tries to assign a project leader when there already is one
    Given the user "Hamz" is already a project leader to project 25001
    When the user assigns "huba" as the project leader to the project 25001
    Then an error message happens

