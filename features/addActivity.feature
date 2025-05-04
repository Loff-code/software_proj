Feature: Add activity to project
  Description: An employee adds an activity to a project in the system.
  Actor: Employee

  Scenario: Employee adds an activity successfully
    Given the user is logged in
    And project with name "P" exists
    Then the project is created with the name "P"
    When the user adds an activity with name "A", budgeted time 1 hours, start week 1, end week 4 to project "P"
    Then the activity "A" is added to project "P"
    #And an error message happens

  #Scenario: User adds an activity
   # Given that the employee Bob is logged in
   # Given that a project called "Cat"
   # When an activity called "Part1" is added
   # Then the activity is in the project "Cat"

  #Scenario: Employee adds an activity with an empty name
   # Given that the employee Bob is logged in
   # Given that a project called "Cat"
   # When an activity called "" is added
   # Then an error message happens

  #Scenario: Employee adds an activity which is already created
   # Given that the employee Bob is logged in
   # Given that a project called "Cat" is created
   # And an activity called "part1" is already present
   # When an activity called "part1" is added
   # Then an error message happens
