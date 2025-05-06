package hellocucumber;

import app.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.*;

public class registerTimeSteps {
    private Project project;
    private ErrorMessageHolder errorMessageHolder;
    private PM_App app;
    private MockDateServer mockDateServer;




    public registerTimeSteps(PM_App app, ErrorMessageHolder errorMessageHolder, MockDateServer mockDateServer) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
        this.mockDateServer = mockDateServer;

    }


    // scenario 1: User registers time spent on an activity successfully

    @When("the user {string} registers {int} hours spent on date {string}")
    public void the_user_registers_hours_spent_on_date(String name, int hours, String date) throws OperationNotAllowedException {

    }


    @Then("the system records {int} hours for {string} by {string} on {string} for the user {string}")
    public void the_system_records_hours_for_by_on_for_the_user(int hours, String activityName, String initials, String date, String name) throws OperationNotAllowedException {

    }


    // scenario 2: User tries to register time for an activity they are not assigned to

    @Given("the user {string} is not assigned to it")
    public void the_user_is_not_assigned_to_it(String name) {
        // Assuming the user is not assigned to any activity
        //assertNull(app.getUserByID(name));
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

    // bruges også til scenario 3
    // bruges også til scenario 4
    // bruges også til scenario 5
    // bruges også til scenario 6
    // bruges også til scenario 7
    // bruges også til scenario 8
    @When("the user {string} tries to register {int} hours spent on {string} on date {string}")
    public void the_user_tries_to_register_hours_spent_on_on_date(String name, int hours, String activityName, String date) {
        // Assuming the user is not assigned to any activity
        //assertNull(app.getUserByID(name));
    }


    // bruges også til scenario 3
    // bruges også til scenario 4
    // bruges også til scenario 5
    // bruges også til scenario 6
    // bruges også til scenario 7
    @Then("an error message {string} should be shown")
    public void an_error_message_should_be_shown(String errorMessage) {
        // Assuming the error message is stored in the errorMessageHolder
        assertEquals(errorMessage, errorMessageHolder.getErrorMessage());
    }


    // scenario 3: User registers time with invalid hours (oppe)


    // scenario 4: User registers time with invalid date (oppe)

    // scenario 5: User registers time with missing hours (oppe)

    // scenario 6: User registers time with missing date (oppe)

    // scenario 7: User registers time with missing user (oppe)

    // scenario 8: User registers time with non-0.5 hour value (oppe)
    @Then("the system rounds the hours up to {string} hours")
    public void the_system_rounds_the_hours_up_to_hours(String hours) {
        // Assuming the system rounds the hours up to the nearest 0.5
        assertEquals(hours, errorMessageHolder.getErrorMessage());
    }

    @Then("the system records {string} hours for {string} on {string} for the user {string}")
    public void the_system_records_hours_for_on_for_the_user(String hours, String activityName, String initials, String date, String name) {
        // Assuming the system records the hours for the user
        assertEquals(hours, errorMessageHolder.getErrorMessage());
    }


    //test for the date server



    @Given("the system date is mocked")
    public void the_system_date_is_mocked() {
        mockDateServer.setDate(LocalDate.of(2025, 5, 3));
        app.setDateServer(mockDateServer);
        System.out.println("Test");
    }

    @Then("the system date should be {string}")
    public void the_system_date_should_be(String expectedDate) {
        String actualDate = app.getDateServer().dateToString(app.getDateServer().getDate());
        assertEquals(expectedDate, actualDate);
    }


    @Given("there exists users with the following initials in the project")
    public void there_exists_users_with_the_following_initials_in_the_project(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new io.cucumber.java.PendingException();
    }
    @Given("the user creates an activity with the following informations")
    public void the_user_creates_an_activity_with_the_following_informations(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new io.cucumber.java.PendingException();
    }





}
