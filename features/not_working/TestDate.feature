Feature: Test date

  Scenario: Test mock date is returned
    Given the system date is mocked
    Then the system date should be "2025-05-03"


