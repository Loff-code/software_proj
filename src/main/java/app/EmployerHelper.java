package app;

public class EmployerHelper {
    private Employer employer;
    public Employer getEmployer(){
        return exampleEmployer();
    }
    public Employer exampleEmployer(){
        this.employer = new Employer("Carl", "carl@world.com", "0001", "CarlCarl");
        return employer;
    }
}
