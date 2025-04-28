Feature: Edit Project
  As a user
  I want to edit an existing project
  So that I can update its details when needed

  Scenario: Successfully edit a project's name
    Given a project with project number "24001" exists
    When I update the project's name to "New Project Name"
    Then the project's name should be "New Project Name"

  Scenario: Try to edit a non-existing project
    Given no project with project number "99999" exists
    When I attempt to edit the project
    Then I should receive an error message saying "Project not found"

  Scenario: Successfully change project leader
    Given a project with project number "24002" exists
    And a user with ID "huba" exists
    When I assign "huba" as the new project leader
    Then the project's leader ID should be "huba"
