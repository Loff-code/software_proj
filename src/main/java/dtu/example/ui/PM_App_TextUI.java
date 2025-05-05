// ... your imports here ...
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
	private String lastError = null;
	private BufferedReader reader;

	private enum ProcessStep {
		LOGIN, MAIN_MENU, SELECT_PROJECT, PROJECT_MENU, SELECT_ACTIVITY, ACTIVITY_MENU,
		LOG_TIME, EDIT_ACTIVITY, STATUS_REPORT, EDIT_ACTIVITY_NAME, EDIT_ACTIVITY_START, EDIT_ACTIVITY_END, EDIT_ACTIVITY_BUDGET_TIME,
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
		reader = new BufferedReader(new InputStreamReader(in));
		String choice;

		while (true) {
			clearScreen(out);
			showMenu(out);

			choice = reader.readLine();
			if (choice == null) break;

			lastError = null;

			try {
				handleChoice(choice.trim(), out);
			} catch (NumberFormatException e) {
				lastError = "Invalid number input. Please enter a valid number.";
			} catch (OperationNotAllowedException e) {
				lastError = "Operation not allowed: " + e.getMessage();
			} catch (IllegalArgumentException e) {
				lastError = "Invalid selection. Please choose a number from the list.";
			}
		}
	}

	private void handleChoice(String input, PrintStream out) throws IOException, OperationNotAllowedException {
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
			case ASSIGN_PROJECT_LEADER -> handleAssignProjectLeader(number);
			case CREATE_ACTIVITY -> handleCreateActivity(input, out);
			case CREATE_PROJECT -> handleCreateProject(input, out);
			case USERS_MENU -> handleUsersMenu(number);
			case LIST_ALL_USERS, LIST_VACANT_USERS -> processStep = ProcessStep.USERS_MENU;
			case CREATE_USER -> handleCreateUser(input, out);
			case EDIT_ACTIVITY -> handleEditActivity(number);
			case EDIT_ACTIVITY_NAME -> {
				processStep = ProcessStep.EDIT_ACTIVITY;
			}
			case EDIT_ACTIVITY_START -> {
				processStep = ProcessStep.EDIT_ACTIVITY;
			}
			case EDIT_ACTIVITY_END -> {
				processStep = ProcessStep.EDIT_ACTIVITY;
			}
			case EDIT_ACTIVITY_BUDGET_TIME -> {
				processStep = ProcessStep.EDIT_ACTIVITY;
			}
			case LOG_TIME -> {
				processStep = ProcessStep.ACTIVITY_MENU;
			}
			case STATUS_REPORT -> {
				out.println("Status for Activity: " + activityName);
				Activity a = app.getProjectByName(projectName).getActivityByName(activityName);
				out.println("Press enter to continue...");
				reader.readLine();
				processStep = ProcessStep.ACTIVITY_MENU;
			}
		}
	}

	private void handleEditActivity(int choice) {
		switch (choice) {
			case 0 -> processStep = ProcessStep.ACTIVITY_MENU;
			case 1 -> processStep = ProcessStep.EDIT_ACTIVITY_NAME;
			case 2 -> processStep = ProcessStep.EDIT_ACTIVITY_START;
			case 3 -> processStep = ProcessStep.EDIT_ACTIVITY_END;
			case 4 -> processStep = ProcessStep.EDIT_ACTIVITY_BUDGET_TIME;
			default -> setInvalidChoice();
		}
	}

	private boolean needsNumericInput(ProcessStep step) {
		return !Arrays.asList(
				ProcessStep.LOGIN, ProcessStep.CREATE_ACTIVITY,
				ProcessStep.CREATE_PROJECT, ProcessStep.CREATE_USER,
				ProcessStep.EDIT_ACTIVITY_NAME, ProcessStep.EDIT_ACTIVITY_START,
				ProcessStep.EDIT_ACTIVITY_END, ProcessStep.EDIT_ACTIVITY_BUDGET_TIME,
				ProcessStep.LOG_TIME
		).contains(step);
	}

	private void handleLogin(String input, PrintStream out) throws OperationNotAllowedException {
		app.login(input);
		this.userID = input;
		processStep = ProcessStep.MAIN_MENU;
	}

	private void handleMainMenu(int choice) {
		switch (choice) {
			case 0 -> logout();
			case 1 -> processStep = ProcessStep.SELECT_PROJECT;
			case 2 -> processStep = ProcessStep.CREATE_PROJECT;
			case 3 -> processStep = ProcessStep.USERS_MENU;
			case 4 -> processStep = ProcessStep.LOG_TIME;
			default -> setInvalidChoice();
		}
	}

	private void handleSelectProject(int choice) {
		if (choice == 0) {
			projectName = null;
			processStep = ProcessStep.MAIN_MENU;
			return;
		}
		projectName = selectFromList(app.getProject(), choice, Project::getName);
		if (projectName != null) processStep = ProcessStep.PROJECT_MENU;
	}

	// ✅ FIXED: enter ASSIGN_PROJECT_LEADER step instead of assigning immediately
	private void handleProjectMenu(int choice) {
		switch (choice) {
			case 0 -> {
				projectName = null;
				processStep = ProcessStep.SELECT_PROJECT;
			}
			case 1 -> processStep = ProcessStep.SELECT_ACTIVITY;
			case 2 -> processStep = ProcessStep.CREATE_ACTIVITY;
			case 3 -> processStep = ProcessStep.ASSIGN_PROJECT_LEADER; // <- now enters correct flow
			default -> setInvalidChoice();
		}
	}

	private void handleSelectActivity(int choice) {
		if (choice == 0) {
			activityName = null;
			processStep = ProcessStep.PROJECT_MENU;
			return;
		}
		activityName = selectFromList(app.getProjectByName(projectName).getActivities(), choice, Activity::getName);
		if (activityName != null) processStep = ProcessStep.ACTIVITY_MENU;
	}

	private void handleActivityMenu(int choice) {
		switch (choice) {
			case 0 -> {
				activityName = null;
				processStep = ProcessStep.PROJECT_MENU;
			}
			case 1 -> processStep = ProcessStep.ASSIGN_USER;
			case 2 -> processStep = ProcessStep.VIEW_ASSIGNED_USERS;
			case 3 -> processStep = ProcessStep.EDIT_ACTIVITY;
			case 4 -> processStep = ProcessStep.LOG_TIME;
			case 5 -> processStep = ProcessStep.STATUS_REPORT;
			case 6 -> processStep = ProcessStep.PROJECT_MENU;
			default -> setInvalidChoice();
		}
	}

	private void handleAssignUser(int choice) {
		if (choice == 0) {
			processStep = ProcessStep.ACTIVITY_MENU;
			return;
		}
		String userId = selectFromList(app.getUsers(), choice, User::getID);
		if (userId != null) {
			app.getProjectByName(projectName).getActivityByName(activityName).assignEmployeeToActivity(userId);
		}
		processStep = ProcessStep.ACTIVITY_MENU;
	}

	private void handleAssignProjectLeader(int choice) throws OperationNotAllowedException {
		if (choice == 0) {
			processStep = ProcessStep.PROJECT_MENU;
			return;
		}
		String userId = selectFromList(app.getUsers(), choice, User::getID);
		if (userId != null) {
			try {
				app.assignProjectLeader(projectName, userId);
			} catch (OperationNotAllowedException e) {
				lastError = "Operation not allowed: " + e.getMessage();
			}
		}
		processStep = ProcessStep.PROJECT_MENU;
	}

	private void handleCreateActivity(String input, PrintStream out) throws OperationNotAllowedException {
		if (input.equals("0")) {
			processStep = ProcessStep.PROJECT_MENU;
			return;
		}
		String[] parts = input.split(" ");
		if (parts.length == 4) {
			try {
				app.getProjectByName(projectName).addActivity(new Activity(
						parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
				out.println("Activity created successfully.");
			} catch (NumberFormatException e) {
				lastError = "Invalid week or time format.";
			}
		} else {
			lastError = "Invalid input. Format: name startWeek endWeek expectedTime";
		}
		processStep = ProcessStep.PROJECT_MENU;
	}

	private void handleCreateProject(String input, PrintStream out) throws OperationNotAllowedException {
		if (input.equals("0")) {
			processStep = ProcessStep.MAIN_MENU;
			return;
		}
		String[] parts = input.split(" ");
		if (parts.length == 2) {
			app.createProject(parts[0], parts[1]);
			out.println("Project created successfully.");
		} else {
			lastError = "Invalid input. Format: projectName clientName";
		}
		processStep = ProcessStep.MAIN_MENU;
	}

	private void handleUsersMenu(int choice) {
		switch (choice) {
			case 0, 4 -> processStep = ProcessStep.MAIN_MENU;
			case 1 -> processStep = ProcessStep.LIST_ALL_USERS;
			case 2 -> processStep = ProcessStep.LIST_VACANT_USERS;
			case 3 -> processStep = ProcessStep.CREATE_USER;
			default -> setInvalidChoice();
		}
	}

	private void handleCreateUser(String input, PrintStream out) throws OperationNotAllowedException {
		if (input.equals("0")) {
			processStep = ProcessStep.USERS_MENU;
			return;
		}
		if (input.length() <= 5) {
			app.createUser(input);
			out.println("User created successfully.");
		} else {
			lastError = "Invalid userID. Maximum 4 characters allowed.";
		}
		processStep = ProcessStep.USERS_MENU;
	}

	private void logout() {
		userID = null;
		projectName = null;
		activityName = null;
		app.logout();
		processStep = ProcessStep.LOGIN;
	}

	private <T> String selectFromList(Iterable<T> list, int index, NameExtractor<T> extractor) {
		if (index == 0) return null;
		int i = 1;
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
		if (lastError != null) {
			out.println("ERROR: " + lastError + "\n");
		}

		out.println("UserID: " + (userID != null ? userID : "Not logged in"));
		printHeader(out);

		switch (processStep) {
			case LOGIN -> out.println("Enter UserID:");
			case MAIN_MENU -> printOptions(out, "Logout", "Select Project", "Create Project", "Users Menu", "Log Time");
			case SELECT_PROJECT -> printList(out, app.getProject(), Project::getName, "Select a Project:");
			case PROJECT_MENU -> printOptions(out, "Back", "Select Activity", "Create Activity", "Assign Project Leader");
			case SELECT_ACTIVITY -> printList(out, app.getProjectByName(projectName).getActivities(), Activity::getName, "Select an Activity:");
			case ACTIVITY_MENU -> printOptions(out, "Back", "Assign User", "View Assigned Users", "Edit Activity", "Log Time", "Status Report", "Back to Project Menu");
			case ASSIGN_USER -> printList(out, app.getUsers(), User::getID, "Select a User:");
			case ASSIGN_PROJECT_LEADER -> printList(out, app.getUsers(), User::getID, "Select a Project Leader:");
			case CREATE_ACTIVITY -> out.println("Enter: activityName startWeek endWeek expectedTime (or 0 to cancel)");
			case CREATE_PROJECT -> out.println("Enter: projectName clientName (or 0 to cancel)");
			case USERS_MENU -> printOptions(out, "Back", "List All Users", "List Vacant Users", "Create User", "Main Menu");
			case LIST_ALL_USERS -> printListReadOnly(out, app.getUsers(), User::getID, "All Users:");
			case LIST_VACANT_USERS -> printListReadOnly(out, app.getUsers(), User::getID, "Vacant Users:");
			case VIEW_ASSIGNED_USERS -> printListReadOnly(out, app.getProjectByName(projectName).getActivityByName(activityName).getAssignedUsers(), id -> id, "Assigned Users:");
			case CREATE_USER -> out.println("Enter UserID (max 4 chars) or 0 to cancel:");
			case EDIT_ACTIVITY -> printOptions(out, "Back", "Edit Name", "Edit Start Week", "Edit End Week", "Edit Budget Time");
			case EDIT_ACTIVITY_NAME, EDIT_ACTIVITY_START, EDIT_ACTIVITY_END, EDIT_ACTIVITY_BUDGET_TIME -> out.println("Enter new value:");
			case LOG_TIME -> out.println("Enter time to log (hours):");
			case STATUS_REPORT -> {}
		}
	}

	private void printHeader(PrintStream out) {
		if (projectName != null) {
			Project project = app.getProjectByName(projectName);
			String header = "[Project: " + project.getName();
			User leader = project.getProjectLeader();
			if (leader != null) {
				header += " (" + leader.getID() + ")";
			}
			header += "]";

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
		out.println("   0) Back");
		int index = 1;
		for (T item : list) {
			out.println("   " + index++ + ") " + extractor.getName(item));
		}
		out.println("Select a number:");
	}

	private <T> void printListReadOnly(PrintStream out, Iterable<T> list, NameExtractor<T> extractor, String title) {
		out.println("   0) Back\n");
		out.println(title);
		int index = 1;
		for (T item : list) {
			out.println("   " + index++ + ". " + extractor.getName(item));
		}
	}

	private void setInvalidChoice() {
		lastError = "Invalid choice. Please try again.";
	}

	private void clearScreen(PrintStream out) {
		for (int i = 0; i < 20; i++) out.println();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// Event hooks can go here
	}
}
