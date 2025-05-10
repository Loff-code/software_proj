package app;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


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
    private String userID = "";

    public void createUser(User newUser) throws OperationNotAllowedException {
        String newUserID = newUser.getID();
        for (User user1 : users) {
            if (user1.getID().equals(newUserID)) {
                throw new OperationNotAllowedException("User ID is taken");
            }
        }
        if (newUserID.length() < 1 || newUserID.length() > 4) {
            throw new OperationNotAllowedException("User ID must be between 1 and 4 characters");
        }
            this.users.add(newUser);

    }

    public List<Project> getProjects() {
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
    public List<String> getAvailableUserIDsForActivity(String projectName, String activityName) throws OperationNotAllowedException {
        Activity activity = getProject(projectName).getActivityByName(activityName);
        List<String>  availables = getAvailableUserIDs(activity.getStartWeek(), activity.getEndWeek());
            for (String userID : activity.getAssignedUsers()) {
                availables.remove(userID);
            }
        return  availables;
    }
    public String getProjectLeaderID(int projectID) throws OperationNotAllowedException {
        Project project = getProject(projectID);
        return project.getProjectLeaderID();
    }


    public void assignActivityToUser(String userID, String activityName, String projectName) throws OperationNotAllowedException {
        Activity  activity = getProject(projectName).getActivityByName(activityName);
        if (!isAvailable(userID, activity.getStartWeek(), activity.getEndWeek())) {
            throw new OperationNotAllowedException("User has already 20 Activities in this week");
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


    public int createProjectID() {
        int year = 25;
        int counterForProjectsCreatedThisYear = 0;
        for (Project project : projects){
            if (project.getProjectID()/1000 == year){
                counterForProjectsCreatedThisYear++;
            }
        }
        return year * 1000 + counterForProjectsCreatedThisYear;
    }

    public void createProject(Project project) throws OperationNotAllowedException{

        for (Project project1 : projects){
            if (project1.getName().equals(project.getName())){
                throw new OperationNotAllowedException("Project name is taken");
            }
        }
        if (project.getName().length() < 1 || project.getName().length() > 20) {
            throw new OperationNotAllowedException("Project name must be between 1 and 20 characters");
        }
        if (project.getClient().length() < 1 || project.getClient().length() > 20) {
            throw new OperationNotAllowedException("Client name must be between 1 and 20 characters");
        }

            project.setProjectID(createProjectID());
            this.projects.add(project);

    }

    public Project getProject(String name) throws OperationNotAllowedException {
        for (Project project : this.projects){
            if (project.getName().equals(name)){
                return project;
            }

        }
        throw new OperationNotAllowedException("Project does not exist");
    }

    public Project getProject(int id) throws  OperationNotAllowedException {
        for (Project project : this.projects){
            if (project.getProjectID() == id){
                return project;
            }
        }
        throw new OperationNotAllowedException("Project does not exist");
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

    public void assignProjectLeader(String projectName, String assignedUserID) throws IllegalArgumentException, OperationNotAllowedException {
        Project project = getProject(projectName);
        project.setProjectLeader(assignedUserID,userID);

    }
    public void assignProjectLeader(int projectID, String assignedUserID) throws IllegalArgumentException, OperationNotAllowedException {
        Project project = getProject(projectID);
        project.setProjectLeader(assignedUserID,userID);

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


    public void registerTimeForActivity(String userID, String activityName, Double hours, String dateStr) throws OperationNotAllowedException {
        Activity activity = getActivityByName(activityName);
        LocalDate date = dateServer.parseDate(dateStr);
        activity.registerTime(userID, hours, date, activityName, dateServer);
    }




    public void addActivityToProject(String projectName, Activity activity) throws IllegalArgumentException, OperationNotAllowedException {
        Project project = getProject(projectName);
        project.addActivity(activity,userID);
    }

    public void addActivityToProject(int projectID, Activity activity) throws IllegalArgumentException, OperationNotAllowedException {
        Project project = getProject(projectID);
      project.addActivity(activity,userID);
    }











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



    public void editActivityInProject(String projectName, String oldName, String newName, int budgetHours, int startWeek, int endWeek) throws OperationNotAllowedException {
        System.out.println("🔧 editActivityInProject called with projectName='" + projectName + "', oldName='" + oldName + "', newName='" + newName + "'");

        Project project = getProject(projectName);
        Activity activity = project.getActivityByName(oldName);

        if (activity == null) {
            throw new OperationNotAllowedException("Not allowed: Activity not found");
        }

        if (!oldName.equals(newName) && project.getActivityByName(newName) != null) {
            throw new OperationNotAllowedException("Not allowed: Activity with the new name already exists");
        }

        if (!oldName.equals(newName)) {
            // Skab ny aktivitet med nye værdier
            Activity newActivity = new Activity(newName, budgetHours, startWeek, endWeek);

            // Kopier tilknyttede brugere
            for (String user : activity.getAssignedUsers()) {
                newActivity.getAssignedUsers().add(user);
            }

            // Kopier status og log
            newActivity.setStatus(activity.getStatus());
            for (String logEntry : activity.getLog()) {
                newActivity.addLog(logEntry);
            }

            // Kopier time-registreringer
            newActivity.getTimeMap().putAll(activity.getTimeMap());

            // Fjern gammel og tilføj ny
            project.removeActivityByName(oldName);
            project.getActivities().add(newActivity);
        } else {
            // Samme navn – opdater direkte
            activity.setBudgetTime(budgetHours);
            activity.setStartWeek(startWeek);
            activity.setEndWeek(endWeek);
        }

        // Debug
        System.out.println("✅ Aktiviteter i projekt '" + projectName + "' efter ændring:");
        for (Activity a : project.getActivities()) {
            System.out.println("• " + a.getName() + " | " + a.getBudgetTime() + "h | uge " + a.getStartWeek() + "–" + a.getEndWeek());
        }
    }

}

