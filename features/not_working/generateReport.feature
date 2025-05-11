#læs 5.2 i projekt beskrivelsen

Feature: View status report for projects
  Description: Allows a user to generate a report that shows all project activities with used time, budgeted time, and their current status.
  Actor: User

  Background:
    Given the user is logged in
    Given that a project with ID 25000 and project name "P1" exists

    #And the project "P1" has a total budget of 100 hours
    # synes det er en god ide at når
    #man laver en project at man også siger hvor mange timer man forventer at bruge
    #på den. men de har ikke gjort det i createProject.feature.
    #så spørg søren om du skal gøre det


    When the user adds an activity with name "Demo", budgeted time 10 hours, start week 1, end week 4 to project "P1"
    When the user adds an activity with name "Design", budgeted time 30 hours, start week 4, end week 10 to project "P1"
    And the user "vict" is assigned to the activity "Demo" in project "P1" with ID 25000
    And the user "zoha" is assigned to the activity "Demo" in project "P1" with ID 25000
    And the user "vict" is assigned to the activity "Design" in project "P1" with ID 25000
    When the user "vict" registers 5.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    When the user "zoha" registers 5.0 hours spent on activity "Demo" on date "2025-02-07" in project with ID 25000
    When the user "vict" registers 6.0 hours spent on activity "Design" on date "2025-02-07" in project with ID 25000
    And the user sets the status of "Demo" to "Completed" in project with ID 25000
    And the user sets the status of "Design" to "In progress" in project with ID 25000
    Given the user "sore" is not already a leader of the project
    When the user assigns "sore" as the project leader to the project 25000
    Then the user "sore" is set as the leader of project 25000


  Scenario: Multiple users register time on the same activity
    When the user requests a status report for project "P1"
    Then the activity "Design" should show 8.0 used hours


  Scenario: User views report of ongoing activities and their hours
    When the user requests a status report for project "P1"
    Then the report should include:
      | ProjectName | ProjectID | ProjectLeader | Activity | Assigned users | Used hours | Budgeted hours | Status      | Start week | End week |
      | P1          | 25000     | sore          | Demo     | vict, zoha     | 10.0       | 10             | Completed   | 1          | 4        |
      | P1          | 25000     | sore          | Design   | vict           | 6.0        | 30             | In progress | 4          | 10       |


  Scenario: Report for project with no activities
    Given that a project with ID 30000 and project name "EmptyProject" exists
    When the user requests a status report for project "EmptyProject"
    Then the report should be empty


  Scenario: Report request for a non-existing project
    Given the user is logged in
    When the user requests a status report for project "P99999"
    Then an error message "Project not found" should be shown

