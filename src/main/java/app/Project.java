package app;

import java.util.*;

public class Project {

    private String name;
    private Client client;
    private List<Activity> activities = new ArrayList<>();
    private String projectLeaderID;
    private Employee projectLeader;
    private int projectID;

    public Project(String name, Client client) {
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


    public void setProjectLeader(Employee employee) {
        this.projectLeader = employee;
        this.projectLeaderID = employee.getID();

    }

    public Employee getProjectLeader() {
        return this.projectLeader;
    }

    public String getProjectLeaderID() {
        return this.projectLeaderID;
    }

    public void assignProjectID(int id) {
        this.projectID = id;
    }
}
