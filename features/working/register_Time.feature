Feature: Register used time
  Description: Allows the user to register the time they have spent on activities.
  Actor: User

  Background:
    Given the user is logged in
    And that a project with ID 25000 exists
    And the following users are registered
      | huba |
      | sore |
      | vict |
      | hamz |
      | zoha |
    And the user adds an activity with name "Demo", budgeted time 100 hours, start year 2025, start week 3, end year 2025, end week 6 to project with ID 25000
    And the user "vict" is assigned to the activity "Demo" in project "P" with ID 25000

  Scenario: User registers time spent on an activity successfully
    Given the system date is mocked
    When the user "vict" registers 4.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    Then the system records 4.0 hours for "Demo" on "2025-02-06" for the user "vict" in project with ID 25000
    And printsStuff "vict"
    And no error message is shown

  Scenario: User registers time with invalid hours
    Given the system date is mocked
    When the user "vict" registers -3.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    Then an error message "Not allowed: Hours have to be positive" should be shown

  Scenario: User registers time with missing date
    When the user "vict" registers 4.0 hours spent on activity "Demo" on date "" in project with ID 25000
    Then an error message "Not allowed: Date cannot be null" should be shown

  Scenario: User registers time with missing user
    Given the system date is mocked
    When the user "" registers 4.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    Then an error message "Not allowed: User ID cannot be blank" should be shown

  Scenario: User registers time with non-0.5 hour value (rounded up)
    Given the system date is mocked
    When the user "vict" registers 5.4 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    Then the system records 5.5 hours for "Demo" on "2025-02-06" for the user "vict" in project with ID 25000
    And no error message is shown

  Scenario: User registers time with non-0.5 hour value (rounded down)
    Given the system date is mocked
    When the user "vict" registers 5.2 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    Then the system records 5.0 hours for "Demo" on "2025-02-06" for the user "vict" in project with ID 25000
    And no error message is shown

  Scenario: User views all registered time entries for their activity
    Given the system date is mocked
    When the user "vict" registers 2.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    And the user "vict" registers 3.5 hours spent on activity "Demo" on date "2025-02-07" in project with ID 25000
    Then the user "vict" should see the following registered time entries for activity "Demo" in project with ID 25000:
      | 2025-02-06 | 2.0 hours |
      | 2025-02-07 | 3.5 hours |

