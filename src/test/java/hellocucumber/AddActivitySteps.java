package hellocucumber;

import app.Activity;
import app.OperationNotAllowedException;
import app.PM_App;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddActivitySteps {

    private final ErrorMessageHolder errorMessageHolder;
    private final PM_App app;

    public AddActivitySteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.errorMessageHolder = errorMessageHolder;
        this.app = app;
    }

    @Then("the activity {string} is added to project with ID {int}")
    public void theActivityIsAddedToProjectWithID(String activityName, int projectID) throws OperationNotAllowedException {
        assertEquals(activityName, app.getActivityByName(activityName, projectID).getName());
    }

    @When("the user adds an activity with name {string}, budgeted time {int} hours, start year {int}, start week {int}, end year {int}, end week {int} to project with ID {int}")
    public void theUserAddsAnActivityWithNameBudgetedTimeAndYearsToProject(
            String activityName,
            int budgetHours,
            int startYear,
            int startWeek,
            int endYear,
            int endWeek,
            int projectID
    ) {
        try {
            Activity activity = new Activity(activityName, budgetHours, startYear, startWeek, endYear, endWeek);
            app.addActivityToProject(projectID, activity);
        } catch (OperationNotAllowedException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }
}
