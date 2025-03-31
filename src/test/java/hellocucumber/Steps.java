package hellocucumber;

import io.cucumber.java.en.Given;
import app.*;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Steps {
   Employer employer1 = new EmployerHelper().getEmployer();
   Employee bob = new EmployeeHelper().getEmployee();

   public Steps(){

   }
   PM_App app = new PM_App(employer1);
   @Given("an employer is logged in")
   public void an_employer_is_logged_in() {
      // Write code here that turns the phrase above into concrete actions
      app.Login(employer1.getID(), employer1.getPassword());
      assertTrue(employer1.getID().equals(app.getUserID()));
   }

   @Given("that employer with the name {string} and password {string} exists")
   public void that_employer_with_the_name_and_password_exists(String id, String password) {
      String tmpID;
      String tmpPassword;
      for (Employer employer : app.getEmployers()){
         if (employer.getID().equals(id)){
            tmpID = id;
         }
         if (employer.getPassword().equals(password))
            tmpPassword = password;
      }
   }

   @Given("that the employer is not logged in")
   public void that_the_employer_is_not_logged_in() {
      // Write code here that turns the phrase above into concrete actions
      throw new io.cucumber.java.PendingException();
   }

   @Given("the employer username is {string}")
   public void the_employer_username_is(String string) {
      // Write code here that turns the phrase above into concrete actions
      throw new io.cucumber.java.PendingException();
   }

   @Given("the employer password is {string}")
   public void the_employer_password_is(String string) {
      // Write code here that turns the phrase above into concrete actions
      throw new io.cucumber.java.PendingException();
   }

   @Then("the employer login succeeds")
   public void the_employer_login_succeeds() {
      // Write code here that turns the phrase above into concrete actions
      throw new io.cucumber.java.PendingException();
   }

   @Then("the employer is logged in")
   public void the_employer_is_logged_in() {
      // Write code here that turns the phrase above into concrete actions
      throw new io.cucumber.java.PendingException();
   }
}
