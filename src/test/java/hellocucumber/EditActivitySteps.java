package hellocucumber;

import app.*;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class EditActivitySteps {

    private PM_App app;
    private ErrorMessageHolder errorMessageHolder;

    public EditActivitySteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
    }


// jeg har sat budgethours som int i stedet for double lige pt. Det skal ændres til double men så fucker det op i andre filer. og magter ikke at kigge på det lige nu
    @When("the user edits activity {string} in project {string} to name {string}, budgeted time {int} hours, start week {int}, end week {int}")
    public void the_user_edits_activity_in_project_to_name_budgeted_time_hours_start_week_end_week(String actNameOld, String projectName1, String actNameNew, int budgetHours, int start, int end) {
        try {
            app.editActivityInProject(actNameOld, projectName1, actNameNew, budgetHours, start, end);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }

    }

    // der er fejl i den her. Det er en lille fejl. det er noget med at man skal sikre at den nye er lavet.
    @Then("the activity {string} exists in project {string} with budgeted time {int} hours, start week {int}, end week {int}")
    public void theActivityExistsInProjectWithBudgetedTimeHoursStartWeekEndWeek(String actName, String projectName1, int budgetHours, int start, int end) throws OperationNotAllowedException {
        Activity activity = app.getProject(projectName1).getActivityByName(actName);

        assertNotNull(activity, "Activity should exist");

        assertEquals(budgetHours, activity.getBudgetTime());
        assertEquals(start, activity.getStartWeek());
        assertEquals(end, activity.getEndWeek());
    }



}