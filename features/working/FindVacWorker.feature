Feature: Find vacant workers
  Background:
    Given the user is logged in

  Scenario: Find available employees when some are free
    Given the following users are registered
      | alic |
      | bob  |
    And the user "alic" is assigned to an activity from start year 2025, start week 10 to end year 2025, end week 15
    When the user searches for available employees between start year 2025, start week 16 and end year 2025, end week 20
    Then the available employees list should contain "alic"
    Then the available employees list should contain "bob"
    And no error message is shown

  Scenario: No available employees when all are busy
    Given the following users are registered
      | char |
      | dave |
    And the user "char" is assigned to an activity from start year 2025, start week 10 to end year 2025, end week 20
    And the user "dave" is assigned to an activity from start year 2025, start week 12 to end year 2025, end week 22
    When the user searches for available employees between start year 2025, start week 15 and end year 2025, end week 18
    Then the available employees list should not contain "char"
    Then the available employees list should not contain "dave"
    And no error message is shown
