package hellocucumber;

import app.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class registerTimeSteps {

    private final ErrorMessageHolder errorMessageHolder;
    private final PM_App app;
    private final MockDateServer mockDateServer;

    public registerTimeSteps(PM_App app, ErrorMessageHolder errorMessageHolder, MockDateServer mockDateServer) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
        this.mockDateServer = mockDateServer;

        if (this.app.getDateServer() == null) {
            this.app.setDateServer(new RealDateServer());
        }
    }

    @And("the user {string} is assigned to the activity {string} in project {string} with ID {int}")
    public void theUserIsAssignedToTheActivityInProject(String userID, String activityName, String projectName, int projectID) throws OperationNotAllowedException {
        app.assignUserToActivity(userID, activityName, projectID);
    }

    @When("the user {string} registers {double} hours spent on activity {string} on date {string} in project with ID {int}")
    public void the_user_registers_hours_spent_on_activity_on_date(String userID, Double hours, String activityName, String date, int projectID) throws OperationNotAllowedException {
        if (app.getDateServer() == null) {
            app.setDateServer(new RealDateServer());
        }



        try {
            app.registerTimeForActivity(userID, projectID, activityName, hours, date);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Given("the user {string} is not assigned to it")
    public void the_user_is_not_assigned_to_it(String name) {
        boolean isAssigned = app.getProjects().stream()
                .flatMap(p -> p.getActivities().stream())
                .anyMatch(a -> a.getAssignedUsers().contains(name));

        assertFalse(isAssigned);
    }

    @Then("an error message {string} should be shown")
    public void an_error_message_should_be_shown(String errorMessage) {
        assertEquals(errorMessage, errorMessageHolder.getErrorMessage());
    }

    @Then("the system records {double} hours for {string} on {string} for the user {string} in project with ID {int}")
    public void the_system_records_hours_for_on_for_the_user(Double hours, String activityName, String date, String userID, int projectID) throws OperationNotAllowedException {
        Activity activity = app.getActivityByName(activityName, projectID);
        String parsedDate = app.getDateServer().dateToString(app.getDateServer().parseDate(date));
        String key = "[" + userID + "]|" + activityName + "|" + parsedDate;

        Map<String, Double> timeMap = activity.getTimeMap();

        assertTrue(timeMap.containsKey(key), "Expected time entry not found");
        assertEquals(hours, timeMap.get(key));
    }

    @Given("the system date is mocked")
    public void the_system_date_is_mocked() {
        mockDateServer.setDate(LocalDate.of(2025, 2, 6));
        app.setDateServer(mockDateServer);
    }

    @And("printsStuff {string}")
    public void printsstuff(String userID) {
        app.getUsersEntriesForToday(userID); // This can print/log if needed
    }
}
