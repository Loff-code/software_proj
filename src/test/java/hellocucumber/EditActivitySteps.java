package hellocucumber;

import app.*;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

public class EditActivitySteps {

    private PM_App app;
    private ErrorMessageHolder errorMessageHolder;

    public EditActivitySteps() {
        this.app = new PM_App();
        this.errorMessageHolder = new ErrorMessageHolder();
    }

    private Activity addedActivity;
    private Project project;

    @Given("that a project with ID {int} exists")
    public void that_a_project_with_id_exists(Integer id) throws OperationNotAllowedException {
        project = new Project("Projectx", "DTU");
        project.setProjectID(id);
        app.createProject(project);

    }

    @Given("the user adds an activity named {string} with budget {int}, start week {int}, end week {int} to project {int}")
    public void the_user_adds_an_activity_named_with_budget_start_week_end_week_to_project(
            String activityName, Integer budgetTime, Integer startWeek, Integer endWeek, Integer projectID) {
        try {
            addedActivity = new Activity(activityName, budgetTime, startWeek, endWeek);
            app.addActivityToProject(projectID, addedActivity);
        } catch (OperationNotAllowedException | IllegalArgumentException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }





    @When("the user edits the name of activity {string} in project {int} to {string}")
    public void user_edits_name(String activityName, int projectID, String newName) {
        try {
            app.getActivityByName(activityName, projectID).setName(newName);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} exists in project with ID {int}")
    public void activity_exists(String name, int projectID) throws Exception {
        assertEquals(name, app.getActivityByName(name, projectID).getName());
    }

    @When("the user edits the budgeted time of activity {string} in project {int} to {int}")
    public void user_edits_budgeted_time(String activityName, int projectID, int newBudget) {
        try {
            app.getActivityByName(activityName, projectID).setBudgetTime(newBudget);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} in project {int} has budgeted time {int}")
    public void activity_has_budgeted_time(String activityName, int projectID, int expected) throws Exception {
        assertEquals(expected, app.getActivityByName(activityName, projectID).getBudgetTime());
    }

    @When("the user edits the start week of activity {string} in project {int} to {int}")
    public void user_edits_start_week(String activityName, int projectID, int newStartWeek) {
        try {
            app.getActivityByName(activityName, projectID).setStartWeek(newStartWeek);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} in project {int} has start week {int}")
    public void activity_has_start_week(String activityName, int projectID, int expectedStartWeek) throws Exception {
        assertEquals(expectedStartWeek, app.getActivityByName(activityName, projectID).getStartWeek());
    }

    @When("the user edits the end week of activity {string} in project {int} to {int}")
    public void user_edits_end_week(String activityName, int projectID, int newEndWeek) {
        try {
            app.getActivityByName(activityName, projectID).setEndWeek(newEndWeek);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} in project {int} has end week {int}")
    public void activity_has_end_week(String activityName, int projectID, int expectedEndWeek) throws Exception {
        assertEquals(expectedEndWeek, app.getActivityByName(activityName, projectID).getEndWeek());
    }

    @Then("an error message {string} is shown")
    public void error_message_shown(String expectedMessage) {
        assertEquals(expectedMessage, errorMessageHolder.getErrorMessage());
    }
}
