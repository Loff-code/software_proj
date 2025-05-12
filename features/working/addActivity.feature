Feature: Create activity
  Description: The user creates an activity
  Actor: User

  Background:
    Given the user is logged in
    And that a project with ID 25000 exists

  Scenario: User adds an activity
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then the activity "a" is added to project with ID 25000
    Then no error message is shown

  Scenario: Employee adds an activity with an empty name and fails
    When the user adds an activity with name "", budgeted time 1 hours, start year 2025, start week 1, end year 2025, end week 4 to project with ID 25000
    Then an error message "Activity name cannot be null or empty" is shown

  Scenario: Employee adds an activity with the same name and fails
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Activity already exists" is shown

  Scenario: Employee adds an activity with negative start week and fails
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week -5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Weeks must be between 1 and 52" is shown

  Scenario: Employee adds an activity with end week before start week and fails
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 4 to project with ID 25000
    Then an error message "End time cannot be before start time" is shown

  Scenario: Employee adds an activity with negative budgeted time and fails
    When the user adds an activity with name "a", budgeted time -10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Budget time cannot be negative" is shown

  Scenario: Employee tries to add an activity while there is a different project leader and fails
    Given the following users are registered
      | huba |
      | sore |
      | vict |
      | hamz |
      | zoha |
    When the user assigns "sore" as the project leader to the project 25000
    And the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Only the project leader can add activities" is shown

  Scenario: Employee tries to add an activity while not being logged in
    Given the user logs out
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "User ID cannot be null or empty" is shown


  Scenario: Employee adds an activity with a negative start year and fails
    When the user adds an activity with name "a", budgeted time 10 hours, start year -2025, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "Year cannot be negative" is shown

  Scenario: Employee adds an activity with start year after end year and fails
    When the user adds an activity with name "a", budgeted time 10 hours, start year 2026, start week 5, end year 2025, end week 6 to project with ID 25000
    Then an error message "End time cannot be before start time" is shown
