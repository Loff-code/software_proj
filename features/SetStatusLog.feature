Feature: Set status log
  Description: Allow setting the status of an activity and logging the change
  Actor: Employee / Project leader

  Scenario: Employee sets the status of their assigned activity
    Given that the employee is logged in
    And the employee is assigned to an activity named "Coding"
    When the employee sets the status of "Coding" to "Completed"
    Then the status of "Coding" is updated to "completed"
    And a log entry is created with the new status

  Scenario: Employee tries to set the status of an activity they are not assigned to
    Given that the employee is logged in
    And there is an activity named "Documentation" that the employee is not assigned to
    When the employee tries to set the status of "Documentation" to "in progress"
    Then the system does not allow the status to change
    And an error message happens

  Scenario: Project leader sets the status of any activity in their project
    Given that the project leader is logged in
    And there is an activity named "Planning" in their project
    When the project leader sets the status of "Planning" to "started"
    Then the status of "Planning" is updated to "started"
    And a log entry is created with the new status
