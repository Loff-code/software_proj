package hellocucumber;

import app.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateReportSteps {

    private final PM_App pmApp;
    private final ErrorMessageHolder errorMessageHolder;
    private Project project;
    private String report;

    public GenerateReportSteps(PM_App pmApp, ErrorMessageHolder errorMessageHolder) {
        this.pmApp = pmApp;
        this.errorMessageHolder = errorMessageHolder;
    }

    @Given("that a project with ID {int} and project name {string} exists")
    public void projectWithIDAndNameExists(int projectID, String projectName) throws OperationNotAllowedException {
        Project newProject = new Project(projectName, "Client");
        pmApp.createProject(newProject);
        newProject.setProjectID(projectID);
        project = pmApp.getProject(projectID);
        assertNotNull(project);
    }

    @Then("the user {string} should see the registered times for activity {string} in project with ID {int}")
    public void the_user_should_see_the_registered_times_for_activity_in_project_with_id(String userID, String activityName, int projectID) throws OperationNotAllowedException {
        List<String> entries = pmApp.getTimeEntriesForUser(userID, activityName, projectID);
        for (String entry : entries) {
            System.out.println(entry);
        }
        assertFalse(entries.isEmpty(), "No time entries found for user " + userID);
    }

    @Then("the user {string} should see the following registered time entries for activity {string} in project with ID {int}:")
    public void the_user_should_see_the_following_registered_time_entries_for_activity_in_project_with_id(String userID, String activityName, int projectID, DataTable expectedTable) throws OperationNotAllowedException {
        List<List<String>> expectedRows = expectedTable.asLists();
        List<String> actualEntries = pmApp.getTimeEntriesForUser(userID, activityName, projectID);

        for (List<String> row : expectedRows) {
            String date = row.get(0).trim();
            String hours = row.get(1).trim();
            String expectedLine = "[" + userID + "]|" + activityName + "|" + date + " = " + hours;
            assertTrue(actualEntries.contains(expectedLine), "Expected to find: " + expectedLine);
            System.out.println(expectedLine);
        }

    }


    @When("the user requests a status report for project {string} between start year {int}, start week {int} and end year {int}, end week {int}")
    public void theUserRequestsStatusReportForProjectBetweenFullRange(String projectName, int startYear, int startWeek, int endYear, int endWeek) {
        report = pmApp.getStatusReport(startYear, startWeek, endYear, endWeek);
    }

    @When("the user requests a status report for project {string}")
    public void theUserRequestsAStatusReportForProject(String projectName) {
        report = pmApp.getStatusReport(2025, 1, 2025, 52);
    }

    @Then("the report should be empty")
    public void theReportShouldBeEmpty() {
        assertEquals("Report: Project Status Report (Weeks 1 - 52)\n" +
                        "---------------------------------------------------------------",
                pmApp.getStatusReport(2025, 1, 2025, 52));
    }

    @Then("the activity {string} should show {double} used hours")
    public void theActivityShouldShowUsedHours(String activityName, double expectedUsedHours) {
        assertTrue(report.contains(activityName), "Activity " + activityName + " not found in the report.");
        assertTrue(report.contains(String.valueOf(expectedUsedHours)),
                "The expected used hours (" + expectedUsedHours + ") for activity " + activityName + " are not found in the report.");
    }

    @Then("the report should include:")
    public void theReportShouldInclude(DataTable expectedTable) {
        for (Map<String, String> row : expectedTable.asMaps()) {
            String activity = row.get("Activity");
            String status = row.get("Status");
            String budgetedHours = row.get("Budgeted hours");
            String usedHours = row.get("Used hours");
            String assignedUsers = row.get("Assigned users").replace(",", "").trim();

            String expectedSnippet = String.format(
                    "Activity: %s\n" +
                            "    Status: %s\n" +
                            "    Budgeted Hours: %s\n" +
                            "    Used Hours: %s\n" +
                            "    Assigned Users: %s",
                    activity, status, budgetedHours, usedHours, assignedUsers);

            assertTrue(normalize(report).contains(normalize(expectedSnippet)),
                    "Expected snippet not found for activity: " + activity);
        }
    }

    @Then("the report should not include:")
    public void theReportShouldNotInclude(DataTable expectedTable) {
        List<List<String>> rows = expectedTable.asLists();
        for (List<String> row : rows) {
            String combined = String.join(" ", row).trim();
            assertFalse(report.contains(combined), "The report should not include: " + combined);
        }
    }

    @When("the user requests a status report from start year {int}, start week {int} to end year {int}, end week {int}")
    public void theUserRequestsAStatusReportFromStartYearStartWeekToEndYearEndWeek(int startYear, int startWeek, int endYear, int endWeek) {
        report = pmApp.getStatusReport(startYear, startWeek, endYear, endWeek);
    }

    @And("there are no activities in project with ID {int}")
    public void thereAreNoActivitiesInProjectWithID(int projectID) throws OperationNotAllowedException {
        Project project = pmApp.getProject(projectID);
        assertTrue(project.getActivities().isEmpty(), "The project should have no activities.");
    }

    @And("the user sets the status of {string} to {string} in project with ID {int}")
    public void the_user_sets_the_status_of_activity_in_project(String activityName, String status, int projectID) throws OperationNotAllowedException {
        Activity activity = pmApp.getActivityByName(activityName, projectID);
        pmApp.setStatusOfActivity(activityName, projectID, status);
        assertEquals(status, activity.getStatus(), "The status of the activity was not updated correctly.");
    }

    private String normalize(String input) {
        return input.lines()
                .map(String::stripTrailing)
                .map(String::stripLeading)
                .collect(Collectors.joining("\n"))
                .trim();
    }
}
