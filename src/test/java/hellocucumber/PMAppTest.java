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

//    @Test
//    void getAvailableUserIDs_returnsOnlyAvailableUsers() throws Exception {
//        PM_App app = new PM_App();
//
//        // Opret brugere
//        app.createUser(new User("ali"));
//
//        app.login("huba"); // Login på én af dem
//
//
//        app.createProject(new Project("test1", "client"));
//
//        // Opret projekt
//        int projectID = app.createProjectID();
//
//        //  bruger 20 aktiviteter i uge 6
//        for (int i = 0; i < 20; i++) {
//            String act = "A" + i;
//            app.addActivityToProject(projectID, new Activity(act, 10, 2025, 6, 2025, 6));
//            app.assignUserToActivity("huba", act, projectID);
//        }
//
//        // Kald metoden
//        List<String> result = app.getAvailableUserIDs(2025, 6, 2025, 6);
//
//        // Assertions
//        assertTrue(result.contains("ali"));
//        assertFalse(result.contains("huba"));
//    }




    @Test
    void getVacantUserIDs_throwsException_whenWeekIsOutOfBounds() {
        PM_App app = new PM_App();
        Exception exception = assertThrows(OperationNotAllowedException.class, () ->
                app.getVacantUserIDs(2025, 0, 2025, 6) // startWeek = 0
        );
        assertEquals("Weeks must be between 1 and 52", exception.getMessage());
    }

    @Test
    void getVacantUserIDs_throwsException_whenEndBeforeStart() {
        PM_App app = new PM_App();
        Exception exception = assertThrows(OperationNotAllowedException.class, () ->
                app.getVacantUserIDs(2025, 10, 2025, 5) // slut før start
        );
        assertEquals("End week cannot be before start week", exception.getMessage());
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
            app.generateReport("999999"); // projekt eksisterer ikke
        });

        assertEquals("Project does not exist", exception.getMessage());
    }

//
//    @Test
//    void getAssignedActivitiesByUserID_returnsCorrectList() throws Exception {
//        PM_App app = new PM_App();
//        String userID = "ali";
//        app.createUser(new User(userID));
//        app.login(userID);
//        app.createProject(new Project("P1", "client"));
//
//
//        String projectID = app.createProjectID();
//        app.createProject(new Project(projectID, "P1", "client"));
//
//        // Tilføj aktivitet og assign user
//        app.addActivityToProject("999999", 10, 5, 6);
//        app.assignUserToActivity(userID, "Act1", "P1");
//
//        List<String> assigned = app.getAssignedActivitiesByUserID(userID);
//
//        assertEquals(1, assigned.size());
//        assertTrue(assigned.get(0).contains("Act1"));
//        assertTrue(assigned.get(0).contains("P1"));
//    }






}
