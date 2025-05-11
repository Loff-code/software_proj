package hellocucumber;

import app.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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


}
