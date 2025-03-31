package app;

import java.util.*;

public class Activity {
private String name;
private int budgetTime;
private int startWeek;
private int endWeek;
private List<String> employees = new ArrayList<>();
private Map<String,Integer> timeMap =new HashMap<>();

public Activity(String name, int budgetTime, int startWeek, int endWeek){
    this.name = name;
    this.budgetTime = budgetTime;
    this.startWeek = startWeek;
    this.endWeek = endWeek;
}
public void assignEmployeeToActivity(String employeeID){
    this.employees.add(employeeID);
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
    public List<String> getEmployees(){
    return this.employees;
    }

}
