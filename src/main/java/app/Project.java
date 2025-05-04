package app;

import java.util.*;

public class Project {
    private ProjectInfo info;
    private ProjectLeaderManager leaderManager;
    private ActivityManager activityManager;

    public Project(String name, String client, List<String> userIDs) {
        this.info = new ProjectInfo(name, client);
        this.leaderManager = new ProjectLeaderManager(userIDs);
        this.activityManager = new ActivityManager(userIDs,this.leaderManager.getLeaderID());

    }

    public ProjectInfo getInfo() { return info; }
    public ProjectLeaderManager getLeaderManager() { return leaderManager; }
    public ActivityManager getActivityManager() { return activityManager; }
}
