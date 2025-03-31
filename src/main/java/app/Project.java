package app;

import java.util.*;
public class Project {

private String name;
private Client client;
private List<Activity> activities = new ArrayList<>();
private String projectLeaderID;

public Project (String name, Client client) {
    this.name = name;
    this.client = client;
}


public void addActivity (String name, int budgetTime, int startWeek, int endWeek){
    this.activities.add(new Activity(name,  budgetTime,  startWeek,  endWeek));
}

public List<Activity> getActivities (){
    return this.activities;
}

public void assignProjectLeader(String ID){
    this.projectLeaderID = ID;
}

public String getProjectLeaderID(){
    return this.projectLeaderID;

}

}
