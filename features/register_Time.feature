Feature: Register used time
  Description: Allows the user to register the time they have spent on activities.
  Actor: User

  Background:
    Given the user is logged in
    And a project with ID 25001 exists
    And there exists users with the following initials in the project
      | Huba     |
      | Sore     |
      | Vict     |
      | Hamz     |
      | Zoha     |
    And the user creates an activity with the following informations
      | Name | Description  | Budget Hours | Start Week | End Week | Start Year | End Year | Initials  |
      | Demo | Optimization | 150          | 3          | 6        | 2025       | 2025     |Sore, Vict |


  Scenario: User registers time spent on an activity successfully
    When the user "Vict" registers 4 hours spent on date "2025-02-06"
    Then the system records 4 hours for "Demo" by "Vict" on "2025-02-06" for the user "Vict"


  Scenario: User tries to register time for an activity they are not assigned to
    Given the user "Huba" is not assigned to it
    When the user "Huba" tries to register 2 hours spent on "Demo" on date "2025-02-06"
    Then the system does not allow the time registration
    And an error message "You are not allowed to register time for this activity" should be shown

  Scenario: User registers time with invalid hours
    When the user "Vict" tries to register -3 hours spent on "Demo" on Date "2025-02-06"
    Then the system does not accept the negative hours
    And an error message "Invalid hours" should be shown

  Scenario: User registers time with invalid date
    When the user "Vict" tries to register 4 hours spent on "Demo" on date "2025-02-06"
    Then the system does not accept the invalid date
    And an error message "Invalid date" should be shown

  Scenario: User registers time with missing hours
    When the user "Vict" registers "" hours spent on "Demo" on date "2025-02-06"
    Then an error message "Hours missing" should be shown

  Scenario: User registers time with missing date
    When the user "Vict" registers "4" hours spent on "Demo" on date ""
    Then an error message "Date missing" should be shown

  Scenario: User registers time with missing user
    When the user "" registers "4" hours spent on "Demo" on date "2025-02-06"
    Then an error message "User missing" should be shown

  Scenario: User registers time with non-0.5 hour value
    When the user "Vict" registers "5.4" hours spent on "Demo" on date "2025-02-06"
    Then the system rounds the hours up to "5.5"
    And the system records "5.5" hours for "Demo" on "2025-02-06" for the user "Vict"


    # Gør sådan at det kun er de tildelte user + project leader leader som kan registrere tid
  #Scenario: Project leader registers time on behalf of another user
  #  Given the user "Huba" is project leader
   # When the user "Huba" registers 4 hours on behalf of "Vict" for "Demo" on date "2025-02-06"
    #Then the system records 4 hours for "Vict" on "Demo" on "2025-02-06"





