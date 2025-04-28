package dtu.example.ui;

import app.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;


public class PM_App_TextUI implements PropertyChangeListener {
	PM_App app;

	String projectName;
	String activityName;

	public PM_App_TextUI() {
		this.app = new PM_App();
		this.app.addObserver(this);

	}

	public PM_App_TextUI(PM_App app) {
		this.app = app;
		this.app.addObserver(this);
	}

	public static void main(String[] args) throws Exception {
//		int bananas = 5;
//		int apples = 5;
//		if (args.length == 2) {
//			bananas = Integer.valueOf(args[0]);
//			apples = Integer.valueOf(args[1]);
//		}
		new PM_App_TextUI().mainLoop(System.in, System.out);
	}

	int processStep = 0;
	int stringThing[] = {0,2,4};
	public void mainLoop(InputStream in, PrintStream out) throws IOException, OperationNotAllowedException {
		BufferedReader rs = new BufferedReader(new InputStreamReader(in));
		String choice = null;
		do {
			for (int i = 0; i < 50; i++) {
				out.println();
			}
			showMenu(processStep, out);
			choice = rs.readLine();
			if (choice == null) {
				break;
			}
			int number =  Arrays.asList(0, 9, 10, 14).contains(processStep) ? 1: Integer.valueOf(choice);
			if (number == 0) {
				break;
			}
			processChoice(choice, number, out);

		} while (choice != null);
	}
	String userID;
	private void processChoice(String choice, int number, PrintStream out) throws OperationNotAllowedException {
		int tmp = 0;
		switch (processStep) {
			case 0:
				//LOGIN
				app.login(choice);
				userID = choice;
				processStep ++;
				app.createProject("test", "test");
				app.getProjectByName("test").addActivity("test", 1, 1, 1);
				app.createProject("test1", "test1");
				app.getProjectByName("test1").addActivity("test1", 1, 1, 1);
				break;
		case 1:
			//MAIN MENU
			switch (number) {
				case 0:
					app.logout();
					processStep = 0;
					break;
				case 1:
					processStep = 2;
					break;
				case 2:
					processStep = 10;
					break;
				case 3:
					processStep = 11;
					break;
				case 4:
					processStep = 1;
					break;
			}
			break;
		case 2:
			// SELECT PROJECT
			tmp = 0;
			for (Project project : app.getProject()) {
				if (tmp == number) {
					projectName = project.getName();
					break;
				}
				tmp++;
			}
			processStep = 3;
			break;
		case 3:
			// PROJECT MENU
			switch (number) {
				case 0:
					app.logout();
					processStep = 0;
					break;
				case 1:
					processStep = 4;
					break;
				case 2:
					app.getProjectByName(projectName).addActivity("test", 1, 1, 1);
					processStep = 9;
					break;
				case 3:
					app.getProjectByName(projectName).setProjectLeader(app.getUserByID(app.getUserID()));
					processStep = 8;
					break;
			}
			break;
		case 4:
			// SELECT ACTIVITY
			tmp = 0;
			for (Activity activity : app.getProjectByName(projectName).getActivities()) {
				if (tmp == number) {
					activityName = activity.getName();
					break;
				}
				tmp++;
			}
			processStep = 5;
			break;
		case 5:
			// ACTIVITY MENU
			switch (number) {
				case 0:
					app.logout();
					processStep = 0;
					break;
				case 1:
					processStep = 6;
					break;
				case 2:
					processStep = 7;
					break;
				case 3:
					processStep = 3;
					break;
			}
			break;
			case 6:
				// ASSIGN USER
				tmp = 0;
				for (User user : app.getUsers()) {
					if (tmp == number) {
						app.getProjectByName(projectName).getActivityByName(activityName).assignEmployeeToActivity(user.getID());
						break;
					}
					tmp++;
				}
				processStep = 5;
				break;
			case 7:
				// GET ASSIGNED USERS
				processStep = 5;
				break;

			case 8:
				tmp = 0;
				for (User user : app.getUsers()) {
					if (tmp == number) {
//						app.getProjectByName(projectName).getActivityByName(activityName).assignProjectLeader();
						break;
					}
					tmp++;
				}
				processStep = 5;
				break;
			case 9:
				// CREATE ACTIVITY
				String[] parts = choice.split(" ");
				if (parts.length == 4) {
					app.getProjectByName(projectName).addActivity(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));

				} else {
					out.println("Invalid input");
				}
				processStep = 3;
				break;
			case 10:
			// CREATE PROJECT
			String[] parts0 = choice.split(" ");
			if (parts0.length == 2) {
				app.createProject(parts0[0], parts0[1]);
			} else {
				out.println("Invalid input");
			}
				processStep = 1;
			break;
			case 11:
				// USER MENU
				switch (number) {
					case 0:
						app.logout();
						processStep = 0;
						break;
					case 1:
						processStep = 12;
						break;
					case 2:
						processStep = 13;
						break;
					case 3:
						processStep = 14;
						break;
					case 4:
						processStep = 1;
						break;
				}
				break;
				case 12:
					// LIST ALL USERS
					processStep = 11;
					break;
				case 13:
					// LIST VACANT USERS
					processStep = 11;
					break;
				case 14:
					// CREATE USER
					if (choice.length() < 5) {
						app.createUser(choice);
						processStep = 11;
					} else {
						out.println("Invalid input");
					}
					break;
		default:
			break;
		}
	}


	private void showMenu(int processStep, PrintStream out) throws OperationNotAllowedException {
		int tmp = 0;
		switch (processStep) {
			case 0:
				// LOGIN
				out.println("Enter UserID: ");
				break;
			case 1:
				// MAIN MENU
				out.println("UserID: " + userID);
				out.println("   0) Logout");
				out.println("   1) Select Projects");
				out.println("   2) Create Project");
				out.println("   3) Users");
				out.println("   4) Log Time");
				out.println("   5) Back");
				out.println("Select a number (0-5): ");
				break;
			case 2:
				// SELECT PROJECT
				 tmp = 0;
				out.println("UserID: " + userID);
				out.println("Select Project: ");
				for (Project project : app.getProject()) {
					out.println("   " + tmp + " )  " + project.getName());
					tmp++;
				}
				break;
			case 3:
				// PROJECT
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("   0) Logout");
				out.println("   1) Select Activity");
				out.println("   2) Create Activity");
				out.println("   3) Assign Project Leader");
				out.println("   4) Back");
				out.println("Select a number (0-4): ");
				break;
			case 4:
				// SELECT ACTIVITY
				tmp = 0;
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("Select Activity: ");
				for (Activity activity : app.getProjectByName(projectName).getActivities()) {
					out.println("   " + tmp + " )  " + activity.getName());
					tmp++;
				}
				break;
			case 5:
				// ACTIVITY
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("Activity: " + activityName);
				out.println(app.getProjectByName(projectName).getActivityByName(activityName).getStartWeek() + " - " + app.getProjectByName(projectName).getActivityByName(activityName).getEndWeek());
				out.println("   0) Logout");
				out.println("   1) Assign User");
				out.println("   2) View Assigned Users");
				out.println("   3) Back");
				out.println("Select a number (0-3): ");
				break;
			case 6:
				// ASSIGN USER
				tmp = 0;
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("Activity: " + activityName);
				out.println(app.getProjectByName(projectName).getActivityByName(activityName).getStartWeek() + " - " + app.getProjectByName(projectName).getActivityByName(activityName).getEndWeek());
				out.println("Select User: ");
				for (User user : app.getUsers()) {
					out.println("   " + tmp + " )  " + user.getID());
					tmp++;
				}
				break;
			case 7:
				// GET ASSIGNED USERS
				tmp = 0;
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("Activity: " + activityName);
				out.println(app.getProjectByName(projectName).getActivityByName(activityName).getStartWeek() + " - " + app.getProjectByName(projectName).getActivityByName(activityName).getEndWeek());
				for (String userID : app.getProjectByName(projectName).getActivityByName(activityName).getAssignedUsers()) {
					out.println("   " + tmp + " )  " + userID);
					tmp++;
				}
				break;
			case 8:
				// ASSIGN PROJECT LEADER
				tmp = 0;
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("Select User To Be Project Leader: ");
				for (String userID : app.getProjectByName(projectName).getActivityByName(activityName).getAssignedUsers()) {
					out.println("   " + tmp + " )  " + userID);
					tmp++;
				}
				break;
			case 9:
				//CREATE ACTIVITY
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("Write name, start week, end week and expected time: ");
				out.println("Example: \"activity\" 1 2 3");
				break;
			case 10:
				//CREATE PROJECT
				out.println("UserID: " + userID);
				out.println("Write project name and client: ");
				out.println("Example: \"project\" \"client\" ");
				break;
			case 11:
				//USERS
				out.println("UserID: " + userID);
				out.println("   0) Logout");
				out.println("   1) List All Users");
				out.println("   2) List Vacant Users");
				out.println("   3) Create User");
				out.println("   4) Back");
				out.println("Select a number (0-4): ");
				break;
			case 12:
				//LIST ALL USERS
				tmp=0;
				out.println("UserID: " + userID);;
				out.println("List Of All Users: ");
				for (User user : app.getUsers()) {
					out.println("   " + tmp + " )  " + user.getID());
					tmp++;
				}
				break;
			case 13:
				//LIST VACANT USERS
				tmp=0;
				out.println("UserID: " + userID);;
				out.println("List Of Vacant Users: ");
				for (String userID : app.getProjectByName(projectName).getActivityByName(activityName).getAssignedUsers()) {
					out.println("   " + tmp + " )  " + userID);
					tmp++;
				}
				break;
			case 14:
				// CREATE USER
				out.println("UserID: " + userID);;
				out.println("Write userID (max 4 characters): ");
				break;
			default:
				break;
		}



	}






	@Override
	public void propertyChange(PropertyChangeEvent evt) {


	}
}
