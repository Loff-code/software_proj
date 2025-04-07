package app;

import java.util.*;

public class Project {

    private String name;
    private String client;
    private List<Activity> activities = new ArrayList<>();
    private String projectLeaderID;
    private User projectLeader;
    private int projectID;

    public Project(String name, String client) {
        this.name = name;
        this.client = client;
    }

    public void addActivity(String name, int budgetTime, int startWeek, int endWeek) {
        this.activities.add(new Activity(name, budgetTime, startWeek, endWeek));
    }

    public List<Activity> getActivities() {
        return this.activities;
    }


    public String getName() {
        return this.name;
    }


    public void setProjectLeader(User user) {
        this.projectLeader = user;
        this.projectLeaderID = user.getID();

    }

    public User getProjectLeader() {
        return this.projectLeader;
    }

    public String getProjectLeaderID() {
        return this.projectLeaderID;
    }

    public void assignProjectID(int id) {
        this.projectID = id;
    }



    //public void assignProjectLeader(String projectName, String employeeID) throws OperationNotAllowedException{
//        Project project = getProjectByName(projectName);
//        if (project == null){
//            throw new OperationNotAllowedException("Project does not exist");
//        }
//
//        User user = getUserByID(userID);
//        if (user == null){
//            throw new OperationNotAllowedException("Employee does not exist");
//        }
//
//        if(project.getProjectLeader() != null && project.getProjectLeader().equals(user)) {
//            throw new OperationNotAllowedException("Project leader already assigned");
//        }
//
//        project.setProjectLeader(user);
//
//
//    }
}
