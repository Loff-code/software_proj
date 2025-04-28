package hellocucumber;


import app.PM_App;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindVacEmployeeSteps {

    private Project currentProject;
    private List<Employee> foundEmployees;
    private ErrorMessageHolder errorMessageHolder;
    private PM_App app;

    public FindVacEmployeeSteps(PM_App app, ErrorMessageHolder errorMessageHolder){
        this.errorMessageHolder = errorMessageHolder;
        this.app = app;
    }

    public FindVacEmployeeSteps(){
        Employer employer = new EmployerHelper().getEmployer();
        Employee employee= new EmployeeHelper().getEmployee();
        app = new PM_App(employer,employee);
        this.foundEmployees = new ArrayList<>();
    }
    @Given("the project leader is logged in to the system")
    public
}

