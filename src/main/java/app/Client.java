package app;

public class Client {
    private String name;
    private String email;
    private String CVR;

    public Client(String name, String email, String CVR){
        this.name = name;
        this.email = email;
        this.CVR = CVR;

    }



    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getCVR(){
        return this.CVR;
    }
}
