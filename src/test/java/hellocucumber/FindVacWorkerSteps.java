package hellocucumber;

import app.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

public class FindVacWorkerSteps {

    private ErrorMessageHolder errorMessageHolder;
    private PM_App app;
    private List<String> foundEmployees;

    public FindVacWorkerSteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.errorMessageHolder = errorMessageHolder;
        this.app = app;
        this.foundEmployees = new ArrayList<>();
    }

    @Given("the following users are registered")
    public void the_following_users_are_registered(io.cucumber.datatable.DataTable dataTable) {
        List<String> userIDs = dataTable.asList();
        for (String userID : userIDs) {
            try {
                app.createUser(new User(userID));
            } catch (OperationNotAllowedException | IllegalArgumentException e) {
                errorMessageHolder.setErrorMessage(e.getMessage());
            }
        }
    }

    @Given("the user {string} is assigned to an activity from week {int} to {int}")
    public void the_user_is_assigned_to_an_activity_from_week_to_week(String userID, Integer startWeek, Integer endWeek) throws OperationNotAllowedException {
        Project project = new Project("TestProject", "TestClient");
        Activity activity = new Activity("TestActivity", 10, startWeek, endWeek);
        project.getActivities().add(activity);
        app.getProjects().add(project);
        app.assignUserToActivity(userID, activity.getName(), project.getProjectID());
    }

    @When("the user searches for available employees between week {int} and week {int}")
    public void the_user_searches_for_available_employees_between_week_and_week(Integer startWeek, Integer endWeek) {
        foundEmployees = app.getVacantUserIDs(startWeek, endWeek);
    }

    @Then("the available employees list should contain {string}")
    public void the_available_employees_list_should_contain(String userID) {
        assertTrue(foundEmployees.contains(userID));
    }

    @Then("the available employees list should not contain {string}")
    public void the_available_employees_list_should_not_contain(String userID) {
        assertFalse(foundEmployees.contains(userID));
    }
}
