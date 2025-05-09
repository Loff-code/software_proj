Feature: Edit activity

  Scenario: User edits an activity successfully
    Given the user is logged in
    And project with name "A" exists
    And the user adds an activity with name "P", budgeted time 80 hours, start week 10, end week 12 to project "A"
    When the user edits activity "P" in project "A" to name "B", budgeted time 100 hours, start week 11, end week 13
    Then the activity "B" exists in project "A" with budgeted time 100 hours, start week 11, end week 13

  Scenario: User edits activity without changing name
    Given the user is logged in
    And project with name "A" exists
    And the user adds an activity with name "P", budgeted time 80 hours, start week 10, end week 12 to project "A"
    When the user edits activity "P" in project "A" to name "P", budgeted time 90 hours, start week 12, end week 14
    Then the activity "P" exists in project "A" with budgeted time 90 hours, start week 12, end week 14

  Scenario: Editing activity fails because project does not exist
    Given the user is logged in
    When the user edits activity "A" in project "K" to name "B", budgeted time 50 hours, start week 1, end week 2
    Then an error message "Project does not exist" should be shown

  Scenario: Editing activity fails because activity does not exist
    Given the user is logged in
    And project with name "P" exists
    When the user edits activity "X" in project "P" to name "B", budgeted time 100 hours, start week 11, end week 13
    Then an error message "Not allowed: Activity not found" should be shown

  Scenario: Editing activity fails because new name already exists
    Given the user is logged in
    And project with name "D" exists
    And the user adds an activity with name "D", budgeted time 80 hours, start week 10, end week 12 to project "D"
    And the user adds an activity with name "E", budgeted time 45 hours, start week 8, end week 10 to project "D"
    When the user edits activity "D" in project "D" to name "E", budgeted time 50 hours, start week 1, end week 2
    Then an error message "Not allowed: Activity with the new name already exists" should be shown

  Scenario: Editing activity in another project that does not exist
    Given the user is logged in
    When the user edits activity "A" in project "Z" to name "B", budgeted time 60 hours, start week 5, end week 6
    Then an error message "Project does not exist" should be shown
