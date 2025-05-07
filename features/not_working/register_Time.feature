Feature: Register used time
  Description: Allows the user to register the time they have spent on activities.
  Actor: User

  Background:
    Given the user is logged in
    And project with name "P" exists
    And there exists users with the following initials in the project
      | huba     |
      | sore     |
      | vict     |
      | hamz     |
      | zoha     |
    And the user creates an activity with the following informations
      | Name | Description  | Budget Hours | Start Week | End Week | Start Year | End Year | Initials  |
      | Demo | Optimization | 150          | 3          | 6        | 2025       | 2025     |Sore, vict |


  Scenario: User registers time spent on an activity successfully
    When the user "vict" registers 4 hours spent on activity "Demo" on date "2025-02-06"
    Then the system records 4 hours for "Demo" by "vict" on "2025-02-06" for the user "vict"


  Scenario: User tries to register time for an activity they are not assigned to
    Given the user "huba" is not assigned to it
    When the user "huba" tries to register 2 hours spent on "Demo" on date "2025-02-06"
    Then the system does not allow the time registration
    And an error message "You are not allowed to register time for this activity" should be shown

  Scenario: User registers time with invalid hours
    When the user "vict" tries to register -3 hours spent on "Demo" on Date "2025-02-06"
    Then the system does not accept the negative hours
    And an error message "Invalid hours" should be shown

  Scenario: User registers time with invalid date
    When the user "vict" tries to register 4 hours spent on "Demo" on date "2025-02-06"
    Then the system does not accept the invalid date
    And an error message "Invalid date" should be shown

  Scenario: User registers time with missing hours
    When the user "vict" registers "" hours spent on "Demo" on date "2025-02-06"
    Then an error message "Hours missing" should be shown

  Scenario: User registers time with missing date
    When the user "vict" registers "4" hours spent on "Demo" on date ""
    Then an error message "Date missing" should be shown

  Scenario: User registers time with missing user
    When the user "" registers "4" hours spent on "Demo" on date "2025-02-06"
    Then an error message "User missing" should be shown

  Scenario: User registers time with non-0.5 hour value
    When the user "vict" registers 5.4 hours spent on activity "Demo" on date "2025-02-06"
    Then the system rounds the hours up to "5.5"
    And the system records "5.5" hours for "Demo" on "2025-02-06" for the user "vict"





