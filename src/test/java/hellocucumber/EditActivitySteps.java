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
            assertNotNull(project, "Project '" + projectName + "' should exist");

            System.out.println("🧪 Aktiviteter i projekt '" + projectName + "':");
            for (Activity a : project.getActivities()) {
                System.out.println("• " + a.getName() + " | budget: " + a.getBudgetTime() + " | start: " + a.getStartWeek() + " | end: " + a.getEndWeek());
            }

            Activity activity = project.getActivityByName(actName);
            assertNotNull(activity, "Activity '" + actName + "' should exist in project '" + projectName + "'");

            System.out.println("✅ Fundet aktivitet: " + activity.getName());
            System.out.println("   - budgetedTime: " + activity.getBudgetTime());
            System.out.println("   - startWeek: " + activity.getStartWeek());
            System.out.println("   - endWeek: " + activity.getEndWeek());

            assertEquals(budgetHours, activity.getBudgetTime(), "Budgeted time mismatch");
            assertEquals(start, activity.getStartWeek(), "Start week mismatch");
            assertEquals(end, activity.getEndWeek(), "End week mismatch");

        } catch (OperationNotAllowedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
