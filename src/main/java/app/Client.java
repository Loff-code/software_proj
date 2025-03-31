package app;

public class Client {
    private String name;
    private String email;
    private Project project;
    private String CVR;

    public Client(String name, String email, Project project, String CVR){
        this.name = name;
        this.email = email;
        this.project = project;
        this.CVR = CVR;

    }



    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public Project getProject(){
        return this.project;
    }
    public String getCVR(){
        return this.CVR;
    }
}
