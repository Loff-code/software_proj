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

    private DateServer dateServer ;
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
        if (id.length() < 1 || id.length() > 4) {
            throw new OperationNotAllowedException("User ID must be between 1 and 4 characters");
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

    // Get the number of activities assigned to a user in a specific week range
    public int getUserActivityCountByWeek(String userID, int startWeek, int endWeek) {
        int count = 0;
        for (Project project : projects) {
            for (Activity activity : project.getActivities()) {
                if (activity.getAssignedUsers().contains(userID)  && activity.getStartWeek() < startWeek
                        && activity.getEndWeek() > endWeek ) {
                    count++;
                }
            }
        }
        return count;
    }


    public User getUserByID (String ID) throws  OperationNotAllowedException{
        for (User user : users){
            if (user.getID().equals(ID)){
                return user;
            }
        }
        throw new OperationNotAllowedException("User does not exist");
    }

    // Check if user is vacant for the given weeks
    public boolean isVacant(String userID, int startWeek, int endWeek) {
     return getUserActivityCountByWeek(userID, startWeek, endWeek) == 0;
    }

    // Check if user is available for the given weeks
    public boolean isAvailable(String userID, int startWeek, int endWeek) {
        for (int i = startWeek; i <= endWeek; i++) {
            if (getUserActivityCountByWeek(userID, startWeek, i) >= 20){
                return false;
            }

        }
    return true;
    }

    // Mainly for UI
    public List<String> getAvailableUserIDs(int startWeek, int endWeek){
        List<String>  availables = new ArrayList<>();
        for (User user : users){
            if (isAvailable(user.getID(),startWeek,endWeek)){
                availables.add(user.getID());
            }
        }
        return  availables;
    }
    // Mainly for UI
    public List<String> getAvailableUserIDsForActivity(String projectName, String activityName){
        Activity activity = getProjectByName(projectName).getActivityByName(activityName);
        List<String>  availables = getAvailableUserIDs(activity.getStartWeek(), activity.getEndWeek());
            for (String userID : activity.getAssignedUsers()) {
                availables.remove(userID);
            }
        return  availables;
    }


    public void assignActivityToUser(String userID, String activityName, String projectName) throws OperationNotAllowedException {
        Activity  activity = getProjectByName(projectName).getActivityByName(activityName);

        if (activity.getAssignedUsers().contains(userID)) {
            throw new OperationNotAllowedException("User is already assigned to this activity");
        }
        if (!isAvailable(userID, activity.getStartWeek(), activity.getEndWeek())) {
            throw new OperationNotAllowedException("User has already 20 Activities in this week");
        }
        for (String user : activity.getAssignedUsers()) {
            if (user.equals(userID)) {
                throw new OperationNotAllowedException("User is already assigned to this activity");
            }
        }
        activity.assignEmployeeToActivity(userID);
    }

    public List<String> getVacantUserIDs(int startWeek, int endWeek){
        List<String>  vacantUserIDs = new ArrayList<>();
        for (User user : users){
            if (isVacant(user.getID(),startWeek,endWeek)){
                vacantUserIDs.add(user.getID());
            }
        }
        return  vacantUserIDs;
    }


    public void createProject(String projectName, String client) throws OperationNotAllowedException {

        //preconditions
        if (projectName == null || projectName.isEmpty()) {
            throw new OperationNotAllowedException("Project name empty");
        }

        if (client == null || client.isEmpty()) {
            throw new OperationNotAllowedException("Client name empty");
        }

        if(projectDuplicateChecker(projectName)){
            throw new OperationNotAllowedException("Project name already exist");
        }

        assert  !projectName.trim().isEmpty() && !client.trim().isEmpty() && projectDuplicateChecker(projectName): "Precondition createProject";

        Project project = new Project(projectName, client);
        projects.add(project);

        assert projects.contains(project) : "Postcondition createProject";
    }

    public boolean projectDuplicateChecker(String projectName) {
        for (Project project : projects) {
            if (getProjectByName(projectName).equals(projectName)) {
                return false;
            }
        }
        return true;
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


    public void assignProjectLeader(String projectID, String assignedUserID) throws OperationNotAllowedException{
        Project project = getProjectByName(projectID);
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



    public void setDateServer(DateServer dateServer){
        this.dateServer = dateServer;
    }

    public Activity getActivityByName(String name) {
        for (Project project : projects) {
            for (Activity activity : project.getActivities()) {
                if (activity.getName().equalsIgnoreCase(name)) {
                    return activity;
                }
            }
        }
        return null;
    }

    public DateServer getDateServer() {return this.dateServer;}






    public void setStatusOfActivity(String activityName, String status) throws OperationNotAllowedException {
        Activity activity = getActivityByName(activityName);
        if (activity == null) {
            throw new OperationNotAllowedException("Activity not found");
        }

        Project containingProject = null;
        for (Project project : projects) {
            if (project.getActivities().contains(activity)) {
                containingProject = project;
                break;
            }
        }

        if (containingProject == null) {
            throw new OperationNotAllowedException("Activity does not belong to any project");
        }

        boolean isAssigned = activity.getAssignedUsers().contains(userID);
        boolean isProjectLeader = userID.equals(containingProject.getProjectLeaderID());

        if (!isAssigned && !isProjectLeader) {
            throw new OperationNotAllowedException("Only assigned employees or the project leader can set the status");
        }

        activity.setStatus(status);
        activity.addLog("Status changed to: " + status + " by " + userID);
    }


}

