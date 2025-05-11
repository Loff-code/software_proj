package hellocucumber;

import app.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class AddProjectLeaderSteps {

    private Project project;
    private ErrorMessageHolder errorMessageHolder;
    String assignedUserID;
    private PM_App app;


    public AddProjectLeaderSteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.app = app;
        this.errorMessageHolder = errorMessageHolder;

    }


    // scenario 1: User assigns a project leader successfully

    @Given("that a project with ID {int} exists")
    public void that_a_project_with_ID_exists(int projectID) {
        try {
            this.project = new Project("default", "client");
            app.createProject(project);
            project.setProjectID(projectID);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    // jeg har bare brugt viktors kode, men måske ændre jeg det til at
    @Given("the user {string} exists")
    public void the_user_exists(String name) throws OperationNotAllowedException {
        assignedUserID = name;
        try {
            app.getUserByID(name);
        } catch ( IllegalArgumentException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }catch (OperationNotAllowedException  e){
            app.createUser(new User(name));
        }
    }

    @Given("the user {string} is not already a leader of the project")
    public void the_user_is_not_already_a_leader_of_the_project(String name) {
        assignedUserID = name;
        if (project.getProjectLeaderID() != null) {
            assertFalse(project.getProjectLeaderID().equals(assignedUserID));
        }
        assertTrue(project.getProjectLeaderID() == null);
    }

    @When("the user assigns {string} as the project leader to the project {int}")
    public void the_user_assigns_as_the_project_leader_to_the_project(String name, int id) throws OperationNotAllowedException, IllegalArgumentException {
        assignedUserID = name;
        try {
            app.assignProjectLeader(project.getProjectID(), assignedUserID);
        } catch (OperationNotAllowedException | IllegalArgumentException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }

    }

    @Then("the user {string} is set as the leader of project {int}")
    public void the_project_leader_of_the_project_is(String name, int id) {
        assignedUserID = name;
        assertEquals(name, project.getProjectLeaderID());
    }


    // scenario 2: User assigns a project leader who is already assigned

    @Given("the user {string} is already a project leader to project {int}")
    public void the_user_is_already_a_project_leader_to_project(String name, int id) throws OperationNotAllowedException {
        User user = new User(name);
        app.getUsers().add(user);

        project = new Project("Projectx", "DTU");
        project.setProjectID(id);

        app.createProject(project);
        app.assignProjectLeader(project.getProjectID(), user.getID());

        assignedUserID = name;

    }

    @When("the user {string} tries to assign {string} as the project leader to project {int} again")
    public void the_user_tries_to_assign_as_the_project_leader_to_project_again(String user, String name, int id) throws OperationNotAllowedException {
        app.logout();
        app.login(user);
        try {
            app.assignProjectLeader(id, name);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
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
    @When("the user {string} tries to assign themselves as project leader to project {int}")
    public void the_user_tries_to_assign_themselves_as_project_leader_to_project(String user1, Integer projectID) throws OperationNotAllowedException {
        assertTrue(app.getLoggedInUserID().equals(user1));
        app.assignProjectLeader(projectID, user1);
    }

    @Then("no error message is shown")
    public void noErrorMessageIsShown() {
        System.out.println(errorMessageHolder.getErrorMessage());
        assertTrue(errorMessageHolder.getErrorMessage() == "");
    }

    @Then("the user {string} is assigned to the activity {string} in the project with ID {int}")
    public void theUserIsAssignedToTheActivityInTheProjectWithID(String userID, String activityName, int projectID) throws OperationNotAllowedException {
       assertTrue(app.getActivityByName(activityName, projectID).getAssignedUsers().contains(userID));
       //assertTrue(app.getAssignedActivitiesByUserID(userID).contains(app.getActivityByName(activityName, projectID)));
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




