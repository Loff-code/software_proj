Feature: Edit activity attributes

  Background:
    Given the user is logged in

  Scenario: User changes activity name
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    When the user edits the name of activity "a" in project 25000 to "b"
    Then the activity "b" exists in project with ID 25000
    And no error message is shown

  Scenario: User changes budgeted time
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    When the user edits the budgeted time of activity "a" in project 25000 to 40
    Then the activity "a" in project 25000 has budgeted time 40
    And no error message is shown

  Scenario: User changes start week
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    When the user edits the start week of activity "a" in project 25000 to 7
    Then the activity "a" in project 25000 has start week 7
    And no error message is shown

  Scenario: User changes end week
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    When the user edits the end week of activity "a" in project 25000 to 10
    Then the activity "a" in project 25000 has end week 10
    And no error message is shown

  # FEJLTESTS

  Scenario: End week before start week
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    When the user edits the end week of activity "a" in project 25000 to 4
    Then an error message "End week cannot be before start week" is shown

  Scenario: Negative start week
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    When the user edits the start week of activity "a" in project 25000 to -4
    Then an error message "Start week cannot be negative" is shown

  Scenario: Negative end week
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    When the user edits the end week of activity "a" in project 25000 to -4
    Then an error message "End week cannot be negative" is shown

  Scenario: Empty name
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    When the user edits the name of activity "a" in project 25000 to ""
    Then an error message "Name cannot be blank" is shown

  Scenario: Negative budget time
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    When the user edits the budgeted time of activity "a" in project 25000 to -40
    Then an error message "Budget time cannot be negative" is shown

  Scenario: Edit name in non-existent project
    When the user edits the name of activity "a" in project 25001 to "b"
    Then an error message "Project does not exist" is shown

  Scenario: Edit start week in non-existent project
    When the user edits the start week of activity "a" in project 25001 to 4
    Then an error message "Project does not exist" is shown

  Scenario: Edit end week in non-existent project
    When the user edits the end week of activity "a" in project 25001 to 4
    Then an error message "Project does not exist" is shown

  Scenario: Edit budgeted time in non-existent project
    When the user edits the budgeted time of activity "a" in project 25001 to 4
    Then an error message "Project does not exist" is shown

  Scenario: User fails to assign a user to an activity twice
    Given that a project with ID 25000 exists
    And the user adds an activity named "a" with budget 10, start week 5, end week 6 to project 25000
    Given the following users are registered
      | huba     |
      | sore     |
      | vict     |
      | hamz     |
      | zoha     |
    And the user "huba" is not already assigned to the activity "a" in project 25000
    When the user assigns "huba" to the activity "a" in project 25000
    And the user assigns "huba" to the activity "a" in project 25000
    Then an error message "User is already assigned to this activity" is shown
