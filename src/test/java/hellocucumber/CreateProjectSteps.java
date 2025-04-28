package hellocucumber;

import io.cucumber.java.en.Given;
import app.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


public class CreateProjectSteps {

    private ErrorMessageHolder errorMessageHolder;
    private PM_App app;

    public CreateProjectSteps(PM_App app, ErrorMessageHolder errorMessageHolder){
        this.errorMessageHolder = errorMessageHolder;
        this.app = app;
    }

    @When("the user creates a project with the name {string}, client {string}")
    public void the_user_creates_a_project_with_the_name_client(String projectName, String clientName) throws OperationNotAllowedException {
        try{app.createProject(projectName, clientName);}
        catch (OperationNotAllowedException e) {
            assertEquals("Project name is taken", e.getMessage());
        }
    }
    @Then("the project is created with the name {string}")
    public void the_project_is_created_with_the_name(String projectName) {
        app.getProjectByName(projectName);
    }

    @Then("the project {string} is not created")
    public void the_project_is_not_created(String projectName) {
        System.out.println("name" + app.getProjectByName(projectName));
        assertTrue(app.getProjectByName(projectName) == null);
    }

    @Given("project with name {string} exists")
    public void project_with_name_exists(String projectName) throws OperationNotAllowedException {
        try{app.createProject(projectName, "client");}
        catch (OperationNotAllowedException e) {
            assertEquals("Project name is taken", e.getMessage());
        }
    }

    @Then("there are no duplicates of the project {string}")
    public void there_are_no_duplicates_of_the_project(String name) {
        int cnt = 0;
      for (Project project : app.getProject()){
            cnt += project.getName().equals((name))?1:0;
        }
        assertTrue(cnt==1);

    }




}
