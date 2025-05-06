package hellocucumber;

import app.*;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class SetStatusLogSteps {

    private final PM_App app;
    private final ErrorMessageHolder errorMessageHolder;
    private Project project;
    private Activity activity;

    public SetStatusLogSteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
    }

    // === EMPLOYEE SCENARIO ===

    @Given("that the employee is logged in")
    public void that_the_employee_is_logged_in() throws OperationNotAllowedException {
        app.createUser("emp");
        app.login("emp");
    }

    @Given("the employee is assigned to an activity named {string}")
    public void the_employee_is_assigned_to_an_activity_named(String activityName) {
        project = new Project("TestProject", "TestClient");
        activity = new Activity(activityName, 10, 1, 10);
        activity.assignEmployeeToActivity("emp");
        project.getActivities().add(activity);
        app.getProject().add(project); // VIGTIGT!
    }

    @When("the employee sets the status of {string} to {string}")
    public void the_employee_sets_the_status_of_to(String activityName, String status) {
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
        String expected = "Status changed to: " + app.getActivityByName(activity.getName()).getStatus() + " by " + app.getUserID();
        assertTrue(app.getActivityByName(activity.getName()).getLog().stream().anyMatch(log -> log.equals(expected)),
                "Expected log not found: " + expected);
    }


    // === UNAUTHORIZED EMPLOYEE SCENARIO ===

    @Given("there is an activity named {string} that the employee is not assigned to")
    public void there_is_an_activity_named_that_the_employee_is_not_assigned_to(String activityName) {
        project = new Project("UnassignedProject", "Client");
        activity = new Activity(activityName, 10, 1, 10);
        project.getActivities().add(activity);
        app.getProject().add(project); // VIGTIGT!
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

    // === PROJECT LEADER SCENARIO ===

    @Given("that the project leader is logged in")
    public void that_the_project_leader_is_logged_in() throws OperationNotAllowedException {
        app.createUser("lead");
        app.login("lead");
    }

    @Given("there is an activity named {string} in their project")
    public void there_is_an_activity_named_in_their_project(String activityName) throws OperationNotAllowedException {
        app.createUser("adm");
        app.login("adm");
        app.createProject("LeaderProject", "Client");
        app.assignProjectLeader("LeaderProject", "lead");

        project = app.getProjectByName("LeaderProject");
        activity = new Activity(activityName, 10, 1, 10);
        project.getActivities().add(activity); // VIGTIGT!
        app.login("lead");
    }

    @When("the project leader sets the status of {string} to {string}")
    public void the_project_leader_sets_the_status_of_to(String activityName, String status) {
        try {
            app.setStatusOfActivity(activityName, status);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }
}
