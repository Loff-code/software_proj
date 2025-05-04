package app;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;


public class PM_App extends Observable  {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    public void addObserver(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    public  PM_App(){
        this.users.add(new User("huba"));
    }
    private List<Project> projects = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private DateServer dateServer;


    private boolean isEmployer;
    private String userID = "";

    public void createUser(String id) throws OperationNotAllowedException {
        boolean idTaken = false;
        for (User user : users) {
            if (user.getID().equals(id)) {
                idTaken = true;
                throw new OperationNotAllowedException("User ID is taken");
            }
        }
        if (!id.equals("") && !idTaken) {
            this.users.add(new User(id));
        }
    }

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

        for (Project project : this.projects){
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


    // register  date

    public void  setDateServer(DateServer dateServer){
        this.dateServer = dateServer;
    }

    public DateServer getDateServer() {
        return this.dateServer;
    }




}
