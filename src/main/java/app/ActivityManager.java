package app;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
    private String projectLeaderID;
    public ActivityManager(List<String> userIDs, String projectLeaderID){
this.projectLeaderID =projectLeaderID;
    }
    private List<Activity> activities = new ArrayList<>();

    public void addActivity(Activity activity, String currentUser) throws OperationNotAllowedException {
        if (projectLeaderID != null ? (currentUser.equals(projectLeaderID)) : true){
            for (Activity existing : activities) {
                if (existing.getName().equals(activity.getName())) {
                    throw new OperationNotAllowedException("Activity name is taken");
                }
            }
            this.activities.add(activity);
        }

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





}
