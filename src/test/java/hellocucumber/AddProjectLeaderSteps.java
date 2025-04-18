package hellocucumber;

import app.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class AddProjectLeaderSteps {

    Project project;
    private ErrorMessageHolder errorMessageHolder;
    String assignedUserID;


    public AddProjectLeaderSteps() {
        this.errorMessageHolder = new ErrorMessageHolder();
    }

    // Create a new instance of PM_App with the given employees
    PM_App app = AppHolder.app;

    // scenario 1: User assigns a project leader successfully

    @Given("that a project with ID {int} exists")
    public void that_a_project_with_id_exists(int id) throws OperationNotAllowedException {
        project = new Project("Projectx", "DTU");
        project.assignProjectID(id);
        app.getProject().add(project);

    }

    @Given("the user {string} exists")
    public void the_user_exists(String name) {
        app.getUsers().add(new User(name));
    }

    @Given("the user {string} is not already a leader of the project")
    public void the_user_is_not_already_a_leader_of_the_project(String name) {
        assignedUserID = name;
        assertNull(project.getProjectLeaderID());
    }

    @When("the user assigns {string} as the project leader to the project {int}")
    public void user_assigns_as_the_project_leader_to_the_project(String name, int id) throws OperationNotAllowedException {
        app.assignProjectLeader(project.getProjectID(), assignedUserID);

    }

    @Then("the user {string} is set as the leader of project {int}")
    public void the_project_leader_of_the_project_is(String name, int id) {
        assertEquals(assignedUserID, project.getProjectLeaderID());
    }


    // scenario 2: User assigns a project leader who is already assigned

    @Given("the user {string} is already a project leader to project {int}")
    public void the_user_is_already_a_project_leader_to_project(String name, int id) throws OperationNotAllowedException {
        User user = new User(name);
        app.getUsers().add(user);

        project = new Project("Projectx", "DTU");
        project.assignProjectID(id);

        project.setProjectLeader(user);

        app.getProject().add(project);

        assignedUserID = name;

    }

    @When("the user {string} tries to assign {string} as the project leader to project {int} again")
    public void the_user_tries_to_assign_as_the_project_leader_to_project_again(String user, String name, int id) throws OperationNotAllowedException {
        try {
            app.assignProjectLeader(project.getProjectID(), assignedUserID);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("an error message happens")
    public void an_error_message_happens() {
        assertEquals("User is already a project leader", errorMessageHolder.getErrorMessage());
    }



    // scenario 3: User assigns a non existing user as project leader

    @Given("the user {string} does not exist")
    public void the_user_does_not_exist(String name) {
        assignedUserID = name;
    }


    @When("the user assigns {string} as project leader to project {int}")
    public void the_user_assigns_as_project_leader_to_project(String name, int id) throws OperationNotAllowedException {
        try {
            app.assignProjectLeader(project.getProjectID(), assignedUserID);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("an error message happens about the user not existing")
    public void an_error_message_happens_about_the_user_not_existing() {
        assertEquals("User does not exist", errorMessageHolder.getErrorMessage());
    }


    // scenario 4: User assigns a project leader to a non existing project
    @Given("the project with ID {int} does not exist")
    public void the_project_with_id_does_not_exist(int id) {
        project = null;
    }

    @When("the user assigns {string} as project leader to project with ID {int} that does not exist")
    public void the_user_assigns_as_project_leader_to_project_with_id_that_does_not_exist(String name, int id) throws OperationNotAllowedException {
        try {
            app.assignProjectLeader(id, assignedUserID);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("an error message happens about the project not existing")
    public void an_error_message_happens_about_the_project_not_existing() {
        assertEquals("Project does not exist", errorMessageHolder.getErrorMessage());
    }


    // scenario 5: User tries to assign themselves as project leader

    //@When("the user {string} tries to assign themselves as project leader to project {int}")
    //public void the_user_tries_to_assign_themselves_as_project_leader_to_project(String name, int id) throws OperationNotAllowedException {
      //  try {
        //    app.assignProjectLeader(project.getProjectID(), assignedUserID);
        //} catch (OperationNotAllowedException e) {
         //   errorMessageHolder.setErrorMessage(e.getMessage());
        //}
    //}

    //@Then("an error message happens about the user not being able to assign themselves as project leader")
    //public void an_error_message_happens_about_the_user_not_being_able_to_assign_themselves_as_project_leader() {
      //  assertEquals("User cannot assign themselves as project leader", errorMessageHolder.getErrorMessage());
    //}




}




