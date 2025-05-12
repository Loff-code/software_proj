package hellocucumber;

import app.*;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PMAppTest {

    @Test
    void testAddObserver() {
        PM_App app = new PM_App();
        PropertyChangeListener dummyListener = evt -> {};
        app.addObserver(dummyListener);
    }

    @Test
    void getAvailableUserIDs_returnsOnlyAvailableUsers() throws Exception {
        PM_App app = new PM_App();

        app.login("huba");
        app.createUser(new User("ali"));

        int projectID = app.createProjectID();
        Project p = new Project("test1", "Client");
        p.setProjectID(projectID);
        app.createProject(p);

        //  20 activities in week 6
        for (int i = 0; i < 20; i++) {
            String act = "A" + i;
            app.addActivityToProject(projectID, new Activity(act, 10, 2025, 6, 2025, 6));
            app.assignUserToActivity("huba", act, projectID);
        }

        List<String> result = app.getAvailableUserIDs(2025, 6, 2025, 6);

        assertTrue(result.contains("ali"));
        assertFalse(result.contains("huba"));
    }




    @Test
    void getVacantUserIDs_throwsException_whenWeekIsOutOfBounds() {
        PM_App app = new PM_App();
        Exception exception = assertThrows(OperationNotAllowedException.class, () ->
                app.getVacantUserIDs(2025, 0, 2025, 6)
        );
        assertEquals("Weeks must be between 1 and 52", exception.getMessage());
    }

    @Test
    void getVacantUserIDs_throwsException_whenEndBeforeStart() {
        PM_App app = new PM_App();
        Exception exception = assertThrows(OperationNotAllowedException.class, () ->
                app.getVacantUserIDs(2025, 10, 2025, 5)
        );
        assertEquals("End time cannot be before start time", exception.getMessage());
    }




    @Test
    void isAvailable_handlesWeekRolloverCorrectly() throws Exception {
        PM_App app = new PM_App();

        app.login("huba");
        app.createProject(new Project("test1", "client"));


        assertTrue(app.isAvailable("huba", 2024, 52, 2025, 1));
    }




    @Test
    void generateReport_throwsExceptionIfProjectNotFound() {
        PM_App app = new PM_App();

        Exception exception = assertThrows(OperationNotAllowedException.class, () -> {
            app.generateReport("999999");
        });

        assertEquals("Project does not exist", exception.getMessage());
    }


    @Test
    void getAvailableUserIDsForActivity_returnsOnlyTrulyAvailableUsers() throws Exception {
        PM_App app = new PM_App();

        // huba already exist in the project so we just log him in
        app.login("huba");

        // creation of project and activity
        int projectID = app.createProjectID();
        Project p = new Project("proj", "client");
        p.setProjectID(projectID);
        app.createProject(p);

        app.addActivityToProject(projectID, new Activity("Act1", 10, 2025, 6, 2025, 6));

        app.createUser(new User("ali"));


        app.assignUserToActivity("ali", "Act1", projectID);

        List<String> available = app.getAvailableUserIDsForActivity(projectID, "Act1");

        assertTrue(available.contains("huba"));
        assertFalse(available.contains("ali"));
    }

    @Test
    void getAssignedActivitiesByUserID_returnsCorrectList() throws Exception {
        PM_App app = new PM_App();

        app.login("huba");

        int projectID = app.createProjectID();
        Project project = new Project("P1", "client");
        project.setProjectID(projectID);
        app.createProject(project);

        app.addActivityToProject(projectID, new Activity("Act1", 10, 2025, 6, 2025, 6));
        app.assignUserToActivity("huba", "Act1", projectID);

        List<String> assigned = app.getAssignedActivitiesByUserID("huba");

        assertEquals(1, assigned.size());
        assertTrue(assigned.get(0).contains("Act1"));
        assertTrue(assigned.get(0).contains("P1"));
        assertTrue(assigned.get(0).contains("" + projectID));
    }

    @Test
    void generateReport_returnsFormattedReport() throws Exception {
        PM_App app = new PM_App();
        app.login("huba");

        int projectID = app.createProjectID();
        Project project = new Project("TestProject", "client");
        project.setProjectID(projectID);
        app.createProject(project);

        app.addActivityToProject(projectID, new Activity("Act1", 10, 2025, 6, 2025, 6));
        app.addActivityToProject(projectID, new Activity("Act2", 5, 2025, 7, 2025, 7));

        String report = app.generateReport("" + projectID);

        assertTrue(report.contains("Report for project: TestProject"));
        assertTrue(report.contains("Activity: Act1"));
        assertTrue(report.contains("Used Hours: 10"));
        assertTrue(report.contains("Activity: Act2"));
        assertTrue(report.contains("Used Hours: 5"));
    }

    @Test
    void generateReport_throwsExceptionWhenProjectNotFound() throws OperationNotAllowedException {
        PM_App app = new PM_App();
        app.login("huba");

        int invalidProjectID = 9999;

        Exception exception = assertThrows(OperationNotAllowedException.class, () -> {
            app.generateReport("" + invalidProjectID);
        });

        assertEquals("Project does not exist", exception.getMessage());

    }



    @Test
    void createLeaveRequest_createsLeaveActivityCorrectly() throws Exception {
        PM_App app = new PM_App();
        app.login("huba");

        int projectID = app.createProjectID();
        Project project = new Project("TestProject", "client");
        project.setProjectID(projectID);
        app.createProject(project);

        String userID = "huba";
        String activityName = "Vacation";
        double hours = 8.0;
        String dateStart = "2025-07-01";
        String dateEnd = "2025-07-01";

        app.createLeaveRequest(userID, activityName, projectID, hours, dateStart, dateEnd);

        List<Activity> activities = app.getProject(projectID).getActivities();
        assertEquals(1, activities.size());

        Activity leaveActivity = activities.get(0);
        assertTrue(leaveActivity.getName().contains("Vacation"));
        assertTrue(leaveActivity.getName().contains(userID));
        assertTrue(leaveActivity.getName().contains("2025-07-01"));

        double registered = leaveActivity.getTimeMap().get("[" + userID + "]|" + leaveActivity.getName() + "|" + dateStart);
        assertEquals(8.0, registered);
    }


//    @Test
//    void getLeaveStatusReport_includesAllRelevantDetails() throws Exception {
//        PM_App app = new PM_App();
//        app.login("huba");
//
//        int projectID = app.createProjectID();
//        Project project = new Project("TestProject", "client");
//        project.setProjectID(projectID);
//        app.createProject(project);
//
//        Activity activity = new Activity("Leave", 10, 2025, 6, 2025, 6); // uge 6 i 2025
//        app.addActivityToProject(projectID, activity);
//        app.assignUserToActivity("huba", "Leave", projectID);
//        app.registerTimeForActivity("huba", projectID, "Leave", 6.0, "2025-02-06");
//
//        String report = app.getLeaveStatusReport(2025, 6, 2025, 6);
//
//        assertTrue(report.contains("Project: " + projectID + " - TestProject"));
//        assertTrue(report.contains("Activity: Leave"));
//        assertTrue(report.contains("Status:"));
//        assertTrue(report.contains("Budgeted Hours: 10"));
//        assertTrue(report.contains("Used Hours: 6.0"));
//        assertTrue(report.contains("Assigned Users: huba"));
//        assertTrue(report.contains("Users who have logged time: huba"));
//    }














}
