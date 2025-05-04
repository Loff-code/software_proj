package app;

import java.util.*;

public class Activity {
private String name;
private int budgetTime;
private int startWeek;
private int endWeek;
private List<String> assignedUsers = new ArrayList<>();
private Map<String,Integer> timeMap =new HashMap<>();

public Activity(String name, int budgetTime, int startWeek, int endWeek){
    this.name = name;
    this.budgetTime = budgetTime;
    this.startWeek = startWeek;
    this.endWeek = endWeek;
}
    private String status = "";
    private List<String> log = new ArrayList<>();

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void addLog(String entry) {
        this.log.add(entry);
    }

    public List<String> getLog() {
        return this.log;
    }



public void assignEmployeeToActivity(String userID){
    this.assignedUsers.add(userID);
}
public String getName(){
    return this.name;
}
    public int getBudgetTime(){
        return this.budgetTime;
    }
    public int getStartWeek(){
        return this.startWeek;
    }
    public int getEndWeek(){
        return this.endWeek;
    }
    public List<String> getAssignedUsers(){
    return this.assignedUsers;
    }
}

