Feature: Register used time
  Description: Allows the user to register the time they have spent on activities.
  Actor: User

  Background:
    Given the user is logged in
    Given there exists users with the following initials in the project
      | Huba     |
      | Sore     |
      | Vict     |
      | Hamz     |
      | Zoha     |

  Scenario: The user registers time spent on an activity
    Given that a project with ID 25001 exists
    Given that an activity named "Demo" exists
    And the user "Huba" is assigned to an activity named "Demo"
    When the user "Huba" registers 4 hours spent on "Demo" on date "2025-03-21"
    Then the system records 4 hours for "Demo" on "2025-03-22" for the user "Huba"

  Scenario: User tries to register time for an activity they are not assigned to
    And there is an activity named "Demo" that the user "Sore" is not assigned to
    When the user "Sore" tries to register 2 hours spent on "Demo" on date "2025-03-21"
    Then the system does not allow the time registration
    And an error message happens

  Scenario: User registers time with invalid hours
    And the user "Huba" is assigned to an activity named "Demo"
    When the user "Huba" tries to register -3 hours spent on "Demo" on Date "2025-03-21"
    Then the system does not accept the negative hours
    And an error message happens

    Scenario: User registers time with invalid date
    And the user "Huba" is assigned to an activity named "Demo"
    When the user "Huba" tries to register 4 hours spent on "Demo" on date "2025-03-32"
    Then the system does not accept the invalid date
    And an error message happens


    Scenario:  registers time with invalid initials



    Scenario: User registers time to an activity that doesnt exist