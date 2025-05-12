Feature: Set status log
  Description: Allow setting the status of an activity and logging the change
  Actor: User / Project leader

  Scenario: User sets the status of their assigned activity
    Given the user is logged in
    And the user is assigned to an activity named "Coding" in project with ID 25000
    When the user sets the status of "Coding" to "Completed" in project with ID 25000
    Then the status of "Coding" is updated to "Completed" in project with ID 25000
    And a log entry is created with the new status
    And no error message is shown

  Scenario: User tries to set the status of an activity they are not assigned to
    Given the user is logged in
    And there is an activity named "Documentation" that the user is not assigned to in project with ID 25000
    When the user tries to set the status of "Documentation" to "In progress" in project with ID 25000
    Then the status of "Documentation" is updated to "In progress" in project with ID 25000
    And a log entry is created with the new status
    And no error message is shown

  Scenario: Project leader sets the status of any activity in their project
    Given the project leader is logged in
    And there is an activity named "Planning" in their project with ID 25000
    When the project leader sets the status of "Planning" to "Started" in project with ID 25000
    Then the status of "Planning" is updated to "Started" in project with ID 25000
    And a log entry is created with the new status
    And no error message is shown

