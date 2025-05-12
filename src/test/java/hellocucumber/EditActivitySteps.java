package hellocucumber;

import app.*;
import io.cucumber.java.en.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditActivitySteps {

    private final PM_App app;
    private final ErrorMessageHolder errorMessageHolder;

    public EditActivitySteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;
    }

    private List<String> availbles;


    @Given("the user adds an activity named {string} with budget {int}, start year {int}, start week {int}, end year {int}, end week {int} to project {int}")
    public void the_user_adds_an_activity_named_with_years(
            String name, int budget, int startYear, int startWeek, int endYear, int endWeek, int projectID) {
        try {
            if (app.getLoggedInUserID() == null) {
                throw new IllegalStateException("No user is logged in");
            }
            Activity activity = new Activity(name, budget, startYear, startWeek, endYear, endWeek);
            app.addActivityToProject(projectID, activity);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }


    @When("the user edits the name of activity {string} in project {int} to {string}")
    public void the_user_edits_the_name_of_activity(String oldName, int projectID, String newName) {
        try {
            app.getActivityByName(oldName, projectID).setName(newName);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the budgeted time of activity {string} in project {int} to {int}")
    public void the_user_edits_budgeted_time(String name, int projectID, int newBudget) {
        try {
            app.getActivityByName(name, projectID).setBudgetTime(newBudget);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the start week of activity {string} in project {int} to {int}")
    public void the_user_edits_start_week(String name, int projectID, int newStartWeek) {
        try {
            app.getActivityByName(name, projectID).setStartWeek(newStartWeek);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the end week of activity {string} in project {int} to {int}")
    public void the_user_edits_end_week(String name, int projectID, int newEndWeek) {
        try {
            app.getActivityByName(name, projectID).setEndWeek(newEndWeek);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity {string} exists in project with ID {int}")
    public void the_activity_exists_in_project(String name, int projectID) throws OperationNotAllowedException {
        assertEquals(name, app.getActivityByName(name, projectID).getName());
    }

    @Then("the activity {string} in project {int} has budgeted time {int}")
    public void the_activity_has_budgeted_time(String name, int projectID, int expectedBudget) throws OperationNotAllowedException {
        assertEquals(expectedBudget, app.getActivityByName(name, projectID).getBudgetTime());
    }

    @Then("the activity {string} in project {int} has start week {int}")
    public void the_activity_has_start_week(String name, int projectID, int expectedStartWeek) throws OperationNotAllowedException {
        assertEquals(expectedStartWeek, app.getActivityByName(name, projectID).getStartWeek());
    }

    @Then("the activity {string} in project {int} has end week {int}")
    public void the_activity_has_end_week(String name, int projectID, int expectedEndWeek) throws OperationNotAllowedException {
        assertEquals(expectedEndWeek, app.getActivityByName(name, projectID).getEndWeek());
    }

    @Then("an error message {string} is shown")
    public void an_error_message_is_shown(String expectedMessage) {
        assertEquals(expectedMessage, errorMessageHolder.getErrorMessage());
    }

    @And("the user {string} is not already assigned to the activity {string} in project {int}")
    public void theUserIsNotAlreadyAssignedToTheActivityInProject(String userID, String activityName, int projectID) throws OperationNotAllowedException {
        app.logout();
        app.login(userID);
        assertFalse(app.getActivityByName(activityName, projectID).getAssignedUsers().contains(userID));


    }

    @And("the user assigns {string} to the activity {string} in project {int}")
    public void theUserAssignsToTheActivityInProject(String userID, String activityName, int projectID) throws OperationNotAllowedException {
        try {
            app.assignUserToActivity(userID,activityName, projectID);
        }catch (IllegalArgumentException | OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the start year of activity {string} in project {int} to {int}")
    public void theUserEditsTheStartYearOfActivityInProjectTo(String activityName, int projectID, int newStartYear) {
        try {
            app.getActivityByName(activityName, projectID).setStartYear(newStartYear);
        } catch (IllegalArgumentException | OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user edits the end year of activity {string} in project {int} to {int}")
    public void theUserEditsTheEndYearOfActivityInProjectTo(String activityName, int projectID, int newEndYear) {
        try {
            app.getActivityByName(activityName, projectID).setEndYear(newEndYear);
        } catch (IllegalArgumentException | OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user requests the list of available users from year {int} week {int} to year {int} week {int}")
    public void theUserRequestsTheListOfAvailableUsersFromYearWeekToYearWeek(int arg0, int arg1, int arg2, int arg3) {
        try {
            availbles = app.getAvailableUserIDs(arg0, arg1, arg2, arg3);
        }catch (IllegalArgumentException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }


    }

    @Then("the list of available users includes {string}")
    public void theListOfAvailableUsersIncludes(String arg0) {
        assertTrue(availbles.contains(arg0));
    }

    @And("the list of available users does not include {string}")
    public void theListOfAvailableUsersDoesNotInclude(String arg0) {
        assertFalse(availbles.contains(arg0));
    }

    @When("the user requests the list of available users to the activity {string} in project {int}")
    public void theUserRequestsTheListOfAvailableUsersToTheActivityInProject(String arg0, int arg1) throws OperationNotAllowedException {
        try {
            availbles = app.getAvailableUserIDsForActivity(arg1, arg0);
        }catch (IllegalArgumentException| OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("the user requests the list of vacant users from year {int} week {int} to year {int} week {int}")
    public void theUserRequestsTheListOfVacantUsersFromYearWeekToYearWeek(int arg0, int arg1, int arg2, int arg3) throws OperationNotAllowedException {
        try {
            availbles = app.getVacantUserIDs(arg0, arg1, arg2, arg3);
        } catch (IllegalArgumentException | OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());

        }
    }

    @Then("the list of vacant users includes {string}")
    public void theListOfVacantUsersIncludes(String arg0) {
        assertTrue(availbles.contains(arg0));
    }


    @When("the user requests the list all activities assigned to the user {string}")
    public void theUserRequestsTheListAllActivitiesAssignedToTheUser(String arg0) {
        try {
            availbles = app.getAssignedActivitiesByUserID(arg0);
        } catch (IllegalArgumentException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }

    }

    @Then("the list of activities assigned to the user {string} includes {string}")
    public void theListOfActivitiesAssignedToTheUserIncludes(String arg0, String arg1) {
        boolean thing = false;
        for (String activity : availbles) {
                if (activity.contains(arg1)) {
                    thing = true;
                }

        }
        assertTrue(thing);
    }

    Activity leaveActivity;
    String leaveActivityName;

    @When("the user {string} requests leave named {string} from {string} to {string} with {double} hours")
    public void the_user_requests_leave(String userID, String activityName, String dateStart, String dateEnd, double hours) throws Exception {
        app.createLeaveRequest(userID, activityName, 1, hours, dateStart, dateEnd);
        leaveActivity = app.getProject(1).getActivities().get(0);
        leaveActivityName = leaveActivity.getName(); // Store for reuse
    }

    @Then("the project {int} contains a leave activity for {string} named {string}")
    public void the_project_contains_leave_activity(int projectID, String userID, String activityName) throws Exception {
        List<Activity> activities = app.getProject(projectID).getActivities();

        boolean containsUser = false;
        boolean containsActivity = false;
        for (Activity activity : activities) {
            if (activity.getName().contains(userID)) {
                containsUser = true;
            }
            if (activity.getName().contains(activityName)) {
                containsActivity = true;
            }
        }

        assertTrue(containsActivity);
        assertTrue(containsUser);
    }

    @Then("the activity name contains {string}")
    public void the_activity_name_contains(String expected) {
        assertTrue(leaveActivityName.contains(expected));
    }

    @Then("the leave activity for {string} on {string} registers {double} hours")
    public void the_leave_activity_registers_hours(String userID, String date, double expectedHours) {
        String key = "[" + userID + "]|" + leaveActivity.getName() + "|" + date;
        double actual = leaveActivity.getTimeMap().getOrDefault(key, 0.0);
        assertEquals(expectedHours, actual);
    }

    String report;

    @When("the user generates a report for project {int}")
    public void the_user_generates_a_report_for_project(int projectID) throws Exception {
        try {
            report = app.generateReport(String.valueOf(projectID));
        }catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the report contains {string}")
    public void the_report_contains(String expectedContent) {
        assertTrue(report.contains(expectedContent), "Expected report to contain: " + expectedContent);
    }

    @When("the user requests the leave status report from year {int} week {int} to year {int} week {int}")
    public void theUserRequestsTheLeaveStatusReportFromYearWeekToYearWeek(int arg0, int arg1, int arg2, int arg3) {
        try {
            report = app.getLeaveStatusReport(arg0, arg1, arg2, arg3);
        }catch (IllegalArgumentException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the leave status report is generated successfully")
    public void theLeaveStatusReportIsGeneratedSuccessfully() {
        assertNotNull(report, "Leave status report should not be null");
        assertFalse(report.isEmpty(), "Leave status report should not be empty");
    }

    @Then("the leave status report from year {int} week {int} to year {int} week {int} is generated successfully")
    public void theLeaveStatusReportFromYearWeekToYearWeekIsGeneratedSuccessfully(int arg0, int arg1, int arg2, int arg3) {
        try {
            report = app.getLeaveStatusReport(arg0, arg1, arg2, arg3);
        }catch (IllegalArgumentException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }
}
