Feature: Register used time
  Description: Allows the user to register the time they have spent on activities.
  Actor: User

  Background:
    Given the user is logged in
    And project with name "P" exists
    Given the following users are registered
      | huba     |
      | sore     |
      | vict     |
      | hamz     |
      | zoha     |
    And the user adds an activity with name "Demo", budgeted time 100 hours, start week 3, end week 6 to project "P"
    And the user "vict" is assigned to the activity "Demo" in project "P" with ID 25000



  Scenario: User registers time spent on an activity successfully
    Given the system date is mocked
    When the user "vict" registers 4.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    Then the system records 4.0 hours for "Demo" on "2025-02-06" for the user "vict" in project with ID 25000
    And no error message is shown


  Scenario: User tries to register time for an activity they are not assigned to
    Given the user "huba" is not assigned to it
    Given the system date is mocked
    When the user "huba" registers 4.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    And an error message "Not allowed: User is not assigned to this activity" should be shown

  Scenario: User registers time with invalid hours
    Given the system date is mocked
    When the user "vict" registers -3.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    And an error message "Not allowed: Hours have to be positive" should be shown


  Scenario: User registers time with missing date
    When the user "vict" registers 4.0 hours spent on activity "Demo" on date "" in project with ID 25000
    Then an error message "Not allowed: Date cannot be null" should be shown

  Scenario: User registers time with missing user
    Given the system date is mocked
    When the user "" registers 4.0 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    Then an error message "Not allowed: User ID cannot be blank" should be shown


  Scenario: User registers time with non-0.5 hour value
    Given the system date is mocked
    When the user "vict" registers 5.4 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    Then the system records 5.5 hours for "Demo" on "2025-02-06" for the user "vict" in project with ID 25000
    And no error message is shown


  Scenario: User registers time with non-0.5 hour value
    Given the system date is mocked
    When the user "vict" registers 5.2 hours spent on activity "Demo" on date "2025-02-06" in project with ID 25000
    Then the system records 5.0 hours for "Demo" on "2025-02-06" for the user "vict" in project with ID 25000
    And no error message is shown




