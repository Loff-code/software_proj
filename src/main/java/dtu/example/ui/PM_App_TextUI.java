package dtu.example.ui;

import app.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.Arrays;

public class PM_App_TextUI implements PropertyChangeListener {
	private final PM_App app;
	private String projectName;
	private String activityName;
	private String userID;

	private enum ProcessStep {
		LOGIN, MAIN_MENU, SELECT_PROJECT, PROJECT_MENU, SELECT_ACTIVITY, ACTIVITY_MENU,
		ASSIGN_USER, VIEW_ASSIGNED_USERS, ASSIGN_PROJECT_LEADER,
		CREATE_ACTIVITY, CREATE_PROJECT, USERS_MENU, LIST_ALL_USERS, LIST_VACANT_USERS, CREATE_USER
	}

	private ProcessStep processStep = ProcessStep.LOGIN;

	public PM_App_TextUI() {
		this(new PM_App());
	}

	public PM_App_TextUI(PM_App app) {
		this.app = app;
		this.app.addObserver(this);
	}

	public static void main(String[] args) throws Exception {
		new PM_App_TextUI().mainLoop(System.in, System.out);
	}

	public void mainLoop(InputStream in, PrintStream out) throws IOException, OperationNotAllowedException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String choice;

		while (true) {
			clearScreen(out);
			showMenu(out);

			choice = reader.readLine();
			if (choice == null) break;

			try {
				handleChoice(choice.trim(), out);
			} catch (NumberFormatException e) {
				out.println("Invalid number input, please try again.");
			} catch (OperationNotAllowedException e) {
				out.println("Operation not allowed: " + e.getMessage());
			}
		}
	}

	private void handleChoice(String input, PrintStream out) throws OperationNotAllowedException {
		int number = needsNumericInput(processStep) ? Integer.parseInt(input) : -1;

		switch (processStep) {
			case LOGIN -> handleLogin(input, out);
			case MAIN_MENU -> handleMainMenu(number);
			case SELECT_PROJECT -> handleSelectProject(number);
			case PROJECT_MENU -> handleProjectMenu(number);
			case SELECT_ACTIVITY -> handleSelectActivity(number);
			case ACTIVITY_MENU -> handleActivityMenu(number);
			case ASSIGN_USER -> handleAssignUser(number);
			case VIEW_ASSIGNED_USERS -> processStep = ProcessStep.ACTIVITY_MENU;
			case ASSIGN_PROJECT_LEADER -> processStep = ProcessStep.PROJECT_MENU; // Placeholder
			case CREATE_ACTIVITY -> handleCreateActivity(input, out);
			case CREATE_PROJECT -> handleCreateProject(input, out);
			case USERS_MENU -> handleUsersMenu(number);
			case LIST_ALL_USERS, LIST_VACANT_USERS -> processStep = ProcessStep.USERS_MENU;
			case CREATE_USER -> handleCreateUser(input, out);
		}
	}

	private boolean needsNumericInput(ProcessStep step) {
		return !Arrays.asList(ProcessStep.LOGIN, ProcessStep.CREATE_ACTIVITY, ProcessStep.CREATE_PROJECT, ProcessStep.CREATE_USER)
				.contains(step);
	}

	private void handleLogin(String input, PrintStream out) throws OperationNotAllowedException {
		app.login(input);
		this.userID = input;
		out.println("Login successful.");
		processStep = ProcessStep.MAIN_MENU;
	}

	private void handleMainMenu(int choice) {
		switch (choice) {
			case 0 -> logout();
			case 1 -> processStep = ProcessStep.SELECT_PROJECT;
			case 2 -> processStep = ProcessStep.CREATE_PROJECT;
			case 3 -> processStep = ProcessStep.USERS_MENU;
			case 4 -> processStep = ProcessStep.MAIN_MENU;
			default -> invalidChoice();
		}
	}

	private void handleSelectProject(int choice) {
		projectName = selectFromList(app.getProject(), choice, Project::getName);
		processStep = ProcessStep.PROJECT_MENU;
	}

	private void handleProjectMenu(int choice) {
		switch (choice) {
			case 0 -> logout();
			case 1 -> processStep = ProcessStep.SELECT_ACTIVITY;
			case 2 -> processStep = ProcessStep.CREATE_ACTIVITY;
			case 3 -> {
				app.getProjectByName(projectName).setProjectLeader(app.getUserByID(userID));
				processStep = ProcessStep.PROJECT_MENU;
			}
			default -> invalidChoice();
		}
	}

	private void handleSelectActivity(int choice) {
		activityName = selectFromList(app.getProjectByName(projectName).getActivities(), choice, Activity::getName);
		processStep = ProcessStep.ACTIVITY_MENU;
	}

	private void handleActivityMenu(int choice) {
		switch (choice) {
			case 0 -> logout();
			case 1 -> processStep = ProcessStep.ASSIGN_USER;
			case 2 -> processStep = ProcessStep.VIEW_ASSIGNED_USERS;
			case 3 -> processStep = ProcessStep.PROJECT_MENU;
			default -> invalidChoice();
		}
	}

	private void handleAssignUser(int choice) {
		String userId = selectFromList(app.getUsers(), choice, User::getID);
		app.getProjectByName(projectName).getActivityByName(activityName).assignEmployeeToActivity(userId);
		processStep = ProcessStep.ACTIVITY_MENU;
	}

	private void handleCreateActivity(String input, PrintStream out) {
		String[] parts = input.split(" ");
		if (parts.length == 4) {
			try {
				app.getProjectByName(projectName).addActivity(
						parts[0],
						Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3])
				);
				out.println("Activity created successfully.");
			} catch (NumberFormatException e) {
				out.println("Invalid week or time format.");
			}
		} else {
			out.println("Invalid input. Format: name startWeek endWeek expectedTime");
		}
		processStep = ProcessStep.PROJECT_MENU;
	}

	private void handleCreateProject(String input, PrintStream out) throws OperationNotAllowedException {
		String[] parts = input.split(" ");
		if (parts.length == 2) {
			app.createProject(parts[0], parts[1]);
			out.println("Project created successfully.");
		} else {
			out.println("Invalid input. Format: projectName clientName");
		}
		processStep = ProcessStep.MAIN_MENU;
	}

	private void handleUsersMenu(int choice) {
		switch (choice) {
			case 0 -> logout();
			case 1 -> processStep = ProcessStep.LIST_ALL_USERS;
			case 2 -> processStep = ProcessStep.LIST_VACANT_USERS;
			case 3 -> processStep = ProcessStep.CREATE_USER;
			case 4 -> processStep = ProcessStep.MAIN_MENU;
			default -> invalidChoice();
		}
	}

	private void handleCreateUser(String input, PrintStream out) throws OperationNotAllowedException {
		if (input.length() <= 4) {
			app.createUser(input);
			out.println("User created successfully.");
		} else {
			out.println("Invalid userID. Maximum 4 characters allowed.");
		}
		processStep = ProcessStep.USERS_MENU;
	}

	private void logout() {
		userID = null;
		app.logout();
		processStep = ProcessStep.LOGIN;
	}

	private <T> String selectFromList(Iterable<T> list, int index, NameExtractor<T> extractor) {
		int i = 0;
		for (T item : list) {
			if (i++ == index) {
				return extractor.getName(item);
			}
		}
		throw new IllegalArgumentException("Invalid selection");
	}

	private interface NameExtractor<T> {
		String getName(T item);
	}

	private void showMenu(PrintStream out) throws OperationNotAllowedException {
		out.println("UserID: " + (userID != null ? userID : "Not logged in"));
		printHeader(out); // ADD THIS LINE

		switch (processStep) {
			case LOGIN -> out.println("Enter UserID:");
			case MAIN_MENU -> printOptions(out, "Logout", "Select Project", "Create Project", "Users Menu", "Log Time");
			case SELECT_PROJECT -> printList(out, app.getProject(), Project::getName, "Select a Project:");
			case PROJECT_MENU -> printOptions(out, "Logout", "Select Activity", "Create Activity", "Assign Project Leader");
			case SELECT_ACTIVITY -> printList(out, app.getProjectByName(projectName).getActivities(), Activity::getName, "Select an Activity:");
			case ACTIVITY_MENU -> printOptions(out, "Logout", "Assign User", "View Assigned Users", "Back to Project Menu");
			case ASSIGN_USER -> printList(out, app.getUsers(), User::getID, "Select a User:");
			case VIEW_ASSIGNED_USERS -> printList(out, app.getProjectByName(projectName).getActivityByName(activityName).getAssignedUsers(), id -> id, "Assigned Users:");
			case CREATE_ACTIVITY -> out.println("Enter: activityName startWeek endWeek expectedTime");
			case CREATE_PROJECT -> out.println("Enter: projectName clientName");
			case USERS_MENU -> printOptions(out, "Logout", "List All Users", "List Vacant Users", "Create User", "Back");
			case LIST_ALL_USERS -> printList(out, app.getUsers(), User::getID, "All Users:");
			case LIST_VACANT_USERS -> printList(out, app.getUsers(), User::getID, "Vacant Users:");
			case CREATE_USER -> out.println("Enter UserID (max 4 chars):");
		}
	}

	private void printHeader(PrintStream out) {
		if (projectName != null) {
			Project project = app.getProjectByName(projectName);
			String header = "[Project: " + project.getName() + "]";

			if (activityName != null) {
				Activity activity = project.getActivityByName(activityName);
				header += " -> [Activity: " + activity.getName()
						+ " (start: " + activity.getStartWeek()
						+ ", end: " + activity.getEndWeek() + ")]";

			}
			out.println(header);
		}
	}


	private void printOptions(PrintStream out, String... options) {
		for (int i = 0; i < options.length; i++) {
			out.println("   " + i + ") " + options[i]);
		}
		out.println("Select a number:");
	}

	private <T> void printList(PrintStream out, Iterable<T> list, NameExtractor<T> extractor, String title) {
		out.println(title);
		int index = 0;
		for (T item : list) {
			out.println("   " + index++ + ") " + extractor.getName(item));
		}
	}

	private void invalidChoice() {
		System.out.println("Invalid choice. Please try again.");
	}

	private void clearScreen(PrintStream out) {
		for (int i = 0; i < 20; i++) out.println();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// Future event handling could go here
	}
}
