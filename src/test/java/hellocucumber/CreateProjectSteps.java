package hellocucumber;

import io.cucumber.java.en.Given;
import app.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


public class CreateProjectSteps {

    private ErrorMessageHolder errorMessageHolder;
    private PM_App app;
    private Project project;
    public CreateProjectSteps(PM_App app, ErrorMessageHolder errorMessageHolder){
        this.errorMessageHolder = errorMessageHolder;
        this.app = app;
    }

    @When("the user creates a project with the name {string}, client {string}")
    public void the_user_creates_a_project_with_the_name_client(String projectName, String clientName) throws OperationNotAllowedException {
        try{app.createProject(new Project(projectName, clientName));}
        catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }
    @Then("the project is created with the name {string}")
    public void the_project_is_created_with_the_name(String projectName) throws OperationNotAllowedException {
       boolean exists = false;
       for (Project project : app.getProjects()){
           if (project.getName().equals(projectName)){
               exists = true;
           }
       }
       assertTrue(exists);
    }

    @Then("the project {string} is not created")
    public void the_project_is_not_created(String projectName) throws OperationNotAllowedException {
        boolean exists = false;
        for (Project project : app.getProjects()) {
            if (project.getName().equals(projectName)) {
                exists = true;
            }
        }
        assertFalse(exists);
    }

    @Given("project with name {string} exists")
    public void project_with_name_exists(String projectName) throws OperationNotAllowedException {
        try{app.createProject(new Project(projectName, "client"));}
        catch (OperationNotAllowedException e) {
            assertEquals("Project name is taken", e.getMessage());
        }
    }


    @Then("the projects have different IDs")
    public void theProjectsHaveDifferentIDs() {
        int cnt = 0;
        for (Project project1 : app.getProjects()){
           for (Project project2 : app.getProjects()){
               if (project1.getName().equals(project2.getName()) && project1.getProjectID() == project2.getProjectID()){
                   cnt++;
               }
           }
            assertTrue(cnt == 1);// counts itself
            cnt = 0;
        }
    }
}
