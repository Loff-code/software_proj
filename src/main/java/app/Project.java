package app;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String name;
    private String client;
    private List<Activity> activities = new ArrayList<>();
    private String projectLeaderID;
    private int projectID;

    public Project(String name, String client) {
        this.name = name;
        this.client = client;
    }

    public void addActivity(Activity activity, String userID) throws IllegalArgumentException, OperationNotAllowedException {
        if (activity == null || activity.getName() == null || activity.getName().isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be null or empty");
        }

        if (activity.getStartYear() < 0 || activity.getEndYear() < 0) {
            throw new IllegalArgumentException("Year cannot be negative");
        }
        if (userID == null || userID.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (projectLeaderID != null && !userID.equals(projectLeaderID)) {
            throw new IllegalArgumentException("Only the project leader can add activities");
        }
        if (activity.getStartWeek() < 1 || activity.getEndWeek() < 1 || activity.getStartWeek() > 52|| activity.getEndWeek() > 52) {
            throw new IllegalArgumentException("Weeks must be between 1 and 52");
        }
        if (activity.getBudgetTime() < 0) {
            throw new IllegalArgumentException("Budget time cannot be negative");
        }
        if (activity.getEndYear() < activity.getStartYear() ) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        if ((activity.getEndWeek() < activity.getStartWeek() && activity.getEndYear() == activity.getStartYear() )) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        try {
            getActivityByName(activity.getName());
            throw new OperationNotAllowedException("Activity already exists");
        } catch (IllegalArgumentException e) {
            // Activity does not exist, so it's safe to add
        }

        activities.add(activity);
    }

    public List<Activity> getActivities() { return activities; }

    public Activity getActivityByName(String name) throws IllegalArgumentException {
        for (Activity a : activities) {
            if (a.getName().equals(name)) { return a; }
        }
        throw new IllegalArgumentException("Activity does not exist");
    }

    public String getName()           { return name; }
    public String getClient()         { return client; }
    public String getProjectLeaderID(){ return projectLeaderID; }
    public int    getProjectID()      { return projectID; }
    public void setProjectID(int id)   { this.projectID = id; }
    public void setProjectLeader(String projectLeaderID, String userID) {
        if (this.projectLeaderID != null && !userID.equals(this.projectLeaderID)) {
            throw new IllegalArgumentException("Only the project leader can assign a project leader");
        }
        this.projectLeaderID = projectLeaderID;
    }


}
