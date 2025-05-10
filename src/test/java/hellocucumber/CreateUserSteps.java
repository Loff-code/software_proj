package hellocucumber;

import app.Activity;
import app.OperationNotAllowedException;
import app.PM_App;
import app.Project;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CreateUserSteps {

    private ErrorMessageHolder errorMessageHolder;
    private PM_App app;

    public CreateUserSteps(PM_App app, ErrorMessageHolder errorMessageHolder) {
        this.errorMessageHolder = errorMessageHolder;
        this.app = app;
    }
    @When("the user creates a user with the ID {string}")
    public void the_user_creates_a_user_with_the_id(String string)  {
        try{app.createUser(new app.User(string));}
        catch (OperationNotAllowedException | IllegalArgumentException e){
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }
    @Then("the user is created with the ID {string}")
    public void the_user_is_created_with_the_id(String string)  {
     try{ app.getUserByID(string);}
        catch (OperationNotAllowedException e){
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }
}


