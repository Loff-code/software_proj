Feature: Find vacant Workers

  Scenario: Find available employees when some are free
    Given the following users are registered
      | alic |
      | bob   |
    And the user "alic" is assigned to an activity from week 10 to 15
    When the user searches for available employees between week 15 and week 20
    Then the available employees list should contain "alic"
    Then the available employees list should contain "bob"

  Scenario: No available employees when all are busy
    Given the following users are registered
      | char |
      | dave    |
    And the user "char" is assigned to an activity from week 10 to 20
    And the user "dave" is assigned to an activity from week 12 to 22
    When the user searches for available employees between week 15 and week 18
    Then the available employees list should not contain "char"
    Then the available employees list should not contain "dave"
