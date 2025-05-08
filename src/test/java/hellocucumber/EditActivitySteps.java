package hellocucumber;

import app.*;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class EditActivitySteps {

    private final PM_App app;
    private final ErrorMessageHolder errorMessageHolder;

    public EditActivitySteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
    }

    private Project getProjectSafe(int id) throws OperationNotAllowedException {
        for (Project p : app.getProjects()) {
            if (p.getProjectID() == id) return p;
        }
        throw new OperationNotAllowedException("Project not found");
    }

    @Given("that a project with ID {int} exists")
    public void that_a_project_with_ID_exists(int id) {
        for (Project p : app.getProjects()) {
            if (p.getProjectID() == id) return;
        }
        Project project = new Project("P" + id, "ClientX");
        project.assignProjectID(id);
        app.getProjects().add(project);
    }

    @Given("activity with name {string}, budgeted time {int} hours, start week {int}, end week {int} exists in project with ID {int}")
    public void activity_with_details_exists(String name, int budget, int start, int end, int projectID) {
        try {
            Project project = getProjectSafe(projectID);
            Activity activity = new Activity(name, budget, start, end);
            project.getActivities().add(activity);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Given("activity with name {string} exists in project with ID {int}")
    public void activity_with_name_exists(String name, int projectID) {
        try {
            Project project = getProjectSafe(projectID);
            Activity activity = new Activity(name, 10, 1, 2);
            project.getActivities().add(activity);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits activity {string} in project with ID {int} to name {string}, budgeted time {int} hours, start week {int}, end week {int}")
    public void edit_activity(String oldName, int projectID, String newName, int budget, int start, int end) {
        try {
            Project project = getProjectSafe(projectID);
            Activity activity = project.getActivityByName(oldName);

            if (activity == null)
                throw new IllegalArgumentException("Activity not found");

            if (!oldName.equals(newName) && project.getActivityByName(newName) != null)
                throw new IllegalArgumentException("Activity with the new name already exists");

            activity.setName(newName);
            activity.setBudgetTime(budget);
            activity.setStartWeek(start);
            activity.setEndWeek(end);

        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} exists in project with ID {int}")
    public void verify_activity_exists(String name, int projectID) throws OperationNotAllowedException {
        Activity activity = getProjectSafe(projectID).getActivityByName(name);
        assertNotNull(activity);
    }

    @Then("the activity {string} in project with ID {int} has budgeted time {int} hours, start week {int}, end week {int}")
    public void verify_activity_values(String name, int projectID, int budget, int start, int end) throws OperationNotAllowedException {
        Activity activity = getProjectSafe(projectID).getActivityByName(name);
        assertEquals(budget, activity.getBudgetTime());
        assertEquals(start, activity.getStartWeek());
        assertEquals(end, activity.getEndWeek());
    }

    @Then("an error message occurs with text {string}")
    public void error_message_occurs(String expectedMessage) {
        assertEquals(expectedMessage, errorMessageHolder.getErrorMessage());
    }
}
