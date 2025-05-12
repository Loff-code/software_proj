Feature: View status report for projects
  Description: Allows a user to generate a report that shows all project activities with used time, budgeted time, and their current status.
  Actor: User

  Background:
    Given the user is logged in
    Given that a project with ID 25000 and project name "P1" exists
    When the user adds an activity with name "Demo", budgeted time 10 hours, start year 2025, start week 1, end year 2025, end week 4 to project with ID 25000
    When the user adds an activity with name "Design", budgeted time 30 hours, start year 2025, start week 4, end year 2025, end week 10 to project with ID 25000
    And the user "vict" is assigned to the activity "Demo" in project "P1" with ID 25000
    And the user "zoha" is assigned to the activity "Demo" in project "P1" with ID 25000
    And the user "vict" is assigned to the activity "Design" in project "P1" with ID 25000
    When the user "vict" registers 5.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    When the user "zoha" registers 5.0 hours spent on activity "Demo" on date "2025-02-07" in project with ID 25000
    When the user "vict" registers 6.0 hours spent on activity "Design" on date "2025-02-07" in project with ID 25000
    And the user sets the status of "Demo" to "Completed" in project with ID 25000
    And the user sets the status of "Design" to "In progress" in project with ID 25000

  Scenario: User views report of ongoing activities and their hours
    When the user requests a status report from start year 2025, start week 1 to end year 2025, end week 10
    Then the report should include:
      | ProjectName | ProjectID | ProjectLeader | Activity | Assigned users | Used hours | Budgeted hours | Status      | Start week | End week |
      | P1          | 25000     | sore          | Demo     | vict, zoha     | 10.0       | 10             | Completed   | 1          | 4        |
      | P1          | 25000     | sore          | Design   | vict           | 6.0        | 30             | In progress | 4          | 10       |
    And no error message is shown

  Scenario: Report for project with no activities
    Given that a project with ID 30000 and project name "EmptyProject" exists
    And there are no activities in project with ID 30000
    When the user requests a status report from start year 2025, start week 1 to end year 2025, end week 10
    Then the report should not include:
      | EmptyProject | 30000  |
    And no error message is shown


  Scenario: Report for project with no assigned user in activity
    Given that a project with ID 30000 and project name "EmptyProject" exists
    When the user adds an activity with name "Demo", budgeted time 10 hours, start year 2025, start week 1, end year 2025, end week 4 to project with ID 30000
    When the user requests a status report from start year 2025, start week 1 to end year 2025, end week 10
    And no error message is shown


  Scenario: Report for project with no assigned user in activity
    Given that a project with ID 30000 and project name "EmptyProject" exists
    When the user adds an activity with name "Demo", budgeted time 10 hours, start year 2025, start week 1, end year 2025, end week 4 to project with ID 30000
    When the user requests a status report from start year 2025, start week 2 to end year 2026, end week 1
    And no error message is shown

