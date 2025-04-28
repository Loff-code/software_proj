package hellocucumber;

import io.cucumber.java.en.Given;
import app.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


public class LoginLogoutSteps {

   private ErrorMessageHolder errorMessageHolder;
   private PM_App app;

   public LoginLogoutSteps(PM_App app, ErrorMessageHolder errorMessageHolder){
      this.errorMessageHolder = errorMessageHolder;
      this.app = app;
   }




   @Given("the user with the ID {string} exists")
   public void the_user_with_the_id_exists(String id) {
      String tmpID = "";
      for (User user: app.getUsers()){
         if (user.getID().equals(id)){
            tmpID = id;
         }
      }
      assertTrue(tmpID.equals(id));
   }

   @When("the user logs in with the ID {string}")
   public void the_user_logs_in_with_the_id(String id) throws OperationNotAllowedException{
      try{app.login(id);}
      catch (OperationNotAllowedException e) {
           assertEquals("User does not exist", e.getMessage());
       }
   }

   @Then("the user with the ID {string} is now logged in")
   public void the_user_with_the_id_is_now_logged_in(String id) {
         assertTrue(app.getUserID().equals(id));
   }

   @Given("the user with the ID {string} does not exist")
   public void the_user_with_the_id_does_not_exist(String id) {
      String tmpID = "";
      for (User user: app.getUsers()){
         if (user.getID().equals(id)){
            tmpID = id;
         }
      }
      assertFalse(tmpID.equals(id));
   }
   @Then("the user is not logged in")
   public void the_user_is_not_logged_in() {
    assertTrue(app.getUserID().equals(""));
   }

   @When("the user logs out")
   public void the_user_logs_out() {
   app.logout();
   }

   @Given("the user is logged in")
   public void the_user_is_logged_in() throws  OperationNotAllowedException{
      try{app.login("bob");}
      catch (OperationNotAllowedException e) {
         assertEquals("User does not exist", e.getMessage());
      }
   }
}
