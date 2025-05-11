package hellocucumber;

import app.Activity;
import app.OperationNotAllowedException;
import app.PM_App;
import app.Project;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AddActivitySteps {

    private ErrorMessageHolder errorMessageHolder;
    private PM_App app;

    public AddActivitySteps(PM_App app, ErrorMessageHolder errorMessageHolder){
        this.errorMessageHolder = errorMessageHolder;
        this.app = app;
    }



    @Then("the activity {string} is added to project with ID {int}")
    public void theActivityIsAddedToProjectWithID(String activityName, int projectID) throws OperationNotAllowedException {
        assertEquals(app.getActivityByName(activityName, projectID).getName(), activityName);
    }

    @When("the user adds an activity with name {string}, budgeted time {int} hours, start week {int}, end week {int} to project with ID {int}")
    public void theUserAddsAnActivityWithNameBudgetedTimeHoursStartWeekEndWeekToProjectWithID(String activityName, int budgetHours, int startWeek, int endWeek, int proejctID) {
        try {
            app.addActivityToProject(proejctID, new Activity(activityName, budgetHours, startWeek, endWeek));
        } catch (OperationNotAllowedException | IllegalArgumentException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }
}




//package hellocucumber;
//
//import app.*;
//        import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class CreateProjectSteps {
//
//    Employer admin = new EmployerHelper().getEmployer();
//    Employee bob = new EmployeeHelper().getEmployee();
//    Client clientHelper;
//
//
//    private ErrorMessageHolder errorMessageHolder;
//
//    public CreateProjectSteps(){
//    }
//    PM_App app = new PM_App();
//    @Given("an employer is logged in")
//    public void an_employer_is_logged_in() throws OperationNotAllowedException{
//        try{
//            app.login(admin.getID(),admin.getPassword());
//        }
//        catch (OperationNotAllowedException e) {
//            // Assert that the exception message is what you expect
//            assertEquals("Wrong password", e.getMessage());
//        }
//    }
//
//    @Given("client clientHelper exists")
//    public void client_client_helper_exists() {
//        clientHelper =  new ClientHelper().getClient();
//    }
//
//    @When("the employer creates a project with the name {string} and the client.")
//    public void the_employer_creates_a_project_with_the_name_and_the_client(String projectName) {
//        app.createProject(projectName, clientHelper);
//    }
//    @Then("the project is created with the name {string}")
//    public void the_project_is_created_with_the_name(String projectName) {
//        app.getProjectByName(projectName);
//    }
//
//    @When("the employer creates a project with the name {string}, client clientHelper")
//    public void the_employer_creates_a_project_with_the_name_client_client_helper(String projectName) {
//        app.createProject(projectName, clientHelper);
//    }
//
//    @Then("the project {string} is not created")
//    public void the_project_is_not_created(String projectName) {
//        assertTrue(app.getProjectByName(projectName) == null);
//    }
//
//    @Then("an error message happens")
//    public void an_error_message_happens() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//
//    @When("the employer creates a project with the name {string}, client null")
//    public void the_employer_creates_a_project_with_the_name_client_null(String projectName) {
//        app.createProject(projectName, null);
//    }
//
//    @Given("project with name {string} exists")
//    public void project_with_name_exists(String projectName) {
//        app.createProject(projectName, clientHelper);
//    }
//
//
//    @Then("there are no duplicates of the project {string}")
//    public void there_are_no_duplicates_of_the_project(String name) {
//        int cnt = 0;
//        for (Project project : app.getProject()){
//            cnt += project.getName().equals((name))?1:0;
//        }
//        assertTrue(cnt==1);
//    }
//}

