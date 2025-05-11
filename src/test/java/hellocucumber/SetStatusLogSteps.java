package hellocucumber;

import app.*;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class SetStatusLogSteps {

    private final PM_App app;
    private final ErrorMessageHolder errorMessageHolder;
    private Project project;
    private Activity activity;
    private static final int DEFAULT_YEAR = 2025;

    public SetStatusLogSteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
    }

    // === User SCENARIO ===

    @Given("that the User is logged in")
    public void that_the_User_is_logged_in() throws OperationNotAllowedException {
        app.login("huba");
        app.createUser(new User("emp"));
        app.logout();
        app.login("emp");
    }

    @Given("the user is assigned to an activity named {string} in project with ID {int}")
    public void the_User_is_assigned_to_an_activity_named(String activityName, int projectID) throws OperationNotAllowedException {
        project = new Project("TestProject", "TestClient");
        activity = new Activity(activityName, 10, DEFAULT_YEAR, 1, DEFAULT_YEAR, 10);
        activity.assignEmployeeToActivity("emp");
        app.createProject(project);
        app.addActivityToProject(projectID, activity);
    }

    @When("the User sets the status of {string} to {string} in project with ID {int}")
    public void the_User_sets_the_status_of_to(String activityName, String status, int projectID) {
        try {
            app.setStatusOfActivity(activityName, projectID, status);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the status of {string} is updated to {string} in project with ID {int}")
    public void the_status_of_is_updated_to(String activityName, String expectedStatus, int projectID) throws OperationNotAllowedException {
        assertEquals(expectedStatus.toLowerCase(), app.getActivityByName(activityName, projectID).getStatus().toLowerCase());
    }

    @Then("a log entry is created with the new status")
    public void a_log_entry_is_created_with_the_new_status() throws OperationNotAllowedException {
        String expected = "Status changed to: " + app.getActivityByName(activity.getName(), project.getProjectID()).getStatus() + " by " + app.getLoggedInUserID();
        assertTrue(app.getActivityByName(activity.getName(), project.getProjectID()).getLog().stream().anyMatch(log -> log.equals(expected)),
                "Expected log not found: " + expected);
    }

    // === UNAUTHORIZED User SCENARIO ===

    @Given("there is an activity named {string} that the user is not assigned to in project with ID {int}")
    public void there_is_an_activity_named_that_the_User_is_not_assigned_to(String activityName, int projectID) throws OperationNotAllowedException {
        project = new Project("UnassignedProject", "Client");
        activity = new Activity(activityName, 10, DEFAULT_YEAR, 1, DEFAULT_YEAR, 10);
        app.createProject(project);
        app.addActivityToProject(projectID, activity);
    }

    @When("the user tries to set the status of {string} to {string} in project with ID {int}")
    public void the_User_tries_to_set_the_status_of_to(String activityName, String status, int projectID) {
        try {
            app.setStatusOfActivity(activityName, projectID, status);
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

    @Given("the project leader is logged in")
    public void that_the_project_leader_is_logged_in() throws OperationNotAllowedException {
        app.login("huba");
        app.createUser(new User("lead"));
        app.logout();
        app.login("lead");
    }

    @Given("there is an activity named {string} in their project with ID {int}")
    public void there_is_an_activity_named_in_their_project(String activityName, int projectID) throws OperationNotAllowedException {
        app.createUser(new User("adm"));
        app.login("adm");
        project = new Project("LeaderProject", "Client");
        app.createProject(project);
        app.assignProjectLeader(project.getProjectID(), "lead");
        activity = new Activity(activityName, 10, DEFAULT_YEAR, 1, DEFAULT_YEAR, 10);
        app.login("lead");
        app.addActivityToProject(projectID, activity);
    }

    @When("the project leader sets the status of {string} to {string} in project with ID {int}")
    public void the_project_leader_sets_the_status_of_to(String activityName, String status, int projectID) {
        try {
            app.setStatusOfActivity(activityName, projectID, status);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the system allow the status to change")
    public void theSystemAllowTheStatusToChange() {
        assertTrue(errorMessageHolder.getErrorMessage().isEmpty());
    }
}
