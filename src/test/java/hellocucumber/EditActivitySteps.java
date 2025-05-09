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

    @When("the user edits activity {string} in project {string} to name {string}, budgeted time {int} hours, start week {int}, end week {int}")
    public void the_user_edits_activity_in_project_to_name_budgeted_time_hours_start_week_end_week(
            String oldName, String projectName, String newName, int budgetHours, int startWeek, int endWeek) {
        System.out.println("🔧 editActivityInProject called with oldName='" + oldName + "', projectName='" + projectName + "', newName='" + newName + "'");
        try {
            app.editActivityInProject(projectName, oldName, newName, budgetHours, startWeek, endWeek);
        } catch (OperationNotAllowedException e) {
            System.out.println("❌ " + e.getMessage());
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} exists in project {string} with budgeted time {int} hours, start week {int}, end week {int}")
    public void theActivityExistsInProjectWithBudgetedTimeHoursStartWeekEndWeek(
            String actName, String projectName, int budgetHours, int start, int end) throws OperationNotAllowedException {
        Project project = app.getProject(projectName);
        Activity activity = project.getActivityByName(actName);

        assertNotNull(activity, "Activity should exist");
        assertEquals(budgetHours, activity.getBudgetTime());
        assertEquals(start, activity.getStartWeek());
        assertEquals(end, activity.getEndWeek());
    }
}
