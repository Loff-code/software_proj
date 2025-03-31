package app;

import java.util.*;


public class PM_App {

    public  PM_App(Employer employer,Employee employee){
        this.employers.add(employer);
        this.employees.add(employee);
    }
    private List<Project> projects = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();
    private List<Employer> employers = new ArrayList<>();
    private boolean isEmployer;
    private String userID = "";

    private boolean loggedIn = false;


    public List<Project> getProject() {
        return projects;
    }

    public List<Employer> getEmployers() {
        return employers;
    }

    public boolean isEmployer() {
        return isEmployer;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public int getEmployeeActivityCount(String ID) {
        int cnt = 0;
        for (Project proj : this.projects) {
            for (Activity activity : proj.getActivities()) {
                for (String employeeID : activity.getEmployees()) {
                    if (employeeID.equals(ID)) {
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    public void registerEmployee(Employee newEmployee)  {
        for (Employee existingEmployee : employees) {
            if (existingEmployee.getID().equals(newEmployee.getID())
                    &&existingEmployee.getName().equals(newEmployee.getName())
                    && existingEmployee.getEmail().equals(newEmployee.getEmail())) {
                System.out.println(Arrays.toString(employees.toArray()));
            }
        }
        // No duplicates found; add the user
        employees.add(newEmployee);
    }


    //Register Employer ændres i fremtiden, skal tjekke for ikke samme som employee
    public void registerEmployer(String name, String email, String ID, String password){
        employers.add(new Employer( name,  email,  ID,  password));
    }

    public Employee getEmployeeByID (String ID){
        for (Employee employee : employees){
            if (employee.getID().equals(ID)){
                return employee;
            }
        }
        return null;
    }
    public Employer getEmployerByID (String ID){
        for (Employer employer : employers){
            if (employer.getID().equals(ID)){
                return employer;
            }
        }
        return null;
    }

    public boolean isAvailable(String employeeId, int startWeek, int endWeek) {
        for (Project project : projects) {
            for (Activity activity : project.getActivities()) {
                if (activity.getEmployees().contains(employeeId)
                        && activity.getStartWeek() < endWeek
                        && activity.getEndWeek() > startWeek) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public List<String> getAvailableEmployeeIDs(int startWeek, int endWeek){
        List<String>  availables = new ArrayList<>();
        for (Employee employee : employees){
            if (isAvailable(employee.getID(),startWeek,endWeek)){
                availables.add(employee.getID());
            }
        }
        return  availables;
    }




// SKal udvides, mangles at teste for dupp navn
    public void createProject(String name, Client client){
        boolean nameTaken = false;
        for (Project project : projects){
            if (project.getName().equals(name)&& client != null){
                nameTaken = true;
            }
        }

        if (!name.equals("") && client != null && !nameTaken){
            this.projects.add(new Project(name,  client));
        }


    }

    public Project getProjectByName(String name){
        for (Project project : this.projects){
            if (project.getName().equals(name)){
                return project;
            }

        }
        return null;
    }


    public String getUserID() {
        return userID;
    }

    public boolean LoggedIn() {
        return this.loggedIn;
    }
    public void login(String ID, String password) throws OperationNotAllowedException{
        for (Employee employee : employees){
            if(employee.getID().equals(ID) && employee.getPassword().equals(password)) {
                this.userID = employee.getID();
            }

        }
        for (Employer employer : employers){
            if(employer.getID().equals(ID) && employer.getPassword().equals(password)) {
                this.userID = employer.getID();
            }
        }
        if (!this.LoggedIn()) {
            throw new OperationNotAllowedException("Wrong password");
        }
    }



    public void logout(){
        this.userID = "";
    }


}
