package hellocucumber;

import app.Activity;
import app.OperationNotAllowedException;
import app.PM_App;
import app.Project;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class SetStatusLogSteps {

    private ErrorMessageHolder errorMessageHolder;
    private PM_App app;
    private Project project;
    private Activity activity;

    public SetStatusLogSteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.errorMessageHolder = errorMessageHolder;
        this.app = app;
    }

    @Given("that the employee is logged in")
    public void that_the_employee_is_logged_in() throws OperationNotAllowedException {
        app.createUser("employee");
        app.login("employee");
    }

    @Given("the employee is assigned to an activity named {string}")
    public void the_employee_is_assigned_to_an_activity_named(String activityName) {
        project = new Project("TestProject", "TestClient");
        activity = new Activity(activityName, 10, 1, 10);
        activity.assignEmployeeToActivity(app.getUserID());
        project.getActivities().add(activity);
        app.getProject().add(project);
    }

    @When("the employee sets the status of {string} to {string}")
    public void the_employee_sets_the_status_of_to(String activityName, String status) throws OperationNotAllowedException {
        try {
            app.setStatusOfActivity(activityName, status);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the status of {string} is updated to {string}")
    public void the_status_of_is_updated_to(String activityName, String expectedStatus) {
        assertEquals(expectedStatus.toLowerCase(), app.getActivityByName(activityName).getStatus().toLowerCase());
    }

    @Then("a log entry is created with the new status")
    public void a_log_entry_is_created_with_the_new_status() {
        assertTrue(app.getActivityByName(activity.getName()).getLog().contains("Status changed to: " + app.getActivityByName(activity.getName()).getStatus()));
    }

    @Given("there is an activity named {string} that the employee is not assigned to")
    public void there_is_an_activity_named_that_the_employee_is_not_assigned_to(String activityName) {
        project = new Project("TestProject2", "Client");
        activity = new Activity(activityName, 10, 1, 10);
        // not assigning employee
        project.getActivities().add(activity);
        app.getProject().add(project);
    }

    @When("the employee tries to set the status of {string} to {string}")
    public void the_employee_tries_to_set_the_status_of_to(String activityName, String status) {
        try {
            app.setStatusOfActivity(activityName, status);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the system does not allow the status to change")
    public void the_system_does_not_allow_the_status_to_change() {
        assertTrue(errorMessageHolder.getErrorMessage().length() > 0);
    }

    @Then("an error message happens")
    public void an_error_message_happens() {
        assertTrue(errorMessageHolder.getErrorMessage().length() > 0);
    }

    @Given("that the project leader is logged in")
    public void that_the_project_leader_is_logged_in() throws OperationNotAllowedException {
        app.createUser("leader");
        app.login("leader");
    }

    @Given("there is an activity named {string} in their project")
    public void there_is_an_activity_named_in_their_project(String activityName) {
        project = new Project("LeaderProject", "TestClient");
        activity = new Activity(activityName, 10, 1, 10);
        project.getActivities().add(activity);
        app.getProject().add(project);
    }

    @When("the project leader sets the status of {string} to {string}")
    public void the_project_leader_sets_the_status_of_to(String activityName, String status) throws OperationNotAllowedException {
        try {
            app.setStatusOfActivity(activityName, status);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }
}
