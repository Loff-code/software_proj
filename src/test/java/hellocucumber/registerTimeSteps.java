package hellocucumber;

import app.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import java.util.List;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.*;

public class registerTimeSteps {

    private ErrorMessageHolder errorMessageHolder;
    private PM_App app;

    private MockDateServer mockDateServer;




    public registerTimeSteps(PM_App app, ErrorMessageHolder errorMessageHolder, MockDateServer mockDateServer) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
        this.mockDateServer = mockDateServer;

        if (this.app.getDateServer() == null) {
            this.app.setDateServer(new RealDateServer());
        }


    }


    @And("the user {string} is assigned to the activity {string} in project {string}")
    public void theUserIsAssignedToTheActivityInProject(String userID, String activityName, String projectName) throws OperationNotAllowedException {
        app.assignActivityToUser(userID, activityName, projectName);
    }



    // scenario 1: User registers time spent on an activity successfully

    @When("the user {string} registers {double} hours spent on activity {string} on date {string} in project with ID {int}")
    public void the_user_registers_hours_spent_on_activity_on_date(String userID, Double hours, String activityName, String date, int projectID) throws OperationNotAllowedException {


        if (app.getDateServer() == null) {
            app.setDateServer(new RealDateServer());
        }

        if(date == null || date.isBlank()){
            errorMessageHolder.setErrorMessage("Not allowed: Date cannot be null");
            return;
        }

        if(userID.isBlank()){
            errorMessageHolder.setErrorMessage("Not allowed: User ID cannot be blank");
            return;
        }

        try {
            app.registerTimeForActivity(userID,projectID, activityName, hours, date);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }


    }




    // scenario 2: User tries to register time for an activity they are not assigned to

    @Given("the user {string} is not assigned to it")
    public void the_user_is_not_assigned_to_it(String name) {
        boolean isAssigned = false;
        for (Project project : app.getProjects()) {
            for (Activity activity : project.getActivities()) {
                if (activity.getAssignedUsers().contains(name)) {
                    isAssigned = true;
                }
            }
        }
        assertFalse(isAssigned);
    }


    @Then("an error message {string} should be shown")
    public void an_error_message_should_be_shown(String errorMessage) {
        assertEquals(errorMessage, errorMessageHolder.getErrorMessage());
    }



    @Then("the system records {double} hours for {string} on {string} for the user {string} in project with ID {int}")//hh
    public void the_system_records_hours_for_on_for_the_user(Double hours, String activityName, String date, String userID, int projectID) {
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




    // only for testing purposes


    @Then("the system date should be {string}")
    public void the_system_date_should_be(String expectedDate) {
        String actualDate = app.getDateServer().dateToString(app.getDateServer().getDate());
        assertEquals(expectedDate, actualDate);
    }



}
