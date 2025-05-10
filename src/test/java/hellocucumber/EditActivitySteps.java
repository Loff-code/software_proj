package hellocucumber;

import app.*;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class EditActivitySteps {

    private final PM_App app;
    private final ErrorMessageHolder errorMessageHolder;

    public EditActivitySteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
    }



    @Given("the user adds an activity named {string} with budget {int}, start week {int}, end week {int} to project {int}")
    public void the_user_adds_an_activity_named(String name, int budget, int start, int end, int projectID) {
        try {
            if (app.getLoggedInUserID() == null) {
                throw new IllegalStateException("No user is logged in");
            }
            Activity activity = new Activity(name, budget, start, end);
            app.addActivityToProject(projectID, activity);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the name of activity {string} in project {int} to {string}")
    public void the_user_edits_the_name_of_activity(String oldName, int projectID, String newName) {
        try {
            app.getActivityByName(oldName, projectID).setName(newName);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the budgeted time of activity {string} in project {int} to {int}")
    public void the_user_edits_budgeted_time(String name, int projectID, int newBudget) {
        try {
            app.getActivityByName(name, projectID).setBudgetTime(newBudget);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the start week of activity {string} in project {int} to {int}")
    public void the_user_edits_start_week(String name, int projectID, int newStartWeek) {
        try {
            app.getActivityByName(name, projectID).setStartWeek(newStartWeek);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the end week of activity {string} in project {int} to {int}")
    public void the_user_edits_end_week(String name, int projectID, int newEndWeek) {
        try {
            app.getActivityByName(name, projectID).setEndWeek(newEndWeek);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} exists in project with ID {int}")
    public void the_activity_exists_in_project(String name, int projectID) throws OperationNotAllowedException {
        assertEquals(name, app.getActivityByName(name, projectID).getName());
    }

    @Then("the activity {string} in project {int} has budgeted time {int}")
    public void the_activity_has_budgeted_time(String name, int projectID, int expectedBudget) throws OperationNotAllowedException {
        assertEquals(expectedBudget, app.getActivityByName(name, projectID).getBudgetTime());
    }

    @Then("the activity {string} in project {int} has start week {int}")
    public void the_activity_has_start_week(String name, int projectID, int expectedStartWeek) throws OperationNotAllowedException {
        assertEquals(expectedStartWeek, app.getActivityByName(name, projectID).getStartWeek());
    }

    @Then("the activity {string} in project {int} has end week {int}")
    public void the_activity_has_end_week(String name, int projectID, int expectedEndWeek) throws OperationNotAllowedException {
        assertEquals(expectedEndWeek, app.getActivityByName(name, projectID).getEndWeek());
    }

    @Then("an error message {string} is shown")
    public void an_error_message_is_shown(String expectedMessage) {
        assertEquals(expectedMessage, errorMessageHolder.getErrorMessage());
    }
}
