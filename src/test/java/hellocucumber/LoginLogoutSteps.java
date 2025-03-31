package hellocucumber;

import io.cucumber.java.en.Given;
import app.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class Steps {
   Employer carl = new EmployerHelper().getEmployer();
   Employee bob = new EmployeeHelper().getEmployee();
   
   private ErrorMessageHolder errorMessageHolder;

   public Steps(){

   }
   PM_App app = new PM_App(carl,bob);
//   @Given("an employer is logged in")
//   public void an_employer_is_logged_in() {
//      // Write code here that turns the phrase above into concrete actions
//      app.Login(employer1.getID(), employer1.getPassword());
//      assertTrue(employer1.getID().equals(app.getUserID()));
//   }
@Given("that an employee is not logged in")
public void that_the_employee_is_not_logged_in() {
   assertTrue(app.getUserID().equals(""));
}

   @Given("that an employee with the ID {string} and password {string} exist")
   public void that_an_employee_with_the_id_and_password_exist(String id, String password) {
      String tmpID;
      String tmpPassword;
   for (Employee employee : app.getEmployees()){
         if (employee.getID().equals(id)){
            tmpID = id;
         }
         if (employee.getPassword().equals(password)) {
            tmpPassword = password;
         }
      }
   }

   @When("the employee logs in with the ID {string} and password {string}")
   public void the_employee_logs_in_with_the_id_and_password(String id, String password) {
      app.login(id,password);
   }
   @Then("the employee with the ID {string} is now logged in")
   public void the_employee_with_the_id_is_now_logged_in(String id) {
      assertTrue(app.getUserID().equals(id));
   }


}
