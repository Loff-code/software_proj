Feature: Create Project
  Description: The employer creates a project
  Actor: Employer

  Scenario: Employer creates a project successfully
    Given an employer is logged in
    And client clientHelper exists
    When the employer creates a project with the name "P", client clientHelper
    Then the project is created with the name "P"

  Scenario: Employer fails to create project with no name
    Given an employer is logged in
    And client clientHelper exists
    When the employer creates a project with the name "", client clientHelper
    Then the project "" is not created
    #And an error message happens

  Scenario: Employer fails to create project without client
    Given an employer is logged in
    And client clientHelper exists
    When the employer creates a project with the name "P", client null
    Then the project "P" is not created
    #And an error message happens

  Scenario: Employer fails to create project with identical name
    Given an employer is logged in
    And client clientHelper exists
    And project with name "P" exists
    When the employer creates a project with the name "P", client clientHelper
    Then there are no duplicates of the project "P"
    #And an error message happens