package app;

import java.util.*;
import java.time.LocalDate;

public class Activity {
private String name;
private int budgetTime;
private int startWeek;
private int endWeek;
private int startYear;
private int endYear;
private List<String> assignedUsers = new ArrayList<>();
private Map<String,Double> timeMap =new HashMap<>();
    private String status = "";
    private List<String> log = new ArrayList<>();

public Activity(String name, int budgetTime, int startYear,int startWeek, int endYear, int endWeek){
    this.name = name;
    this.budgetTime = budgetTime;
    this.startWeek = startWeek;
    this.endWeek = endWeek;
    this.startYear = startYear;
    this.endYear = endYear;
}



    public void addLog(String entry) {this.log.add(entry);}
    public void setStatus(String status) {this.status = status;}
    public String getStatus() {return this.status;}
    public List<String> getLog() {return this.log;}
    public Map<String, Double> getTimeMap() {return this.timeMap;}
    public String getName(){return this.name;}
    public int getBudgetTime(){return this.budgetTime;}
    public int getStartWeek(){return this.startWeek;}
    public int getEndWeek(){return this.endWeek;}
    public int getStartYear(){return this.startYear;}
    public int getEndYear(){return this.endYear;}
    public List<String> getAssignedUsers(){return this.assignedUsers;}

public void assignEmployeeToActivity(String userID)throws IllegalArgumentException{
    if (this.assignedUsers.contains(userID)){
        throw new IllegalArgumentException("User is already assigned to this activity");
    }

    this.assignedUsers.add(userID);
}
public void setName(String name) throws IllegalArgumentException{
    if (name.isBlank()){
        throw new IllegalArgumentException("Name cannot be blank");
    }
    this.name = name;
}
public void setBudgetTime(int budgetTime) throws IllegalArgumentException{
    if (budgetTime < 0){
        throw new IllegalArgumentException("Budget time cannot be negative");
    }
    this.budgetTime = budgetTime;
}
public void setStartWeek(int startWeek) throws IllegalArgumentException{
    if (startWeek < 0 || startWeek > 52){
        throw new IllegalArgumentException("Start week cannot be negative");
    }
    this.startWeek = startWeek;
}
public void setEndWeek(int endWeek) throws IllegalArgumentException{
    if (endWeek < 0 || endWeek > 52){
        throw new IllegalArgumentException("End week cannot be negative");
    }
    if (endWeek < startWeek){
        throw new IllegalArgumentException("End week cannot be before start week");
    }
    this.endWeek = endWeek;
}
    public void setStartYear(int startYear) throws IllegalArgumentException{
        if (startYear < 0){
            throw new IllegalArgumentException("Start Year cannot be negative");
        }
        this.startYear = startYear;
    }
    public void setEndYear(int endYear) throws IllegalArgumentException{
        if (endYear < 0 ){
            throw new IllegalArgumentException("End year cannot be negative");
        }
        if (endYear < startYear){
            throw new IllegalArgumentException("End year cannot be before start year");
        }
        this.endYear = endYear;
    }

    public void registerTime(String userID, double hours, LocalDate date, DateServer dateServer) throws IllegalArgumentException{

        if(userID.isBlank()){
            throw new IllegalArgumentException("Not allowed: User ID cannot be blank");
        }
        if (hours <= 0 ){
            throw new IllegalArgumentException("Not allowed: Hours have to be positive");
        }
        if (date == null){
            throw new IllegalArgumentException("Not allowed: Date cannot be null");
        }
        // runde op til nærmeste halve time
        double roundedHours = (double) Math.round(hours * 2.0) / 2;
        String formattedDate = dateServer.dateToString(date);
        timeMap.put("[" + userID + "]|" + getName() +"|"+ formattedDate, roundedHours);
    }


    public List<String> getRegisteredTimesForUser(String userID) {
        List<String> result = new ArrayList<>();

        for (Map.Entry<String, Double> entry : timeMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("[" + userID + "]|")) {
                result.add(key + " = " + entry.getValue() + " hours");
            }
        }

        return result;
    }

    // Method to calculate total used hours for the activity
    public double getTotalUsedHours() {
        double totalUsedHours = 0.0;
        // Iterate through timeMap to sum all logged hours
        for (Double hours : timeMap.values()) {
            totalUsedHours += hours;
        }
        return totalUsedHours;
    }

    // Method to get the users who have logged time for this activity
    public List<String> getUsersWithLoggedTime() {
        List<String> usersWithLoggedTime = new ArrayList<>();
        for (String key : timeMap.keySet()) {
            String userID = key.split("\\|")[0].replace("[", "").replace("]", "");
            if (!usersWithLoggedTime.contains(userID)) {
                usersWithLoggedTime.add(userID);
            }
        }
        return usersWithLoggedTime;
    }

    public int getUsersHoursForToday(String userID, String today) {
        int totalHours = 0;
        for (Map.Entry<String, Double> entry : timeMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("[" + userID + "]|")) {
                String date = key.split("\\|")[2];
                if (date.equals(today)) {
                    totalHours += entry.getValue();
                }
            }
        }
        return totalHours;

    }
}

