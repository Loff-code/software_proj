package app;

public class ProjectInfo {
    private String projectName;
    private String client;
    private int projectID;

    public ProjectInfo(String projectName, String client) {
        this.projectName = projectName;

        this.client = client;
    }

    public String getProjectName() { return projectName; }
    public String getClient() { return client; }
    public int getProjectID() { return projectID; }
}

