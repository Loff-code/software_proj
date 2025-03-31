package app;

public class EmployeeHelper {
    private Employee employee;
    public Employee getEmployee(){
        return exampleEmployee();
    }
    public Employee exampleEmployee(){
        this.employee = new Employee("Bob", "bob@world.com", "0002", "BobBob");
        return employee;
    }


}
