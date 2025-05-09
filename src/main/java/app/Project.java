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



    public void addActivity(Activity activity, String userID) throws IllegalArgumentException {
        if (activity.getEndWeek() < activity.getStartWeek()) {
            throw new IllegalArgumentException("End week cannot be before start week");
        }
        if (userID == null || userID.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (projectLeaderID != null) {
            if (!userID.equals(projectLeaderID)) {
                throw new IllegalArgumentException("Only the project leader can add activities");
            }
        }
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null");
        }
        if (activity.getName() == null || activity.getName().isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be null or empty");
        }
        for (Activity existingActivity : activities) {
            if (existingActivity.getName().equals(activity.getName())) {
                throw new IllegalArgumentException("Activity with the same name already exists");
            }
        }
        this.activities.add(activity);
    }

    public List<Activity> getActivities() {
        return this.activities;
    }
    public Activity getActivityByName(String name) {
        for (Activity activity : activities) {
            if (activity.getName().equals(name)) {
                return activity;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getClient() {return this.client;}


    public void setProjectLeader(String projectLeaderID, String userID) throws IllegalArgumentException {
        if (projectLeaderID == null || projectLeaderID.isEmpty()) {
            throw new IllegalArgumentException("Project leader ID cannot be null or empty");
        }
        if (this.projectLeaderID != null){
            if (!userID.equals(this.projectLeaderID)){
                throw new IllegalArgumentException("Only the project leader can assign a project leader");
            }
        }
        this.projectLeaderID = projectLeaderID;
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

    public void setProjectID(int id) {
        this.projectID = id;
    }

    public int getProjectID() {
        return this.projectID;
    }

    public void removeActivityByName(String name) {
        activities.removeIf(activity -> activity.getName().equals(name));
    }


}
