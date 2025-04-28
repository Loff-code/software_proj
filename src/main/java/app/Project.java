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


    public int getProjectID() {
        return this.projectID;
    }
}
