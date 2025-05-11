package hellocucumber;

import app.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

public class FindVacWorkerSteps {

    private final ErrorMessageHolder errorMessageHolder;
    private final PM_App app;
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
                if ("User ID is already taken".equals(errorMessageHolder.getErrorMessage())) {
                    errorMessageHolder.setErrorMessage("");
                }
            }
        }
    }


    @And("the user {string} is assigned to an activity from start year {int}, start week {int} to end year {int}, end week {int}")
    public void theUserIsAssignedToAnActivityFromStartYearStartWeekToEndYearEndWeek(String userID, Integer startYear, Integer startWeek, Integer endYear, Integer endWeek) throws OperationNotAllowedException {
        Project project = new Project("TestProject", "TestClient");
        Activity activity = new Activity("TestActivity", 10, startYear, startWeek, endYear, endWeek);
        project.getActivities().add(activity);
        app.getProjects().add(project);
        app.assignUserToActivity(userID, activity.getName(), project.getProjectID());
    }
    @When("the user searches for available employees between start year {int}, start week {int} and end year {int}, end week {int}")
    public void the_user_searches_for_available_employees_between_year_week_and_year_week(Integer startYear, Integer startWeek, Integer endYear, Integer endWeek) throws OperationNotAllowedException {
        foundEmployees = app.getVacantUserIDs(startYear, startWeek, endYear, endWeek);
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
