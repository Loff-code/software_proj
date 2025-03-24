public class Employer {
    private String name;
    private String email;
    private String ID;
    private String password;

    public Employer(String name, String email, String ID, String password){
        this.name = name;
        this.email = email;
        this.ID = ID;
        this.password = password;

    }


    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getID(){
        return this.ID;
    }
    public String getPassword(){
        return this.password;
    }
}
