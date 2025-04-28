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
	String userID;

	enum ProcessStep {
		LOGIN, MAIN_MENU, SELECT_PROJECT, PROJECT_MENU, SELECT_ACTIVITY, ACTIVITY_MENU,
		ASSIGN_USER, VIEW_ASSIGNED_USERS, ASSIGN_PROJECT_LEADER,
		CREATE_ACTIVITY, CREATE_PROJECT, USERS_MENU, LIST_ALL_USERS, LIST_VACANT_USERS, CREATE_USER
	}

	ProcessStep processStep = ProcessStep.LOGIN;

	public PM_App_TextUI() {
		this.app = new PM_App();
		this.app.addObserver(this);
	}

	public PM_App_TextUI(PM_App app) {
		this.app = app;
		this.app.addObserver(this);
	}

	public static void main(String[] args) throws Exception {
		new PM_App_TextUI().mainLoop(System.in, System.out);
	}

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
			int number = Arrays.asList(ProcessStep.LOGIN, ProcessStep.CREATE_ACTIVITY, ProcessStep.CREATE_PROJECT, ProcessStep.CREATE_USER).contains(processStep)
					? 1
					: Integer.parseInt(choice);
			processChoice(choice, number, out);

		} while (choice != null);
	}

	private void processChoice(String choice, int number, PrintStream out) throws OperationNotAllowedException {
		int tmp;
		switch (processStep) {
			case LOGIN:
				app.login(choice);
				userID = choice;
				processStep = ProcessStep.MAIN_MENU;
				break;

			case MAIN_MENU:
				switch (number) {
					case 0:
						app.logout();
						processStep = ProcessStep.LOGIN;
						break;
					case 1:
						processStep = ProcessStep.SELECT_PROJECT;
						break;
					case 2:
						processStep = ProcessStep.CREATE_PROJECT;
						break;
					case 3:
						processStep = ProcessStep.USERS_MENU;
						break;
					case 4:
						processStep = ProcessStep.MAIN_MENU;
						break;
				}
				break;

			case SELECT_PROJECT:
				tmp = 0;
				for (Project project : app.getProject()) {
					if (tmp == number) {
						projectName = project.getName();
						break;
					}
					tmp++;
				}
				processStep = ProcessStep.PROJECT_MENU;
				break;

			case PROJECT_MENU:
				switch (number) {
					case 0:
						app.logout();
						processStep = ProcessStep.LOGIN;
						break;
					case 1:
						processStep = ProcessStep.SELECT_ACTIVITY;
						break;
					case 2:
						processStep = ProcessStep.CREATE_ACTIVITY;
						break;
					case 3:
						app.getProjectByName(projectName).setProjectLeader(app.getUserByID(app.getUserID()));
						processStep = ProcessStep.PROJECT_MENU;
						break;
				}
				break;

			case SELECT_ACTIVITY:
				tmp = 0;
				for (Activity activity : app.getProjectByName(projectName).getActivities()) {
					if (tmp == number) {
						activityName = activity.getName();
						break;
					}
					tmp++;
				}
				processStep = ProcessStep.ACTIVITY_MENU;
				break;

			case ACTIVITY_MENU:
				switch (number) {
					case 0:
						app.logout();
						processStep = ProcessStep.LOGIN;
						break;
					case 1:
						processStep = ProcessStep.ASSIGN_USER;
						break;
					case 2:
						processStep = ProcessStep.VIEW_ASSIGNED_USERS;
						break;
					case 3:
						processStep = ProcessStep.PROJECT_MENU;
						break;
				}
				break;

			case ASSIGN_USER:
				tmp = 0;
				for (User user : app.getUsers()) {
					if (tmp == number) {
						app.getProjectByName(projectName).getActivityByName(activityName).assignEmployeeToActivity(user.getID());
						break;
					}
					tmp++;
				}
				processStep = ProcessStep.ACTIVITY_MENU;
				break;

			case VIEW_ASSIGNED_USERS:
				processStep = ProcessStep.ACTIVITY_MENU;
				break;

			case ASSIGN_PROJECT_LEADER:
				tmp = 0;
				for (User user : app.getUsers()) {
					if (tmp == number) {
						// no functionality here currently
						break;
					}
					tmp++;
				}
				processStep = ProcessStep.PROJECT_MENU;
				break;

			case CREATE_ACTIVITY:
				String[] parts = choice.split(" ");
				if (parts.length == 4) {
					app.getProjectByName(projectName).addActivity(
							parts[0],
							Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]),
							Integer.parseInt(parts[3])
					);
				} else {
					out.println("Invalid input for creating activity");
				}
				processStep = ProcessStep.PROJECT_MENU;
				break;

			case CREATE_PROJECT:
				String[] parts0 = choice.split(" ");
				if (parts0.length == 2) {
					app.createProject(parts0[0], parts0[1]);
				} else {
					out.println("Invalid input for creating project");
				}
				processStep = ProcessStep.MAIN_MENU;
				break;

			case USERS_MENU:
				switch (number) {
					case 0:
						app.logout();
						processStep = ProcessStep.LOGIN;
						break;
					case 1:
						processStep = ProcessStep.LIST_ALL_USERS;
						break;
					case 2:
						processStep = ProcessStep.LIST_VACANT_USERS;
						break;
					case 3:
						processStep = ProcessStep.CREATE_USER;
						break;
					case 4:
						processStep = ProcessStep.MAIN_MENU;
						break;
				}
				break;

			case LIST_ALL_USERS:
				processStep = ProcessStep.USERS_MENU;
				break;

			case LIST_VACANT_USERS:
				processStep = ProcessStep.USERS_MENU;
				break;

			case CREATE_USER:
				if (choice.length() <= 4) {
					app.createUser(choice);
				} else {
					out.println("Invalid userID (max 4 characters)");
				}
				processStep = ProcessStep.USERS_MENU;
				break;
		}
	}

	private void showMenu(ProcessStep step, PrintStream out) throws OperationNotAllowedException {
		int tmp;
		switch (step) {
			case LOGIN:
				out.println("Enter UserID: ");
				break;
			case MAIN_MENU:
				out.println("UserID: " + userID);
				out.println("   0) Logout");
				out.println("   1) Select Projects");
				out.println("   2) Create Project");
				out.println("   3) Users");
				out.println("   4) Log Time");
				out.println("Select a number (0-4): ");
				break;
			case SELECT_PROJECT:
				tmp = 0;
				out.println("UserID: " + userID);
				out.println("Select Project:");
				for (Project project : app.getProject()) {
					out.println("   " + tmp + ") " + project.getName());
					tmp++;
				}
				break;
			case PROJECT_MENU:
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("   0) Logout");
				out.println("   1) Select Activity");
				out.println("   2) Create Activity");
				out.println("   3) Assign Project Leader");
				out.println("Select a number (0-3): ");
				break;
			case SELECT_ACTIVITY:
				tmp = 0;
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("Select Activity:");
				for (Activity activity : app.getProjectByName(projectName).getActivities()) {
					out.println("   " + tmp + ") " + activity.getName());
					tmp++;
				}
				break;
			case ACTIVITY_MENU:
				out.println("UserID: " + userID);
				out.println("Project: " + projectName);
				out.println("Activity: " + activityName);
				out.println("   0) Logout");
				out.println("   1) Assign User");
				out.println("   2) View Assigned Users");
				out.println("   3) Back");
				out.println("Select a number (0-3): ");
				break;
			case ASSIGN_USER:
				tmp = 0;
				out.println("UserID: " + userID);
				out.println("Select User:");
				for (User user : app.getUsers()) {
					out.println("   " + tmp + ") " + user.getID());
					tmp++;
				}
				break;
			case VIEW_ASSIGNED_USERS:
				tmp = 0;
				out.println("UserID: " + userID);
				for (String assigned : app.getProjectByName(projectName).getActivityByName(activityName).getAssignedUsers()) {
					out.println("   " + tmp + ") " + assigned);
					tmp++;
				}
				break;
			case CREATE_ACTIVITY:
				out.println("Enter activity name, start week, end week and expected time: ");
				out.println("Example: activityName 1 2 3");
				break;
			case CREATE_PROJECT:
				out.println("Enter project name and client: ");
				out.println("Example: projectName clientName");
				break;
			case USERS_MENU:
				out.println("UserID: " + userID);
				out.println("   0) Logout");
				out.println("   1) List All Users");
				out.println("   2) List Vacant Users");
				out.println("   3) Create User");
				out.println("   4) Back");
				out.println("Select a number (0-4): ");
				break;
			case LIST_ALL_USERS:
				tmp = 0;
				out.println("List of all users:");
				for (User user : app.getUsers()) {
					out.println("   " + tmp + ") " + user.getID());
					tmp++;
				}
				break;
			case LIST_VACANT_USERS:
				tmp = 0;
				out.println("Vacant users:");
				for (String uid : app.getProjectByName(projectName).getActivityByName(activityName).getAssignedUsers()) {
					out.println("   " + tmp + ") " + uid);
					tmp++;
				}
				break;
			case CREATE_USER:
				out.println("Enter userID (max 4 characters): ");
				break;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// Nothing here for now
	}
}
