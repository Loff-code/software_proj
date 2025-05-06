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



public void assignEmployeeToActivity(String userID)throws IllegalArgumentException{
    if (this.assignedUsers.contains(userID)){
        throw new IllegalArgumentException("User is already assigned to this activity");
    }

    this.assignedUsers.add(userID);
}
public void setBudgetTime(int budgetTime) throws IllegalArgumentException{
    if (budgetTime < 0){
        throw new IllegalArgumentException("Budget time cannot be negative");
    }
    this.budgetTime = budgetTime;
}
public void setStartWeek(int startWeek) throws IllegalArgumentException{
    if (startWeek < 0){
        throw new IllegalArgumentException("Start week cannot be negative");
    }
    this.startWeek = startWeek;
}
public void setEndWeek(int endWeek) throws IllegalArgumentException{
    if (endWeek < 0){
        throw new IllegalArgumentException("End week cannot be negative");
    }
    if (endWeek < startWeek){
        throw new IllegalArgumentException("End week cannot be before start week");
    }
    this.endWeek = endWeek;
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

