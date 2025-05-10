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
            String activityName, String projectName, String newName, int budgetHours, int startWeek, int endWeek) {
        try {
            // 🔧 Korrekt rækkefølge: projectName, oldName, newName...
            System.out.printf("🔧 editActivityInProject called with projectName='%s', oldName='%s', newName='%s'%n",
                    projectName, activityName, newName);
            app.editActivityInProject(projectName, activityName, newName, budgetHours, startWeek, endWeek);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} exists in project {string} with budgeted time {int} hours, start week {int}, end week {int}")
    public void theActivityExistsInProjectWithBudgetedTimeHoursStartWeekEndWeek(
            String actName, String projectName, int budgetHours, int start, int end) {
        try {
            Project project = app.getProject(projectName);
            assertNotNull(project, "Project should exist");

            Activity activity = project.getActivityByName(actName);
            System.out.printf("🔎 Checking activity '%s' in project '%s'%n", actName, projectName);
            assertNotNull(activity, "Activity should exist");

            assertEquals(budgetHours, activity.getBudgetTime(), "Budgeted time mismatch");
            assertEquals(start, activity.getStartWeek(), "Start week mismatch");
            assertEquals(end, activity.getEndWeek(), "End week mismatch");
        } catch (OperationNotAllowedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}
