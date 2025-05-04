package app;

import java.util.List;

public class ProjectLeaderManager {
    private List<String> userIDs;

    public ProjectLeaderManager(List<String> userIDs ) {
        this.userIDs = userIDs;
    }
    private String projectLeaderID;

    public void setProjectLeader(String selectedUser, String currentUser) throws OperationNotAllowedException{
        if (currentUser.equals(projectLeaderID) || projectLeaderID == null){
            for (String user1 : this.userIDs ) {
                if (user1.equals(selectedUser)){
                    this.projectLeaderID = selectedUser;
                }
            }
        }
        else {
            throw new OperationNotAllowedException("ProjectLeader access needed");
        }
    }

    public String getLeaderID() {
        return projectLeaderID ;
    }
}
