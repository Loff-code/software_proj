package hellocucumber;

import app.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;


public class AddProjectLeaderSteps {

    Employee tom = new Employee("Tom", "tom@world.com", "0004", "TomTom");
    Employee jerry = new Employee("Jerry", "jerry@mail.com", "0003", "JerryJerry");
    Employer carl = new EmployerHelper().getEmployer();

    Project project;
    private ErrorMessageHolder errorMessageHolder;

    public AddProjectLeaderSteps() {

    }

    // Create a new instance of PM_App with the given employees
    PM_App app = new PM_App(carl, tom);

    @Given("an employee is logged in")
    public void an_employee_is_logged_in() throws OperationNotAllowedException {
        try {
            app.login(tom.getID(), tom.getPassword());
        } catch (OperationNotAllowedException e) {
            // Assert that the exception message is what you expect
            assertEquals("Wrong password", e.getMessage());
        }
    }

    @Given("that a project with ID {int} exists")
    public void that_a_project_with_id_exists(int id) throws OperationNotAllowedException {
        Client client = new ClientHelper().getClient();
        project = new Project("Projectx", client);
        project.assignProjectID(id);
        app.getProject().add(project);

    }

    @Given("that the employee {string} is not already a leader of the project")
    public void that_the_employee_is_not_already_a_leader_of_the_project(String name) {
        app.registerEmployee(jerry);
        assertNull(project.getProjectLeaderID());
    }

    @When("Tom assigns {string} as the project leader to the project {int}")
    public void tom_assigns_as_the_project_leader_to_the_project(String name, int id) throws OperationNotAllowedException {
        app.assignProjectLeader(project.getName(), jerry.getID());

    }

    @Then("the employee {string} is set as the leader of project {int}")
    public void the_project_leader_of_the_project_is(String name, int id) {
        assertEquals(jerry.getID(), project.getProjectLeaderID());
    }

}




