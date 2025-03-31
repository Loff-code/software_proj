package app;

public class ClientHelper {
    private Client client;
    public Client getClient(){
        return exampleClient();
    }
    public Client exampleClient(){
        this.client = new Client("mike", "mike@world.com", "123123");
        return client;
    }
}
