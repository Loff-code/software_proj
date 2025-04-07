package hellocucumber;

import io.cucumber.java.en.Given;
import app.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


public class LoginLogoutSteps {
   Employer admin = new EmployerHelper().getEmployer();
   Employee bob = new EmployeeHelper().getEmployee();

   private ErrorMessageHolder errorMessageHolder;

   public LoginLogoutSteps(){

   }
   PM_App app = new PM_App();

@Given("that an employee is not logged in")
public void that_the_employee_is_not_logged_in() {
   assertTrue(app.getUserID().equals(""));
}

   @Given("that an employee with the name {string}, email {string}, ID {string} and password {string} exists")
   public void that_an_employee_with_the_name_email_id_and_password_exists(String string, String string2, String string3, String string4) throws OperationNotAllowedException  {
      try {

         Employee newEmployee = new Employee(string, string2, string3, string4);
      app.login(admin.getID(), admin.getPassword());
      app.registerEmployee(newEmployee);
      assertTrue(app.getEmployees().contains(newEmployee));
      app.logout();
      } catch (OperationNotAllowedException e){
         assertEquals("Wrong password", e.getMessage());

      }
   }

   @When("the employee logs in with the ID {string} and password {string}")
   public void the_employee_logs_in_with_the_id_and_password(String id, String password) throws OperationNotAllowedException {
      try {
         app.login(id, password);
      } catch (OperationNotAllowedException e) {
         assertEquals("Wrong password", e.getMessage());
      }
   }
   @Then("the employee with the ID {string} is now logged in")
   public void the_employee_with_the_id_is_now_logged_in(String id) {
      assertTrue(app.getUserID().equals(id));
   }

   @Then("the employer with the ID {string} is now logged in")
   public void the_employer_with_the_id_is_now_logged_in(String id) {
      assertTrue(app.getUserID().equals(id));
   }

   @Given("that the employee with the ID {string} and password {string} is logged in")
   public void that_the_employee_with_the_id_and_password_is_logged_in(String id, String password) {
      try {
         app.login(id, password);
      } catch (OperationNotAllowedException e) {
         assertEquals("Wrong password", e.getMessage());
      }

      assertTrue(app.getUserID().equals(id));

   }

   @When("the employee logs out")
   public void the_employee_logs_out() {
      app.logout();

   }
   @Then("the employee is not logged in")
   public void the_employee_is_not_logged_in() {
      assertTrue(app.getUserID().equals(""));
   }













   @Given("that the employer is not logged in")
   public void that_the_employer_is_not_logged_in() {
      assertTrue(app.getUserID().equals(""));
   }

   @Given("that the employer with the name {string}, email {string}, ID {string} and password {string} exists")
   public void that_the_employer_with_the_name_email_id_and_password_exists(String name, String email, String id, String password) {
    String tmpID= "";
   for (Employer employer : app.getEmployers()){
            if (employer.getID().equals(id)){
              tmpID = id;
            }
        }
   assertTrue(name.equals(app.getEmployerByID(tmpID).getName()) && email.equals(app.getEmployerByID(tmpID).getEmail()) && id.equals(app.getEmployerByID(tmpID).getID()) && password.equals(app.getEmployerByID(tmpID).getPassword()));
   }


   @Given("the employer logs in with the ID {string} and password {string}")
   public void the_employer_logs_in_with_the_id_and_password(String id, String password)  throws OperationNotAllowedException{
      try{
         app.login(id,password);
      }
      catch (OperationNotAllowedException e) {
         // Assert that the exception message is what you expect
         assertEquals("Wrong password", e.getMessage());
      }
   }

   @Given("that the employer with the ID {string} and password {string} is logged in")
   public void that_the_employer_with_the_id_and_password_is_logged_in(String id, String password) {
      try {
         app.login(id, password);
      } catch (OperationNotAllowedException e) {
         assertEquals("Wrong password", e.getMessage());
      }

      assertTrue(app.getUserID().equals(id));

   }
   @When("the employer logs out")
   public void the_employer_logs_out() {
   app.logout();
   }
   @Then("the employer is not logged in")
   public void the_employer_is_not_logged_in() {
 app.getUserID().equals("");
   }
}
