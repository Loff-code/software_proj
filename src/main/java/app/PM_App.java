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
    public PM_App() {users.add(new User("huba"));}

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
       try {
            getUserByID(newUserID);
            throw new OperationNotAllowedException("User ID is already taken");
        } catch (OperationNotAllowedException e) {
           users.add(newUser);
        }
    }

    public User getUserByID(String ID) throws OperationNotAllowedException {
        if (ID.length() < 1 || ID.length() > 4) {
            throw new OperationNotAllowedException("User ID must be between 1 and 4 characters");
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
    public int getUserActivityCountByWeek(String userID, int startWeek, int endWeek) {
        int count = 0;
        for (Project project : projects) {
            for (Activity activity : project.getActivities()) {
                if (activity.getAssignedUsers().contains(userID)
                        && activity.getStartWeek() < startWeek
                        && activity.getEndWeek()   > endWeek) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isVacant(String userID, int startWeek, int endWeek) {
        return getUserActivityCountByWeek(userID, startWeek, endWeek) == 0;
    }

    public boolean isAvailable(String userID, int startWeek, int endWeek) {
        for (int i = startWeek; i <= endWeek; i++) {
            if (getUserActivityCountByWeek(userID, startWeek, i) >= 20) {
                return false;
            }
        }
        return true;
    }

    public List<String> getAvailableUserIDs(int startWeek, int endWeek) {
        List<String> availables = new ArrayList<>();
        for (User user : users) {
            if (isAvailable(user.getID(), startWeek, endWeek)) {
                availables.add(user.getID());
            }
        }
        return availables;
    }

    public List<String> getAvailableUserIDsForActivity(int projectID, String activityName) throws OperationNotAllowedException {
        Activity activity = getActivityByName(activityName, projectID);
        List<String> availables = getAvailableUserIDs(activity.getStartWeek(), activity.getEndWeek());

        for (String id : activity.getAssignedUsers()) {
            availables.remove(id);
        }
        return availables;
    }

    public List<String> getVacantUserIDs(int startWeek, int endWeek) {
        List<String> vacant = new ArrayList<>();
        for (User user : users) {
            if (isVacant(user.getID(), startWeek, endWeek)) {
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
        if (project.getName().length() < 1 || project.getName().length() > 20) {
            throw new OperationNotAllowedException("Project name must be between 1 and 20 characters");
        }
        if (project.getClient().length() < 1 || project.getClient().length() > 20) {
            throw new OperationNotAllowedException("Client name must be between 1 and 20 characters");
        }
        project.setProjectID(createProjectID());
        projects.add(project);
    }

    public Project getProject(String name) throws OperationNotAllowedException {
        for (Project project : projects) {
            if (project.getName().equals(name)) {
                return project;
            }
        }
        throw new OperationNotAllowedException("Project does not exist");
    }

    public Project getProject(int id) throws OperationNotAllowedException {
        for (Project project : projects) {
            if (project.getProjectID() == id) {
                return project;
            }
        }
        throw new OperationNotAllowedException("Project does not exist");
    }

    public String getProjectLeaderID(int projectID) throws OperationNotAllowedException {
        return getProject(projectID).getProjectLeaderID();
    }

    public void assignProjectLeader(int projectID, String assignedUserID) throws OperationNotAllowedException {
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
        if (!isAvailable(userID, activity.getStartWeek(), activity.getEndWeek())) {
            throw new OperationNotAllowedException("User has already 20 Activities in this week");
        }
        activity.assignEmployeeToActivity(userID);
    }

    public void registerTimeForActivity(String userID, int projectID, String activityName, double hours, String dateStr) throws OperationNotAllowedException {
        Activity activity = getActivityByName(activityName, projectID);
        LocalDate date = dateServer.parseDate(dateStr);
        activity.registerTime(userID, hours, date, dateServer);
    }

    public void setStatusOfActivity(String activityName, int projectID, String status) throws OperationNotAllowedException {
        Activity activity = getActivityByName(activityName, projectID);
        Project containingProject = null;
        //  Tænker det her kan erstattes med "containingProject = getProjectByID(projectID)"
        //HERFRA
        for (Project project : projects) {
            if (project.getActivities().contains(activity)) {
                containingProject = project;
                break;
            }
        }
        if (containingProject == null) {
            throw new OperationNotAllowedException("Activity does not belong to any project");
        }

        //HERTIL
        boolean isAssigned      = activity.getAssignedUsers().contains(loggedInUserID);
        boolean isProjectLeader = loggedInUserID.equals(containingProject.getProjectLeaderID());


        // Tænker umiddelbart at der ikke er behov for denne. Alle kan registrere timer til aktiviteten,
        // Så jeg tænker at det enten er alle som kan eller kun Project Leader
        if (!isAssigned && !isProjectLeader) {
            throw new OperationNotAllowedException("Only assigned employees or the project leader can set the status");
        }

        activity.setStatus(status);
        activity.addLog("Status changed to: " + status + " by " + loggedInUserID);
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
}
