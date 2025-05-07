Feature: Test date

  Scenario: Test mock date is returned
    Given the system date is mocked
    Then the system date should be "2025-05-03"


    Scenario: test
    Given there exists users with the following initials in the project
      | huba     |
      | sore     |
      | vict     |
      | hamz     |
      | zoha     |