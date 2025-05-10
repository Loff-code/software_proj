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

    public void addActivity(Activity activity, String userID) {
        if (activity == null || activity.getName() == null || activity.getName().isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be null or empty");
        }
        if (activity.getEndWeek() < activity.getStartWeek()) {
            throw new IllegalArgumentException("End week cannot be before start week");
        }
        if (userID == null || userID.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (projectLeaderID != null && !userID.equals(projectLeaderID)) {
            throw new IllegalArgumentException("Only the project leader can add activities");
        }
        if (getActivityByName(activity.getName()) != null) {
            throw new IllegalArgumentException("Activity with the same name already exists");
        }
        activities.add(activity);
    }

    public List<Activity> getActivities() { return activities; }

    public Activity getActivityByName(String name) {
        for (Activity a : activities) {
            if (a.getName().equals(name)) { return a; }
        }
        return null;
    }

    public String getName()           { return name; }
    public String getClient()         { return client; }
    public String getProjectLeaderID(){ return projectLeaderID; }
    public int    getProjectID()      { return projectID; }
    public void setProjectID(int id)   { this.projectID = id; }
    public void setProjectLeader(String projectLeaderID, String userID) {
        if (projectLeaderID == null || projectLeaderID.isEmpty()) {
            throw new IllegalArgumentException("Project leader ID cannot be null or empty");
        }
        if (this.projectLeaderID != null && !userID.equals(this.projectLeaderID)) {
            throw new IllegalArgumentException("Only the project leader can assign a project leader");
        }
        this.projectLeaderID = projectLeaderID;
    }


}
