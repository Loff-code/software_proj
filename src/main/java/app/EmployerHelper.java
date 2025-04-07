package app;

public class EmployerHelper {
    private Employer employer;
    public Employer getEmployer(){
        return exampleEmployer();
    }
    public Employer exampleEmployer(){
        this.employer = new Employer("Admin", "admin@world.com", "0001", "AdminAdmin");
        return employer;
    }
}
