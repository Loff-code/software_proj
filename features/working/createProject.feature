Feature: Create Project
  Description: The User creates a project
  Actor: User

  Scenario: User creates a project successfully
    Given the user is logged in
    When the user creates a project with the name "P", client "a"
    Then the project is created with the name "P"

  Scenario: User creates project with identical name, but gets different IDs
    Given the user is logged in
    And project with name "P" exists
    When the user creates a project with the name "P", client "a"
    Then the projects have different IDs
    And no error message is shown

  Scenario: User fails to create project with no name
    Given the user is logged in
    When the user creates a project with the name "", client "a"
    Then an error message "Project name must be between 1 and 20 characters" is shown

  Scenario: User fails to create project without client
    Given the user is logged in
    When the user creates a project with the name "P", client ""
    Then an error message "Client name must be between 1 and 20 characters" is shown

  Scenario: User fails to create project when not logged in
    When the user creates a project with the name "P", client "a"
    Then an error message "User is not logged in" is shown




