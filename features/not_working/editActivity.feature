Feature: Edit activity attributes

  Background:
    Given the user with the ID "huba" exists
    And the user logs in with the ID "huba"
    And the user with the ID "huba" is now logged in
    And that a project with ID 25000 exists
    And the user adds an activity with name "a", budgeted time 10 hours, start week 5, end week 6 to project with ID 25000

  Scenario: User changes activity name
    When the user edits the name of activity "a" in project 25000 to "b"
    Then the activity "b" exists in project with ID 25000

  Scenario: User changes activity budgeted time
    When the user edits the budgeted time of activity "a" in project 25000 to 40
    Then the activity "a" in project 25000 has budgeted time 40

  Scenario: User changes activity start week
    When the user edits the start week of activity "a" in project 25000 to 7
    Then the activity "a" in project 25000 has start week 7

  Scenario: User changes activity end week
    When the user edits the end week of activity "a" in project 25000 to 10
    Then the activity "a" in project 25000 has end week 10
