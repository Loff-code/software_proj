Feature: Register used time
  Description: Allows the user to register the time they have spent on activities.
  Actor: User

  Background:
    Given the user is logged in
    Given there exists a project with name "example"
    Given there exists users with the following initials in the project
      | Huba     |
      | Sore     |
      | Vict     |
      | Hamz     |
      | Zoha     |

  Scenario: Employee registers time spent on an activity
    Given that the employee is logged in
    And the employee is assigned to an activity named "Implementation"
    When the employee registers 4 hours spent on "Implementation" on date "2025-03-21"
    Then the system records 4 hours for "Implementation" on "2025-03-22" for the employee

  Scenario: Employee tries to register time for an activity they are not assigned to
    Given that the employee is logged in
    And there is an activity named "Testing" that the employee is not assigned to
    When the employee tries to register 2 hours spent on "Testing" on date "2025-03-21"
    Then the system does not allow the time registration
    And an error message happens

  Scenario: Employee registers time with invalid hours
    Given that the employee is logged in
    And the employee is assigned to an activity named "Design"
    When the employee tries to register -3 hours spent on "Design" on Date "2025-03-21"
    Then the system does not accept the negative hours
    And an error message happens