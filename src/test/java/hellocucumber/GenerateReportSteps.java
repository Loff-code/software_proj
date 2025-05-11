package hellocucumber;

import app.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import static org.junit.jupiter.api.Assertions.*;

public class GenerateReportSteps {

    private final PM_App pmApp;
    private final ErrorMessageHolder errorMessageHolder;
    private Project project; // Vi gemmer projektet her

    // Constructor til at injectere appen og error holder
    public GenerateReportSteps(PM_App pmApp, ErrorMessageHolder errorMessageHolder) {
        this.pmApp = pmApp;
        this.errorMessageHolder = errorMessageHolder;
    }

    // Step 1: Opret et projekt med ID 25000 og navn "P1"
    @Given("that a project with ID {int} and project name {string} exists")
    public void projectWithIDAndNameExists(int projectID, String projectName) throws OperationNotAllowedException {
        // Vi opretter et projekt og tilføjer det til appen
        Project newProject = new Project(projectName, "Client");
        pmApp.createProject(newProject); // Opretter projektet i systemet
        newProject.setProjectID(projectID); // Sætter projektets ID
        project = pmApp.getProject(projectID); // Henter projektet fra systemet
        assertNotNull(project); // Tjekker om projektet er blevet oprettet korrekt
    }

}
