package app;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class PM_App extends Observable {

    /* ── FIELDS & BASIC STATE ────────────────────────────────────────── */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final List<Project> projects = new ArrayList<>();
    private final List<User>    users    = new ArrayList<>();

    private DateServer dateServer = new RealDateServer();
    private String loggedInUserID = null;

    /* ── CONSTRUCTOR & OBSERVERS ─────────────────────────────────────── */
    public PM_App() {users.add(new User("huba")); projects.add(new Project("Fravær", "InHouse")); projects.get(0).setProjectID(1);}

    public void addObserver(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /* ── SIMPLE ACCESSORS ────────────────────────────────────────────── */
    public List<Project> getProjects() { return projects; }
    public List<User>    getUsers()    { return users;    }
    public String getLoggedInUserID()   { return loggedInUserID;   }
    public DateServer    getDateServer() { return dateServer; }
    public void setDateServer(DateServer dateServer) { this.dateServer = dateServer; }
    public void logout() { loggedInUserID = null; }

    /* ── USER MANAGEMENT ─────────────────────────────────────────────── */
    public void createUser(User newUser) throws OperationNotAllowedException {
        String newUserID = newUser.getID();
        if (loggedInUserID == null || loggedInUserID.isEmpty()) {
            throw new OperationNotAllowedException("User must be logged in");
        }
       try {
            getUserByID(newUserID);
            throw new IllegalArgumentException("User ID is already taken");
        } catch (OperationNotAllowedException e) {
           users.add(newUser);
        }
    }

    public User getUserByID(String ID) throws OperationNotAllowedException {
        if (ID.length() < 1 || ID.length() > 4) {
            throw new IllegalArgumentException("User ID must be between 1 and 4 characters");
        }
        for (User user : users) {
            if (user.getID().equals(ID)) {
                return user;
            }
        }
        throw new OperationNotAllowedException("User does not exist");
    }

    public void login(String id) throws OperationNotAllowedException {
        this.loggedInUserID = getUserByID(id).getID();// makes error if user does not exist
    }

    /* ── AVAILABILITY ────────────────────────────────────────────────── */
    public int getUserActivityCountByWeek(String userID, int startYear, int startWeek, int endYear, int endWeek) {
        int count = 0;
        for (Project project : projects) {
            for (Activity activity : project.getActivities()) {
                if (activity.getAssignedUsers().contains(userID)) {
                    boolean yearInRange =
                            (activity.getStartYear() < endYear ||
                                    (activity.getStartYear() == endYear && activity.getStartWeek() <= endWeek)) &&
                                    (activity.getEndYear() > startYear ||
                                            (activity.getEndYear() == startYear && activity.getEndWeek() >= startWeek));

                    if (yearInRange) {
                        count++;
                    }
                }
            }
        }
        return count;
    }


    public boolean isVacant(String userID, int startYear, int startWeek, int endYear, int endWeek) {
        return getUserActivityCountByWeek(userID,startYear, startWeek,endYear, endWeek) == 0;
    }

    public boolean isAvailable(String userID, int startYear, int startWeek, int endYear, int endWeek) {
        int year = startYear;
        int week = startWeek;

        while (year < endYear || (year == endYear && week <= endWeek)) {
            if (getUserActivityCountByWeek(userID, year, week, year, week) >= 20) {
                return false;
            }

            // Advance to next week, accounting for week rollover
            week++;
            if (week > 52) {
                week = 1;
                year++;
            }
        }

        return true;
    }


    public List<String> getAvailableUserIDs(int startYear, int startWeek, int endYear, int endWeek) {
        List<String> availables = new ArrayList<>();
        if ((startWeek < 1 || endWeek > 52 || endWeek < 1 || startWeek > 52) ) {
            throw new IllegalArgumentException("Weeks must be between 1 and 52");
        }
        if ((endWeek < startWeek && endYear == startYear) || (endYear < startYear)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        for (User user : users) {
            if (isAvailable(user.getID(), startYear, startWeek, endYear, endWeek)) {
                availables.add(user.getID());
            }
        }
        return availables;
    }











    public List<String> getAvailableUserIDsForActivity(int projectID, String activityName) throws OperationNotAllowedException {
        Activity activity = getActivityByName(activityName, projectID);
        List<String> availables = getAvailableUserIDs(
                activity.getStartYear(), activity.getStartWeek(),
                activity.getEndYear(), activity.getEndWeek());

        for (String id : activity.getAssignedUsers()) {
            availables.remove(id);
        }
        return availables;
    }

    public List<String> getVacantUserIDs(int startYear,int startWeek, int endYear, int endWeek) throws OperationNotAllowedException {

        if ((startWeek < 1 || endWeek > 52 || endWeek < 1 || startWeek > 52) ) {
            throw new OperationNotAllowedException("Weeks must be between 1 and 52");
        }
        if  ((endWeek < startWeek && endYear == startYear) || (endYear < startYear)) {
            throw new OperationNotAllowedException("End time cannot be before start time");
        }
        List<String> vacant = new ArrayList<>();
        for (User user : users) {
            if (isVacant(user.getID(), startYear, startWeek, endYear, endWeek)) {
                vacant.add(user.getID());
            }
        }
        return vacant;
    }

    /* ── PROJECT CREATION & LOOK-UP ──────────────────────────────────── */
    public int createProjectID() {
        int year = dateServer.getYear() % 100;
        int count = 0;
        for (Project p : projects) {
            if (p.getProjectID() / 1000 == year) {
                count++;
            }
        }
        return year * 1000 + count;
    }

    public void createProject(Project project) throws OperationNotAllowedException {
        if (loggedInUserID == null) {
            throw new OperationNotAllowedException("User is not logged in");
        }
        if (project.getName().length() < 1 || project.getName().length() > 20) {
            throw new OperationNotAllowedException("Project name must be between 1 and 20 characters");
        }
        if (project.getClient().length() < 1 || project.getClient().length() > 20) {
            throw new OperationNotAllowedException("Client name must be between 1 and 20 characters");
        }
        project.setProjectID(createProjectID());
        projects.add(project);
    }

    public Project getProject(int id) throws OperationNotAllowedException {
        for (Project project : projects) {
            if (project.getProjectID() == id) {
                return project;
            }
        }
        throw new OperationNotAllowedException("Project does not exist");
    }

    public void assignProjectLeader(int projectID, String assignedUserID) throws OperationNotAllowedException {
        getUserByID(assignedUserID); // for errors
        getProject(projectID).setProjectLeader(assignedUserID, loggedInUserID);
    }

    /* ── ACTIVITY MANAGEMENT ─────────────────────────────────────────── */
    public Activity getActivityByName(String name, int projectID) throws OperationNotAllowedException {
        return getProject(projectID).getActivityByName(name);
    }

    public void addActivityToProject(int projectID, Activity activity) throws OperationNotAllowedException {
        getProject(projectID).addActivity(activity, loggedInUserID);
    }

    public void assignUserToActivity(String userID, String activityName, int projectID) throws OperationNotAllowedException {
        Activity activity = getActivityByName(activityName, projectID);

        if (!isAvailable(userID, activity.getStartYear(), activity.getStartWeek(), activity.getEndYear(), activity.getEndWeek())) {
            throw new OperationNotAllowedException("User has already 20 Activities in one or more weeks during this period");
        }

        activity.assignEmployeeToActivity(userID);
    }


    public void registerTimeForActivity(String userID, int projectID, String activityName, double hours, String dateStr) throws OperationNotAllowedException {
        Activity activity = getActivityByName(activityName, projectID);
        LocalDate date = null;
        if (!(dateStr == null || dateStr.isEmpty())) {
            date =dateServer.parseDate(dateStr);
        }
        activity.registerTime(userID, hours, date, dateServer);
    }

    public void setStatusOfActivity(String activityName, int projectID, String status) throws OperationNotAllowedException {
        Activity activity = getActivityByName(activityName, projectID);
        activity.setStatus(status);
        activity.addLog("Status changed to: " + status + " by " + loggedInUserID);
    }

    public List<String> getTimeEntriesForUser(String userID, String activityName, int projectID) throws OperationNotAllowedException {
        Project project = getProject(projectID);
        Activity activity = project.getActivityByName(activityName);
        return activity.getRegisteredTimesForUser(userID);
    }

    public List<String> getUsersEntriesForToday(String userID) {
        String today = dateServer.dateToString(dateServer.getDate());
        List<String> entries = new ArrayList<>();
        for (Project project : projects) {
            for (Activity activity : project.getActivities()) {
                if (activity.getUsersWithLoggedTime().contains(userID)) {
                    entries.add(project.getProjectID() +" "+ project.getName() + " " + activity.getName() + "\n Hours: " + activity.getUsersHoursForToday(userID, today));
                }
            }
        }

        return entries;
    }


    /* ── REPORTS ─────────────────────────────────────────────────────── */
    public List<String> getAssignedActivitiesByUserID(String userID) {
        List<String> list = new ArrayList<>();
        for (Project project : projects) {
            for (Activity activity : project.getActivities()) {
                if (activity.getAssignedUsers().contains(userID)) {
                    list.add(activity.getName() + " " + project.getName() + " " + project.getProjectID());
                }
            }
        }
        return list;
    }

    public String generateReport(String projectID) throws OperationNotAllowedException {
        // Find the project by ID
        Project project = getProject(Integer.parseInt(projectID));
        // Start building the report
        StringBuilder report = new StringBuilder();
        report.append("Report for project: ").append(project.getName()).append("\n");

        // Add activity data to the report
        for (Activity activity : project.getActivities()) {
            // Sum up the total used hours for each activity
            double totalUsedHours = activity.getBudgetTime();  // Assuming Activity has a method to sum up registered hours.
            report.append("Activity: ").append(activity.getName())
                    .append(" | Used Hours: ").append(totalUsedHours).append("\n");
        }

        return report.toString(); // Return the complete report as a string
    }











    public String getStatusReport(int startYear, int startWeek, int endYear, int endWeek) {
        StringBuilder report = new StringBuilder();
        report.append("Project Status Report (Weeks ").append(startWeek).append(" - ").append(endWeek).append(")\n");
        report.append("---------------------------------------------------------------\n");

        for (Project project : projects) {
            if (project.getProjectID() == 1) continue; // Skip the leave project

            StringBuilder projectReport = new StringBuilder();
            boolean hasRelevantActivities = false;

            for (Activity activity : project.getActivities()) {
                boolean overlaps = (activity.getStartYear() < endYear || (activity.getStartYear() == endYear && activity.getStartWeek() <= endWeek)) &&(activity.getEndYear() > startYear || (activity.getEndYear() == startYear && activity.getEndWeek() >= startWeek));

                if (overlaps) {
                    hasRelevantActivities = true;

                    projectReport.append("  Activity: ").append(activity.getName()).append("\n");
                    projectReport.append("    Status: ").append(activity.getStatus()).append("\n");
                    projectReport.append("    Budgeted Hours: ").append(activity.getBudgetTime()).append("\n");

                    double totalUsedHours = activity.getTotalUsedHours();
                    projectReport.append("    Used Hours: ").append(totalUsedHours).append("\n");

                    projectReport.append("    Assigned Users: ");
                    if (activity.getAssignedUsers().isEmpty()) {
                        projectReport.append("None\n");
                    } else {
                        for (String userID : activity.getAssignedUsers()) {
                            projectReport.append(userID).append(" ");
                        }
                        projectReport.append("\n");
                    }

                    projectReport.append("    Users who have logged time: ");
                    if (activity.getUsersWithLoggedTime().isEmpty()) {
                        projectReport.append("None\n");
                    } else {
                        for (String userID : activity.getUsersWithLoggedTime()) {
                            projectReport.append(userID).append(" ");
                        }
                        projectReport.append("\n");
                    }
                }
            }

            if (hasRelevantActivities) {
                report.append("Project: ").append(project.getProjectID()).append(" - ").append(project.getName()).append("\n");
                report.append(projectReport).append("\n");
            }
        }

        return report.toString();
    }



    public void createLeaveRequest(String userID, String activityName, int projectID, double hours, String dateStrStart, String dateStrEnd) throws OperationNotAllowedException {
        LocalDate dateStart = dateServer.parseDate(dateStrStart);
        LocalDate dateEnd = dateServer.parseDate(dateStrEnd);
        String newActivityName = "[" + activityName + "] " + userID + " " + dateStrStart + " - " + dateStrEnd;

        int startWeek = dateStart.getDayOfYear() / 7 + 1;
        int endWeek = dateEnd.getDayOfYear() / 7 + 1;
        int startYear = dateStart.getYear();
        int endYear = dateEnd.getYear();
        Activity activity = new Activity(newActivityName, (int) hours, startYear, startWeek, endYear, endWeek);
        addActivityToProject(projectID, activity);
        activity.registerTime(userID, hours, dateStart, dateServer);
    }



    public String getLeaveStatusReport(int startYear, int startWeek, int endYear, int endWeek) {
        StringBuilder report = new StringBuilder();
        report.append("Current Leave Status Report (Weeks ").append(startWeek).append(" - ").append(endWeek).append(")\n");
        report.append("---------------------------------------------------------------\n");

        for (Project project : projects) {
            if (project.getProjectID() != 1) continue;

            StringBuilder projectReport = new StringBuilder();
            boolean hasRelevantActivities = false;

            for (Activity activity : project.getActivities()) {
                boolean overlaps =
                        (activity.getStartYear() < endYear ||
                                (activity.getStartYear() == endYear && activity.getStartWeek() <= endWeek)) &&
                                (activity.getEndYear() > startYear ||
                                        (activity.getEndYear() == startYear && activity.getEndWeek() >= startWeek));

                if (overlaps) {
                    hasRelevantActivities = true;

                    projectReport.append("  Activity: ").append(activity.getName()).append("\n");
                    projectReport.append("    Status: ").append(activity.getStatus()).append("\n");
                    projectReport.append("    Budgeted Hours: ").append(activity.getBudgetTime()).append("\n");

                    double totalUsedHours = activity.getTotalUsedHours();
                    projectReport.append("    Used Hours: ").append(totalUsedHours).append("\n");

                    projectReport.append("    Users who have logged time: ");

                        for (String userID : activity.getUsersWithLoggedTime()) {
                            projectReport.append(userID).append(" ");
                        }
                        projectReport.append("\n");
                    }

            }

            if (hasRelevantActivities) {
                report.append("Project: ").append(project.getProjectID()).append(" - ").append(project.getName()).append("\n");
                report.append(projectReport).append("\n");
            }
        }

        return report.toString();
    }








}
