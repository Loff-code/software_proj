Feature: Create Project
  Description: The User creates a project
  Actor: User

  Scenario: User creates a project successfully
    Given the user is logged in
    When the user creates a project with the name "P", client "a"
    Then the project is created with the name "P"

  Scenario: User fails to create project with no name
    Given the user is logged in
    When the user creates a project with the name "", client "a"
    And an error message happens

  Scenario: User fails to create project without client
    Given the user is logged in
    When the user creates a project with the name "P", client ""
    Then the project "P" is not created
    #And an error message happens

  Scenario: User creates project with identical name, but gets different IDs
    Given the user is logged in
    And project with name "P" exists
    When the user creates a project with the name "P", client "a"
    Then the projects have different IDs
    #And an error message happens

