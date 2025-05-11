package dtu.example.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import app.Activity;
import app.OperationNotAllowedException;
import app.PM_App;
import app.Project;
import app.RealDateServer;
import app.User;

public class PM_App_TextUI implements PropertyChangeListener {

	private final PM_App app;
	private int projectID = 0;
	private String activityName;
	private String loggedInUserID;
	private String lastError = null;
	private BufferedReader reader;
	private List<String> lastVacantUsers = new ArrayList<>();


	private enum ProcessStep {
		LOGIN, MAIN_MENU, SELECT_PROJECT, PROJECT_MENU, SELECT_ACTIVITY, ACTIVITY_MENU,
		EDIT_ACTIVITY, STATUS_REPORT, EDIT_ACTIVITY_NAME, EDIT_ACTIVITY_START,
		EDIT_ACTIVITY_END, EDIT_ACTIVITY_BUDGET_TIME,EDIT_ACTIVITY_STATUS,
		ASSIGN_USER, VIEW_ASSIGNED_USERS, ASSIGN_PROJECT_LEADER,GENERATE_STATUS_REPORT_INPUT, GENERATE_STATUS_REPORT_OUTPUT,
		ADD_ACTIVITY, CREATE_PROJECT, USERS_MENU, LIST_ALL_USERS,
		LIST_VACANT_USERS_INPUT, LIST_VACANT_USERS, CREATE_USER,
		SELECT_ASSIGNED_ACTIVITY, ENTER_LOG_TIME_HOURS, VIEW_USER_ENTRIES_FOR_TODAY,
		 CHOOSE_LOG_DATE_METHOD, ENTER_LOG_TIME_DATE,CREATE_LEAVE_REQUEST_INPUT,
		GENERATE_LEAVE_STATUS_REPORT_INPUT


	}

	private ProcessStep processStep = ProcessStep.LOGIN;
	private double tempLogHours = 0;

	public PM_App_TextUI() {
		this(new PM_App());
	}

	public PM_App_TextUI(PM_App app) {
		this.app = app;
		this.app.addObserver(this);
		this.app.setDateServer(new RealDateServer());
		this.loggedInUserID  = app.getLoggedInUserID();
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


	private void handleGenerateStatusReportInput(String input) throws IOException {
		if (input.equals("0")) {
			processStep = ProcessStep.MAIN_MENU;
			return;
		}
		String[] parts = input.trim().split(" ");
		if (parts.length == 4) {
			try {
				int startYear = Integer.parseInt(parts[0]);
				int startWeek = Integer.parseInt(parts[1]);
				int endYear = Integer.parseInt(parts[2]);
				int endWeek = Integer.parseInt(parts[3]);

				String report = app.getStatusReport(startYear, startWeek, endYear, endWeek);
				System.out.println("\n" + report);
				System.out.println("Press Enter to continue...");
				reader.readLine();
			} catch (NumberFormatException e) {
				lastError = "Invalid input. Please enter four valid numbers.";
			}
		} else {
			lastError = "Invalid input. Format: startYear startWeek endYear endWeek";
		}
		processStep = ProcessStep.MAIN_MENU;
	}



	private void handleChoice(String input, PrintStream out) throws IOException, OperationNotAllowedException {
		int number = -1;
		if (needsNumericInput(processStep)) {
			if (input.isEmpty()) {
				throw new NumberFormatException();
			}
			number = Integer.parseInt(input);
		}

		switch (processStep) {
			case LOGIN -> handleLogin(input, out);
			case MAIN_MENU -> handleMainMenu(number);
			case GENERATE_STATUS_REPORT_INPUT -> handleGenerateStatusReportInput(input);
			case SELECT_PROJECT -> handleSelectProject(number);
			case PROJECT_MENU -> handleProjectMenu(number);
			case SELECT_ACTIVITY -> handleSelectActivity(number);
			case ACTIVITY_MENU -> handleActivityMenu(number);
			case ASSIGN_USER -> handleAssignUser(number);
			case VIEW_ASSIGNED_USERS -> processStep = ProcessStep.ACTIVITY_MENU;
			case ASSIGN_PROJECT_LEADER -> handleAssignProjectLeader(number);
			case ADD_ACTIVITY -> handleAddActivity(input, out);
			case CREATE_PROJECT -> handleCreateProject(input, out);
			case USERS_MENU -> handleUsersMenu(number);
			case LIST_ALL_USERS -> processStep = ProcessStep.USERS_MENU;
			case LIST_VACANT_USERS_INPUT -> handleListVacantUsersInput(input);
			case LIST_VACANT_USERS -> handleListVacantUsers();
			case CREATE_USER -> handleCreateUser(input, out);
			case EDIT_ACTIVITY -> handleEditActivity(number);
			case EDIT_ACTIVITY_NAME -> handleEditActivityName(input);
			case EDIT_ACTIVITY_START -> handleEditActivityStart(input);
			case EDIT_ACTIVITY_END -> handleEditActivityEnd(input);
			case EDIT_ACTIVITY_BUDGET_TIME -> handleEditActivityBudgetTime(input);
			case SELECT_ASSIGNED_ACTIVITY -> handleSelectAssignedActivity(number);
			case ENTER_LOG_TIME_HOURS -> handleLogTimeHours(input);
			case CHOOSE_LOG_DATE_METHOD -> handleChooseLogDateMethod(number);
			case ENTER_LOG_TIME_DATE -> handleLogTimeDate(input);
			case VIEW_USER_ENTRIES_FOR_TODAY -> processStep = ProcessStep.MAIN_MENU;
			case EDIT_ACTIVITY_STATUS -> handleEditActivityStatus(input);
			case CREATE_LEAVE_REQUEST_INPUT -> handleCreateLeaveRequestInput(input);
			case GENERATE_LEAVE_STATUS_REPORT_INPUT -> handleGenerateLeaveStatusReportInput(input);




		}
	}

	private static final EnumSet<ProcessStep> TEXT_STEPS = EnumSet.of(
			ProcessStep.LOGIN, ProcessStep.ADD_ACTIVITY, ProcessStep.CREATE_PROJECT,
			ProcessStep.CREATE_USER, ProcessStep.EDIT_ACTIVITY_NAME, ProcessStep.EDIT_ACTIVITY_START,
			ProcessStep.EDIT_ACTIVITY_END, ProcessStep.EDIT_ACTIVITY_BUDGET_TIME,
			ProcessStep.LIST_VACANT_USERS_INPUT, ProcessStep.ENTER_LOG_TIME_HOURS,
			ProcessStep.ENTER_LOG_TIME_DATE, ProcessStep.GENERATE_STATUS_REPORT_INPUT,
			ProcessStep.EDIT_ACTIVITY_STATUS,
			ProcessStep.CREATE_LEAVE_REQUEST_INPUT,                  // <-- Add this
			ProcessStep.GENERATE_LEAVE_STATUS_REPORT_INPUT           // <-- Add this too
	);


	private boolean needsNumericInput(ProcessStep step) {
		return !TEXT_STEPS.contains(step);
	}

	private void handleLogin(String input, PrintStream out) throws OperationNotAllowedException {
		app.login(input);
		loggedInUserID = app.getLoggedInUserID();
		processStep = ProcessStep.MAIN_MENU;
	}

	private void handleMainMenu(int choice) throws IOException, OperationNotAllowedException {
		switch (choice) {
			case 0 -> logout();
			case 1 -> processStep = ProcessStep.SELECT_PROJECT;
			case 2 -> processStep = ProcessStep.CREATE_PROJECT;
			case 3 -> processStep = ProcessStep.USERS_MENU;
			case 4 -> processStep = ProcessStep.SELECT_ASSIGNED_ACTIVITY;
			case 5 -> processStep = ProcessStep.GENERATE_STATUS_REPORT_INPUT;
			case 6 -> processStep = ProcessStep.VIEW_USER_ENTRIES_FOR_TODAY;
			case 7 -> processStep = ProcessStep.CREATE_LEAVE_REQUEST_INPUT;      // <-- New
			case 8 -> processStep = ProcessStep.GENERATE_LEAVE_STATUS_REPORT_INPUT; // <-- New
			default -> setInvalidChoice();
		}
	}


	private void handleCreateLeaveRequestInput(String input) throws OperationNotAllowedException {
		if (input.equals("0")) {
			processStep = ProcessStep.MAIN_MENU;
			return;
		}

		String[] parts = input.trim().split(" ");
		if (parts.length == 4) {
			try {
				double hours = Double.parseDouble(parts[0]);
				String startDate = parts[1];
				String endDate = parts[2];
				String activityName = parts[3];
				app.createLeaveRequest(loggedInUserID, activityName, 1, hours, startDate, endDate);
				System.out.println("Leave request created successfully.");
			} catch (Exception e) {
				lastError = "Error creating leave request: " + e.getMessage();
			}
		} else {
			lastError = "Invalid input. Format: hours startDate endDate activityName";
		}
		processStep = ProcessStep.MAIN_MENU;
	}

	private void handleGenerateLeaveStatusReportInput(String input) throws IOException {
		if (input.equals("0")) {
			processStep = ProcessStep.MAIN_MENU;
			return;
		}
		String[] parts = input.trim().split(" ");
		if (parts.length == 4) {
			try {
				int startYear = Integer.parseInt(parts[0]);
				int startWeek = Integer.parseInt(parts[1]);
				int endYear = Integer.parseInt(parts[2]);
				int endWeek = Integer.parseInt(parts[3]);

				String report = app.getLeaveStatusReport(startYear, startWeek, endYear, endWeek);
				System.out.println("\n" + report);
				System.out.println("Press Enter to continue...");
				reader.readLine();
			} catch (NumberFormatException e) {
				lastError = "Invalid input. Please enter four valid numbers.";
			}
		} else {
			lastError = "Invalid input. Format: startYear startWeek endYear endWeek";
		}
		processStep = ProcessStep.MAIN_MENU;
	}


	private void handleSelectProject(int choice) {
		if (choice == 0) {
			projectID = 0;
			processStep = ProcessStep.MAIN_MENU;
			return;
		}
		projectID = selectProjectID(app.getProjects(), choice);
		if (projectID != 0) processStep = ProcessStep.PROJECT_MENU;
	}

	private void handleProjectMenu(int choice) {
		switch (choice) {
			case 0 -> {
				projectID = 0;
				processStep = ProcessStep.SELECT_PROJECT;
			}
			case 1 -> processStep = ProcessStep.SELECT_ACTIVITY;
			case 2 -> processStep = ProcessStep.ADD_ACTIVITY;
			case 3 -> processStep = ProcessStep.ASSIGN_PROJECT_LEADER;
			default -> setInvalidChoice();
		}
	}

	private void handleSelectActivity(int choice) throws OperationNotAllowedException {
		if (choice == 0) {
			activityName = null;
			processStep = ProcessStep.PROJECT_MENU;
			return;
		}
		activityName = selectFromList(app.getProject(projectID).getActivities(), choice, Activity::getName);
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
			case 4 -> processStep = ProcessStep.ENTER_LOG_TIME_HOURS;
			case 5 -> {
				projectID = 0;
				activityName = null;
				processStep = ProcessStep.MAIN_MENU;
			}
			default -> setInvalidChoice();
		}
	}

	private void handleAssignUser(int choice) throws OperationNotAllowedException {
		if (choice == 0) {
			processStep = ProcessStep.ACTIVITY_MENU;
			return;
		}

		String userId = selectFromList(app.getAvailableUserIDsForActivity(projectID, activityName), choice, id ->id);
		if (userId != null) {
			app.assignUserToActivity(userId, activityName, projectID);
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
			app.assignProjectLeader(projectID, userId);
		}
		processStep = ProcessStep.PROJECT_MENU;
	}

	private void handleAddActivity(String input, PrintStream out) throws OperationNotAllowedException {
		if (input.equals("0")) {
			processStep = ProcessStep.PROJECT_MENU;
			return;
		}

		String[] parts = input.split(" ");
		if (parts.length == 6) {
			try {
				Activity activity = new Activity(
						parts[0],
						Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3]),
						Integer.parseInt(parts[4]),
						Integer.parseInt(parts[5])
				);
				app.addActivityToProject(projectID, activity);
				out.println("Activity added successfully.");
			} catch (IllegalArgumentException e){
				lastError = e.getMessage();
			}
		} else {
			lastError = "Invalid input. Format: activityName expectedTime startWeek endWeek";
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
			app.createProject(new Project(parts[0], parts[1]));
			out.println("Project created successfully.");
		} else {
			lastError = "Invalid input. Format: projectName clientName";
		}
		processStep = ProcessStep.MAIN_MENU;
	}

	private void handleUsersMenu(int choice) throws OperationNotAllowedException, IOException {
		switch (choice) {
			case 0, 5 -> processStep = ProcessStep.MAIN_MENU;
			case 1 -> processStep = ProcessStep.LIST_ALL_USERS;
			case 2 -> processStep = ProcessStep.LIST_VACANT_USERS_INPUT;
			case 3 -> processStep = ProcessStep.CREATE_USER;
			case 4 -> handleViewTimeEntriesForUser(); // ✅ NEW: Show time entries
			default -> setInvalidChoice();
		}
	}


	private void handleCreateUser(String input, PrintStream out) throws OperationNotAllowedException {
		if (input.equals("0")) {
			processStep = ProcessStep.USERS_MENU;
			return;
		}
		if (input.length() <= 5) {
			app.createUser(new User(input));
			out.println("User created successfully.");
		} else {
			lastError = "Invalid userID. Maximum 4 characters allowed.";
		}
		processStep = ProcessStep.USERS_MENU;
	}

	private void handleEditActivity(int choice) {
		switch (choice) {
			case 0 -> processStep = ProcessStep.ACTIVITY_MENU;
			case 1 -> processStep = ProcessStep.EDIT_ACTIVITY_NAME;
			case 2 -> processStep = ProcessStep.EDIT_ACTIVITY_START;
			case 3 -> processStep = ProcessStep.EDIT_ACTIVITY_END;
			case 4 -> processStep = ProcessStep.EDIT_ACTIVITY_BUDGET_TIME;
			case 5 -> processStep = ProcessStep.EDIT_ACTIVITY_STATUS; // NEW
			default -> setInvalidChoice();
		}
	}
	private void handleEditActivityStatus(String input) throws OperationNotAllowedException {
		if (input.equals("0")) {
			processStep = ProcessStep.EDIT_ACTIVITY;
			return;
		}
		app.setStatusOfActivity(activityName, projectID, input);
		processStep = ProcessStep.EDIT_ACTIVITY;
	}



	private void handleEditActivityName(String input) throws OperationNotAllowedException {
		if (input.equals("0")) {
			processStep = ProcessStep.EDIT_ACTIVITY;
			return;
		}
        app.getActivityByName(activityName, projectID).setName(input);
        activityName = input; // Update current activityName if changed
        processStep = ProcessStep.EDIT_ACTIVITY;
    }

	private void handleEditActivityStart(String input) {
		if (input.equals("0")) {
			processStep = ProcessStep.EDIT_ACTIVITY;
			return;
		}
		try {
			String[] parts = input.split(" ");
			int newYear = Integer.parseInt(parts[0]);
			int newWeek = Integer.parseInt(parts[1]);
			app.getActivityByName(activityName, projectID).setStartYear(newYear);
			app.getActivityByName(activityName, projectID).setStartWeek(newWeek);
			processStep = ProcessStep.EDIT_ACTIVITY;
		} catch (NumberFormatException e) {
			lastError = "Invalid number. Please enter a valid week number.";
			processStep = ProcessStep.ACTIVITY_MENU;
		} catch (OperationNotAllowedException e) {
            throw new RuntimeException(e);
        }
    }

	private void handleEditActivityEnd(String input) {
		if (input.equals("0")) {
			processStep = ProcessStep.EDIT_ACTIVITY;
			return;
		}
		try {
			int end = Integer.parseInt(input);
			app.getActivityByName(activityName, projectID).setEndWeek(end);
			processStep = ProcessStep.EDIT_ACTIVITY;
		} catch (NumberFormatException e) {
			lastError = "Invalid number. Please enter a valid week number.";
			processStep = ProcessStep.ACTIVITY_MENU;
		} catch (OperationNotAllowedException e) {
            throw new RuntimeException(e);
        }
    }

	private void handleEditActivityBudgetTime(String input) {
		if (input.equals("0")) {
			processStep = ProcessStep.EDIT_ACTIVITY;
			return;
		}
		try {
			int time = Integer.parseInt(input);
			app.getActivityByName(activityName, projectID).setBudgetTime(time);
			processStep = ProcessStep.EDIT_ACTIVITY;
		} catch (NumberFormatException | OperationNotAllowedException e) {
			lastError = "Invalid number. Please enter a valid number of hours.";
			processStep = ProcessStep.ACTIVITY_MENU;
		}
    }


	private void handleListVacantUsersInput(String input) {
		if (input.equals("0")) {
			processStep = ProcessStep.USERS_MENU;
			return;
		}

		String[] parts = input.trim().split(" ");
		if (parts.length == 4) {
			try {
				int startYear = Integer.parseInt(parts[0]);
				int startWeek = Integer.parseInt(parts[1]);
				int endYear = Integer.parseInt(parts[2]);
				int endWeek = Integer.parseInt(parts[3]);

				try {
					lastVacantUsers = app.getVacantUserIDs(startYear, startWeek, endYear, endWeek);
					processStep = ProcessStep.LIST_VACANT_USERS;
				} catch (OperationNotAllowedException e) {
					lastError = e.getMessage();
				}
			} catch (NumberFormatException e) {
				lastError = "Invalid input. Please enter four integers: startYear startWeek endYear endWeek.";
			}
		} else {
			lastError = "Invalid input. Format: startYear startWeek endYear endWeek";
		}
	}

	private void handleViewTimeEntriesForUser() throws OperationNotAllowedException, IOException {
		List<String> allEntries = new ArrayList<>();

		for (Project project : app.getProjects()) {
			if (project.getProjectID() == 1) continue; // Skip project with ID 1

			for (Activity activity : project.getActivities()) {
				allEntries.addAll(app.getTimeEntriesForUser(loggedInUserID, activity.getName(), project.getProjectID()));
			}
		}

		System.out.println("\nAll time entries for user \"" + loggedInUserID + "\":\n");

		if (allEntries.isEmpty()) {
			System.out.println(" - No time entries found.");
		} else {
			for (String entry : allEntries) {
				System.out.println(" - " + entry);
			}
		}

		System.out.println("\nPress Enter to continue...");
		reader.readLine();
	}


	private void handleListVacantUsers() {
		processStep = ProcessStep.USERS_MENU;
	}

	private void handleSelectAssignedActivity(int choice) throws OperationNotAllowedException {
		if (choice == 0) {
			processStep = ProcessStep.MAIN_MENU;
			return;
		}
		List<String> list = app.getAssignedActivitiesByUserID(loggedInUserID);
		if (choice < 1 || choice > list.size()) {
			setInvalidChoice();
			return;
		}
		String[] parts = list.get(choice - 1).split(" ");
		if (parts.length < 3) {
			lastError = "Unexpected format from getAssignedActivitiesByUser.";
			processStep = ProcessStep.MAIN_MENU;
			return;
		}
		activityName = parts[0];
		projectID = Integer.parseInt(parts[2]);
		processStep = ProcessStep.ACTIVITY_MENU;
	}

	private void handleLogTimeHours(String input) {
		if (input.equals("0")) {
			processStep = ProcessStep.ACTIVITY_MENU;
			return;
		}
		try {
			tempLogHours = Double.parseDouble(input);
			processStep = ProcessStep.CHOOSE_LOG_DATE_METHOD;
		} catch (NumberFormatException e) {
			lastError = "Invalid number. Enter a valid number of hours.";
		}
	}
	private void handleChooseLogDateMethod(int choice) {
		switch (choice) {
			case 0 -> processStep = ProcessStep.ENTER_LOG_TIME_HOURS;
			case 1 -> {
				String today = app.getDateServer().dateToString(LocalDate.now());
				try {
					app.registerTimeForActivity(loggedInUserID, projectID, activityName, tempLogHours, today);
				} catch (Exception e) {
					lastError = "Failed to log time: " + e.getMessage();
				}
				processStep = ProcessStep.ACTIVITY_MENU;
			}
			case 2 -> processStep = ProcessStep.ENTER_LOG_TIME_DATE;
			default -> setInvalidChoice();
		}
	}



	private void handleLogTimeDate(String input) {
		if (input.equals("0")) {
			processStep = ProcessStep.ACTIVITY_MENU;
			return;
		}
		try {
			app.registerTimeForActivity(loggedInUserID, projectID, activityName, tempLogHours, input);
		} catch (Exception e) {
			lastError = "Failed to log time: " + e.getMessage();
		}
		processStep = ProcessStep.ACTIVITY_MENU;
	}

	private void logout() {
		projectID = 0;
		activityName = null;
		app.logout();
		loggedInUserID = app.getLoggedInUserID();
		processStep = ProcessStep.LOGIN;
	}

	private int selectProjectID(Iterable<Project> projects, int index) {
		if (index == 0) return 0;          // “Back”
		int i = 1;
		for (Project p : projects) {
			if (i++ == index) return p.getProjectID();
		}
		throw new IllegalArgumentException("Invalid selection");
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

		out.println("UserID: " + (loggedInUserID != null ? loggedInUserID : "Not logged in"));
		printHeader(out);

		switch (processStep) {
			case LOGIN -> out.println("Enter UserID:");
			case MAIN_MENU -> printOptions(out,
					"Logout",
					"Select Project",
					"Create Project",
					"Users Menu",
					"My Assigned Activities",
					"Generate Status Report",
					"View Today's Time Entries",
					"Create Leave Request",
					"Generate Leave Status Report"
			);


			case GENERATE_STATUS_REPORT_INPUT ->
					out.println("Enter: startYear startWeek endYear endWeek (or 0 to cancel)");

			case SELECT_PROJECT -> printList(out, app.getProjects(),
					p -> p.getProjectID() + " - " + p.getName() + " (" + p.getClient() + ")",
					"Select a Project:");
			case PROJECT_MENU -> printOptions(out, "Back", "Select Activity", "Add Activity", "Assign Project Leader");
			case SELECT_ACTIVITY -> printList(out, app.getProject(projectID).getActivities(), Activity::getName, "Select an Activity:");
			case ACTIVITY_MENU -> printOptions(out, "Back", "Assign User", "View Assigned Users", "Edit Activity", "Log Time", "Back to Main Menu");
			case ASSIGN_USER -> printList(out, app.getAvailableUserIDsForActivity(projectID, activityName), id -> id, "Select a User:");
			case ASSIGN_PROJECT_LEADER -> printList(out, app.getUsers(), User::getID, "Select a Project Leader:");
			case ADD_ACTIVITY -> out.println("Enter: activityName expectedTime startYear startWeek endYear endWeek (or 0 to cancel)");
			case CREATE_PROJECT -> out.println("Enter: projectName clientName (or 0 to cancel)");
			case USERS_MENU -> printOptions(out,
					"Back",
					"List All Users",
					"List Vacant Users",
					"Create User",
					"View My Registered Time Entries",  // ✅ NEW
					"Main Menu"
			);
			case LIST_ALL_USERS -> printListReadOnly(out, app.getUsers(), User::getID, "All Users:");
			case LIST_VACANT_USERS_INPUT ->
					out.println("Enter: startYear startWeek endYear endWeek (or 0 to cancel)");
			case LIST_VACANT_USERS -> printListReadOnly(out, lastVacantUsers, id -> id, "Vacant Users:");
			case VIEW_ASSIGNED_USERS -> printListReadOnly(out, app.getActivityByName(activityName,projectID).getAssignedUsers(), id -> id, "Assigned Users:");
			case CREATE_USER -> out.println("Enter UserID (max 4 chars) or 0 to cancel:");
			case EDIT_ACTIVITY -> printOptions(out,
					"Back",
					"Edit Name",
					"Edit Start Week",
					"Edit End Week",
					"Edit Budget Time",
					"Edit Status" // NEW
			);
			case EDIT_ACTIVITY_STATUS -> out.println("Enter new status or 0 to cancel:");

			case EDIT_ACTIVITY_NAME, EDIT_ACTIVITY_START, EDIT_ACTIVITY_END, EDIT_ACTIVITY_BUDGET_TIME -> out.println("Enter new value:");
			case ENTER_LOG_TIME_HOURS -> out.println("Enter time to log (hours) or 0 to go back:");
			case CHOOSE_LOG_DATE_METHOD -> printOptions(out, "Back", "Log for Today", "Enter Date Manually");
			case ENTER_LOG_TIME_DATE -> out.println("Enter date (YYYY-MM-DD) or 0 to go back:");
			case SELECT_ASSIGNED_ACTIVITY -> printList(out, app.getAssignedActivitiesByUserID(loggedInUserID), s -> s, "Select an Assigned Activity:");
			case STATUS_REPORT -> {}
			case VIEW_USER_ENTRIES_FOR_TODAY -> printListReadOnly(out, app.getUsersEntriesForToday(loggedInUserID), string -> string, "Time Entries for Today:");
			case CREATE_LEAVE_REQUEST_INPUT -> out.println("Enter: hours startDate(YYYY-MM-DD) endDate(YYYY-MM-DD) activityName (or 0 to cancel)");
			case GENERATE_LEAVE_STATUS_REPORT_INPUT ->
					out.println("Enter: startYear startWeek endYear endWeek (or 0 to cancel)");

		}
	}

	private void printHeader(PrintStream out) throws OperationNotAllowedException {
		if (projectID != 0) {
			Project project = app.getProject(projectID);
			String header = "[Project: " + project.getName();
			String leaderID = app.getProject(projectID).getProjectLeaderID();
			if (leaderID != null) {
				header += " (" + leaderID + ")";
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
		printListReadOnly(out, list, extractor, title);
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
		for (int i = 0; i < 20; i++) {out.println();}
		}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// Event hooks can go here
	}

}
