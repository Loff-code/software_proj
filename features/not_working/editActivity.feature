Feature: Edit activity


  Background:
    Given project with name "P" exists
    And the user adds an activity with name "A", budgeted time 80 hours, start week 10, end week 12 to project "P"


  Scenario: User edits an activity successfully
    When the user edits activity "A" in project "P" to name "B", budgeted time 100 hours, start week 11, end week 13
    Then the activity "B" exists in project "P" with budgeted time 100 hours, start week 11, end week 13


  Scenario: User edits activity without changing name
    When the user edits activity "A" in project "P" to name "A", budgeted time 90 hours, start week 12, end week 14
    Then the activity "A" exists in project "P" with budgeted time 90 hours, start week 12, end week 14


  Scenario: Editing activity fails because project does not exist
    When the user edits activity "A" in project "K" to name "B", budgeted time 50 hours, start week 1, end week 2
    Then an error message "Not allowed: Project does not exist" should be shown


  Scenario: Editing activity fails because activity does not exist
    When the user edits activity "X" in project "P" to name "B", budgeted time 100 hours, start week 11, end week 13
    Then an error message "Not allowed: Activity not found" should be shown


  Scenario: Editing activity fails because new name already exists
    Given the user adds an activity with name "D", budgeted time 80 hours, start week 10, end week 12 to project "P"
    And the user adds an activity with name "E", budgeted time 45 hours, start week 8, end week 10 to project "P"
    When the user edits activity "D" in project "P" to name "E", budgeted time 50 hours, start week 1, end week 2
    Then an error message "Not allowed: Activity with the new name already exists" should be shown

