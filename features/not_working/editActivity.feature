Feature: Edit activity


  Scenario: User edits an activity successfully
    Given that a project with ID 25001 exists
    And activity with name "A", budgeted time 80 hours, start week 10, end week 12 exists in project with ID 25001
    When the user edits activity "A" in project with ID 25001 to name "B", budgeted time 100 hours, start week 11, end week 13
    Then the activity "B" exists in project with ID 25001
    And the activity "B" in project with ID 25001 has budgeted time 100 hours, start week 11, end week 13

  Scenario: User edits activity without changing name
    Given that a project with ID 25001 exists
    And activity with name "C", budgeted time 60 hours, start week 9, end week 11 exists in project with ID 25001
    When the user edits activity "C" in project with ID 25001 to name "C", budgeted time 90 hours, start week 12, end week 14
    Then the activity "C" in project with ID 25001 has budgeted time 90 hours, start week 12, end week 14

  Scenario: Editing activity fails because project does not exist
    When the user edits activity "A" in project with ID 25002 to name "B", budgeted time 50 hours, start week 1, end week 2
    Then an error message occurs with text "Project not found"

  Scenario: Editing activity fails because activity does not exist
    Given that a project with ID 25001 exists
    When the user edits activity "X" in project with ID 25001 to name "Y", budgeted time 50 hours, start week 3, end week 5
    Then an error message occurs with text "Activity not found"

  Scenario: Editing activity fails because new name already exists
    Given that a project with ID 25001 exists
    And activity with name "D" exists in project with ID 25001
    And activity with name "E" exists in project with ID 25001
    When the user edits activity "D" in project with ID 25001 to name "E", budgeted time 50 hours, start week 6, end week 7
    Then an error message occurs with text "Activity with the new name already exists"
