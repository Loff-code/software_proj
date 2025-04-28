package app;

import java.util.*;


public class PM_App extends Observable  {

    public  PM_App(){
        this.users.add(new User("bob"));
    }
    private List<Project> projects = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    private boolean isEmployer;
    private String userID = "";

    public List<Project> getProject() {
        return this.projects;
    }

    public List<User> getUsers() {
        return users;
    }
    public int getUserActivityCount(String ID) {
        int cnt = 0;
        for (Project proj : this.projects) {
            for (Activity activity : proj.getActivities()) {
                for (String userID : activity.getAssignedUsers()) {
                    if (userID.equals(ID)) {
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    public User getUserByID (String ID){
        for (User user : users){
            if (user.getID().equals(ID)){
                return user;
            }
        }
        return null;
    }


    public boolean isAvailable(String userID, int startWeek, int endWeek) {
        for (Project project : projects) {
            for (Activity activity : project.getActivities()) {
                if (activity.getAssignedUsers().contains(userID)
                        && activity.getStartWeek() < endWeek
                        && activity.getEndWeek() > startWeek) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public List<String> getAvailableUserIDs(int startWeek, int endWeek){
        List<String>  availables = new ArrayList<>();
        for (User user : users){
            if (isAvailable(user.getID(),startWeek,endWeek)){
                availables.add(user.getID());
            }
        }
        return  availables;
    }


    public void createProject(String name, String client)throws OperationNotAllowedException{
        boolean nameTaken = false;
        for (Project project : projects){
            if (project.getName().equals(name)){
                nameTaken = true;
                throw new OperationNotAllowedException("Project name is taken");
            }
        }

        if (!name.equals("") && !(client.equals("")) && !nameTaken){
            this.projects.add(new Project(name,  client));
        }
    }

    public Project getProjectByName(String name){
        System.out.println(name);
        for (Project project : this.projects){
            System.out.println(project.getName());
            if (project.getName().equals(name)){
                return project;
            }

        }
        return null;
    }

    public Project getProjectByID(int id){
        for (Project project : this.projects){
            if (project.getProjectID() == id){
                return project;
            }
        }
        return null;
    }


    public String getUserID() {
        return userID;
    }


    public void login(String id) throws OperationNotAllowedException{
        for (User user : users){
            if(user.getID().equals(id)) {
                this.userID = user.getID();
            }

        }
        if (this.userID.equals("")){
            throw new OperationNotAllowedException("User does not exist");
        }
    }


    public void logout(){
        this.userID = "";
    }


    public void assignProjectLeader(int projectID, String assignedUserID) throws OperationNotAllowedException{
        Project project = getProjectByID(projectID);
        if (project == null) {
            throw new OperationNotAllowedException("Project does not exist");
        }

        if (project.getProjectLeader() != null) {
            throw new OperationNotAllowedException("User is already a project leader");
        }

        User user = getUserByID(assignedUserID);
        if (user == null) {
            throw new OperationNotAllowedException("User does not exist");
        }

        if(assignedUserID.equals(userID)){
            throw new OperationNotAllowedException("User cannot assign themselves as project leader");
        }

        project.setProjectLeader(user);

    }
}
