Feature: Create activity
  Description: The user creates an activity
  Actor: User

  Background:
    Given the user is logged in
    And that a project with ID 25000 exists

  Scenario: Successfully adding a valid activity to a project
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then the activity "a" is added to project with ID 25000
    Then no error message is shown

  Scenario: Failing to add an activity with an empty name
    When the user adds an activity with name "", budgeted time 1 hours, start year 2025, start week 1, end year 2025, end week 4 to project with ID 25000
    Then an error message "Activity name cannot be null or empty" is shown

  Scenario: Failing to add a duplicate activity to the same project
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Activity already exists" is shown

  Scenario: Failing to add an activity with invalid negative start week
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week -5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Weeks must be between 1 and 52" is shown

  Scenario: Failing to add an activity with end week before start week
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 4 to project with ID 25000
    Then an error message "End time cannot be before start time" is shown

  Scenario: Failing to add an activity with negative budgeted time
    When the user adds an activity with name "a", budgeted time -10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Budget time cannot be negative" is shown

  Scenario: Failing to add an activity as a non-project leader
    Given the following users are registered
      | huba |
      | sore |
      | vict |
      | hamz |
      | zoha |
    When the user assigns "sore" as the project leader to the project 25000
    And the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Only the project leader can add activities" is shown

  Scenario: Failing to add an activity while not logged in
    Given the user logs out
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "User ID cannot be null or empty" is shown

  Scenario: Failing to add an activity with a negative start year
    When the user adds an activity with name "a", budgeted time 10 hours, start year -2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Year cannot be negative" is shown

  Scenario: Failing to add an activity with start year after end year
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2026, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "End time cannot be before start time" is shown

  Scenario: Successfully requesting a single day of sick leave
    When the user "huba" requests leave named "Vacation" from "2025-07-01" to "2025-07-01" with 8.0 hours
    Then the project 1 contains a leave activity for "huba" named "Vacation"
    And the activity name contains "Vacation"
    And the activity name contains "huba"
    And the activity name contains "2025-07-01"
    And the leave activity for "huba" on "2025-07-01" registers 8.0 hours

  Scenario: Successfully requesting multiple types of leave
    When the user "huba" requests leave named "Vacation" from "2025-07-01" to "2025-07-01" with 8.0 hours
    And the user "huba" requests leave named "sick" from "2025-07-01" to "2025-07-01" with 8.0 hours
    Then the project 1 contains a leave activity for "huba" named "Vacation"
    And the project 1 contains a leave activity for "huba" named "sick"

  Scenario: Generating full-year leave status report
    When the user requests the leave status report from year 2025 week 1 to year 2025 week 52
    Then the leave status report from year 2025 week 1 to year 2025 week 1 is generated successfully

  Scenario: Generating single-week leave report after leave request
    When the user "huba" requests leave named "Vacation" from "2025-07-01" to "2025-07-01" with 8.0 hours
    When the user requests the leave status report from year 2025 week 1 to year 2025 week 1
    Then the leave status report from year 2025 week 1 to year 2025 week 1 is generated successfully

  Scenario: Generating full-year leave report after leave request
    When the user "huba" requests leave named "Vacation" from "2025-07-01" to "2025-07-01" with 8.0 hours
    When the user requests the leave status report from year 2025 week 1 to year 2025 week 1
    Then the leave status report from year 2025 week 1 to year 2025 week 52 is generated successfully
