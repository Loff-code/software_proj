Feature: Assign User to Activity
  Description: The User assigns a user to an activity
  Actor: User

  Background:
    Given the user is logged in
    And the user "sore" exists
    And that a project with ID 25000 exists
    And the user adds an activity named "1" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "2" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "3" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "4" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "5" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "6" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "7" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "8" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "9" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "10" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "11" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "12" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "13" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "14" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "15" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "16" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "17" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "18" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "19" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user adds an activity named "20" with budget 10, start year 2025, start week 1, end year 2025, end week 4 to project 25000
    And the user assigns "sore" to the activity "1" in project 25000
    And the user assigns "sore" to the activity "2" in project 25000
    And the user assigns "sore" to the activity "3" in project 25000
    And the user assigns "sore" to the activity "4" in project 25000
    And the user assigns "sore" to the activity "5" in project 25000
    And the user assigns "sore" to the activity "6" in project 25000
    And the user assigns "sore" to the activity "7" in project 25000
    And the user assigns "sore" to the activity "8" in project 25000
    And the user assigns "sore" to the activity "9" in project 25000
    And the user assigns "sore" to the activity "10" in project 25000
    And the user assigns "sore" to the activity "11" in project 25000
    And the user assigns "sore" to the activity "12" in project 25000
    And the user assigns "sore" to the activity "13" in project 25000
    And the user assigns "sore" to the activity "14" in project 25000
    And the user assigns "sore" to the activity "15" in project 25000
    And the user assigns "sore" to the activity "16" in project 25000
    And the user assigns "sore" to the activity "17" in project 25000
    And the user assigns "sore" to the activity "18" in project 25000
    And the user assigns "sore" to the activity "19" in project 25000
    And the user assigns "sore" to the activity "20" in project 25000

  Scenario: User assigns a user to an activity successfully
    And the user adds an activity named "21" with budget 10, start year 2025, start week 5, end year 2025, end week 6 to project 25000
    When the user assigns "sore" to the activity "21" in project 25000
    Then the user "sore" is assigned to the activity "21" in the project with ID 25000
    And no error message is shown

  Scenario: User fails to assign a user to an activity when they already have 20 activities
    Given the user adds an activity named "21" with budget 10, start year 2025, start week 1, end year 2025, end week 1 to project 25000
    When the user assigns "sore" to the activity "21" in project 25000
    Then an error message "User has already 20 Activities in one or more weeks during this period" is shown

  Scenario: User retrieves only available users for a time period
    When the user requests the list of available users from year 2025 week 2 to year 2026 week 1
    Then the list of available users includes "huba"
    And the list of available users does not include "sore"

Scenario: User fails to retrieve only available users for a time period
    When the user requests the list of available users from year 2025 week 1 to year 2024 week 1
    Then an error message "End time cannot be before start time" is shown

  Scenario: User fails to retrieve only available users for start time before end time
    When the user requests the list of available users from year 2025 week 1 to year 2025 week -1
    Then an error message "Weeks must be between 1 and 52" is shown

    Scenario: User retrieves only available users for activity
        When the user requests the list of available users to the activity "20" in project 25000
        Then the list of available users includes "huba"
        And the list of available users does not include "sore"

      Scenario: User retrieves list of vacant users
        When the user requests the list of vacant users from year 2025 week 1 to year 2025 week 1
        Then the list of vacant users includes "huba"

      Scenario: User fails to retrieve only vacant users for a time period
        When the user requests the list of vacant users from year 2025 week 1 to year 2025 week -1
        Then an error message "Weeks must be between 1 and 52" is shown

  Scenario: User fails to retrieve only vacant users for a time period
    When the user requests the list of vacant users from year 2025 week 1 to year 2024 week 1
    Then an error message "End time cannot be before start time" is shown


    Scenario: User gets a list of all activities assigned to user
      When the user requests the list all activities assigned to the user "sore"
      Then the list of activities assigned to the user "sore" includes "1"


  Scenario: User generates a project report with activities and used hours
    When the user generates a report for project 25000
    Then the report contains "Report for project: default"
    And the report contains "Activity: 1"
    And the report contains "Activity: 2"


  Scenario: User generates a project report with activities and used hours
    When the user generates a report for project 30000
    Then an error message "Project does not exist" is shown
