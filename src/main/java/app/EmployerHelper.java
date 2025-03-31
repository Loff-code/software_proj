package app;

public class EmployerHelper {
    private Employer employer;
    public Employer getEmployer(){
        return exampleEmployer();
    }
    public Employer exampleEmployer(){
        this.employer = new Employer("Hans Hansen", "hello@world.com", "0001", "password");
        return employer;
    }


}
