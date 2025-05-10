package hellocucumber;

import app.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.*;

public class EditActivitySteps {

    private final PM_App app;
    private final ErrorMessageHolder errorMessageHolder;

    public EditActivitySteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
    }

    @Given("a project with name {string} and ID {int} exists")
    public void a_project_with_name_and_id_exists(String name, Integer id) {
        try {
            Project project = new Project(name, "client");
            project.setProjectID(id);
            app.createProject(project);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Given("the user adds an activity with name {string}, budgeted time {int} hours, start week {int}, end week {int} to project with ID {int}")
    public void the_user_adds_an_activity_with_name_budgeted_time_hours_start_week_end_week_to_project_with_id(String name, Integer time, Integer start, Integer end, Integer id) {
        try {
            Activity activity = new Activity(name, time, start, end);
            app.addActivityToProject(id, activity);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the name of activity {string} in project {int} to {string}")
    public void the_user_edits_the_name_of_activity_in_project_to(String oldName, Integer projectID, String newName) {
        try {
            app.getActivityByName(oldName, projectID).setName(newName);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} exists in project with ID {int}")
    public void the_activity_exists_in_project_with_id(String name, Integer projectID) throws OperationNotAllowedException {
        assertEquals(name, app.getActivityByName(name, projectID).getName());
    }

    @When("the user edits the budgeted time of activity {string} in project {int} to {int}")
    public void the_user_edits_the_budgeted_time_of_activity_in_project_to(String name, Integer projectID, Integer newTime) {
        try {
            app.getActivityByName(name, projectID).setBudgetTime(newTime);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} in project {int} has budgeted time {int}")
    public void the_activity_in_project_has_budgeted_time(String name, Integer projectID, Integer expected) throws OperationNotAllowedException {
        assertEquals(expected, app.getActivityByName(name, projectID).getBudgetTime());
    }

    @When("the user edits the start week of activity {string} in project {int} to {int}")
    public void the_user_edits_the_start_week_of_activity_in_project_to(String name, Integer projectID, Integer newStart) {
        try {
            app.getActivityByName(name, projectID).setStartWeek(newStart);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} in project {int} has start week {int}")
    public void the_activity_in_project_has_start_week(String name, Integer projectID, Integer expected) throws OperationNotAllowedException {
        assertEquals(expected, app.getActivityByName(name, projectID).getStartWeek());
    }

    @When("the user edits the end week of activity {string} in project {int} to {int}")
    public void the_user_edits_the_end_week_of_activity_in_project_to(String name, Integer projectID, Integer newEnd) {
        try {
            app.getActivityByName(name, projectID).setEndWeek(newEnd);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} in project {int} has end week {int}")
    public void the_activity_in_project_has_end_week(String name, Integer projectID, Integer expected) throws OperationNotAllowedException {
        assertEquals(expected, app.getActivityByName(name, projectID).getEndWeek());
    }
}
