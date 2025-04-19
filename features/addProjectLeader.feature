Feature: Add project leader
  Description: the user assigns a project leader to a project in the system.
  Actor: User

  Background:
    Given the user "Huba" exists
    Given the user is logged in

  Scenario: User assigns a project leader successfully
    Given that a project with ID 25001 exists
    Given the user "Huba" is not already a leader of the project
    When the user assigns "Huba" as the project leader to the project 25001
    Then the user "Huba" is set as the leader of project 25001

  Scenario: User assigns a project leader who is already assigned
    Given the user "Huba" is already a project leader to project 25001
    When the user "Hamz" tries to assign "Huba" as the project leader to project 25001 again
    Then an error message happens

  Scenario: User assigns a non existing user as project leader
    Given that a project with ID 25001 exists
    Given the user "Ekke" does not exist
    When the user assigns "Ekke" as project leader to project 25001
    Then an error message happens about the user not existing

  Scenario: User assigns a project leader to a non existing project
    Given the project with ID 25002 does not exist
    When the user assigns "Huba" as project leader to project with ID 25002 that does not exist
    Then an error message happens about the project not existing


  Scenario: User tries to assign themselves as project leader
    Given that a project with ID 25001 exists
    When the user "Huba" tries to assign themselves as project leader to project 25001
    Then an error message happens about the user not being able to assign themselves as project leader
