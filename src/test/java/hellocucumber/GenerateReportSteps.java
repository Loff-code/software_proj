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

import static org.junit.jupiter.api.Assertions.*;import io.cucumber.datatable.DataTable;

public class GenerateReportSteps {

    private final PM_App pmApp;

    private final ErrorMessageHolder errorMessageHolder;
    private Project project; // Vi gemmer projektet her

    // Constructor til at injectere appen og error holder
    public GenerateReportSteps(PM_App pmApp, ErrorMessageHolder errorMessageHolder) {
        this.pmApp = pmApp;
        this.errorMessageHolder = errorMessageHolder;
    }



    // Step 1: Opret et projekt med ID 25000 og navn "P1"
    @Given("that a project with ID {int} and project name {string} exists")
    public void projectWithIDAndNameExists(int projectID, String projectName) throws OperationNotAllowedException {
        // Vi opretter et projekt og tilføjer det til appen
        Project newProject = new Project(projectName, "Client");
        pmApp.createProject(newProject); // Opretter projektet i systemet
        newProject.setProjectID(projectID); // Sætter projektets ID
        project = pmApp.getProject(projectID); // Henter projektet fra systemet
        assertNotNull(project); // Tjekker om projektet er blevet oprettet korrekt
    }

    @And("the project {string} has a total budget of {int} hours")
    public void theProjectHasATotalBudgetOfHours(String projectName, int budgetHours) throws OperationNotAllowedException {
        // Get the project by name
        Project project = pmApp.getProject(projectName);

        // Set the total budget for the project
        project.setTotalBudget(budgetHours);
    }


    // To see when the user has registered time for an activity
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
            String expectedKey = row.get(0).trim();
            String expectedValue = row.get(1).trim();
            String expectedLine = expectedKey + " = " + expectedValue;

            assertTrue(actualEntries.contains(expectedLine),
                    "Expected to find: " + expectedLine + " but it was not found.");
        }

    }
    private String report; // Declare the report variable


    @When("the user requests a status report for project {string} between weeks {int} and {int}")
    public void theUserRequestsStatusReportForProjectBetweenWeeks(String projectName, int startWeek, int endWeek) {
        // Get the status report for the given week range
        report = pmApp.getStatusReport(startWeek, endWeek);
    }

    @Then("the activity {string} should show {double} used hours")
    public void theActivityShouldShowUsedHours(String activityName, double expectedUsedHours) {
        // Check if the report contains the activity name
        assertTrue(report.contains(activityName), "Activity " + activityName + " not found in the report.");

        // Check if the report contains the expected used hours
        assertTrue(report.contains(String.valueOf(expectedUsedHours)),
                "The expected used hours (" + expectedUsedHours + ") for activity " + activityName + " are not found in the report.");
    }




    @And("the user sets the status of {string} to {string} in project with ID {int}")
    public void the_user_sets_the_status_of_activity_in_project(String activityName, String status, int projectID) throws OperationNotAllowedException {
        // Retrieve the activity based on the project ID and activity name
        Activity activity = pmApp.getActivityByName(activityName, projectID);

        // Set the status of the activity
        pmApp.setStatusOfActivity(activityName, projectID, status);

        // Verify that the status has been updated correctly
        assertEquals(status, activity.getStatus(), "The status of the activity was not updated correctly.");
    }


    @When("the user requests a status report for project {string}")
    public void theUserRequestsAStatusReportForProject(String projectName) {
        report = pmApp.getStatusReport(1, 52);
    }


    @Then("the report should be empty")
    public void theReportShouldBeEmpty() {
        assertEquals(pmApp.getStatusReport(1, 52), "Report: Project Status Report (Weeks 1 - 52)\n" +
                "---------------------------------------------------------------");
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
                    activity, status, budgetedHours, usedHours, assignedUsers
            );

            assertTrue(normalize(report).contains(normalize(expectedSnippet)),
                    "Expected snippet not found for activity: " + activity);
        }
    }
    private String normalize(String input) {
        return input.lines()
                .map(String::stripTrailing)
                .map(String::stripLeading)
                .collect(Collectors.joining("\n"))
                .trim();
    }


    @When("the user requests a status report from week {int} to week {int}")
    public void theUserRequestsAStatusReportFromWeekToWeek(int startWeek, int endWeek) {
        report = pmApp.getStatusReport(startWeek, endWeek);
    }


    @And("there are no activities in project with ID {int}")
    public void thereAreNoActivitiesInProjectWithID(int projectID) throws OperationNotAllowedException {
        // Check if the project has no activities
        Project project = pmApp.getProject(projectID);
        assertTrue(project.getActivities().isEmpty(), "The project should have no activities.");
    }
    @Then("the report should not include:")
    public void theReportShouldNotInclude(DataTable expectedTable) {
        List<List<String>> rows = expectedTable.asLists();

        for (List<String> row : rows) {
            String combined = String.join(" ", row).trim(); // Combine the row entries
            assertFalse(report.contains(combined), "The report should not include: " + combined);
        }
    }


}
