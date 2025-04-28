package dtu.example.ui;

import app.Activity;
import app.OperationNotAllowedException;
import app.PM_App;
import app.Project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;


public class PM_App_TextUI implements PropertyChangeListener {
	PM_App app;

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
			showMenu(processStep, out);
			choice = rs.readLine();
			if (choice == null) {
				break;
			}
			int number =  processStep == 0 ? 1: Integer.valueOf(choice);
			if (number == 0) {
				break;
			}
			processChoice(choice, number, out);

		} while (choice != null);
	}

	private void processChoice(String choice, int number, PrintStream out) throws OperationNotAllowedException {

		switch (processStep) {
			case 0:
				app.login(choice);
				processStep ++;
				app.createProject("test", "test");
				app.getProjectByName("test").addActivity("test", 1, 1, 1);
				app.createProject("test1", "test1");
				app.getProjectByName("test1").addActivity("test1", 1, 1, 1);
				break;
		case 1:
			switch (number) {
				case 0:
					app.logout();
					processStep = 0;
					break;
				case 1:
					processStep = 2;
					break;
			}
			break;
		case 2:
			int tmp = 0;
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
					break;
				case 3:
					app.getProjectByName(projectName).setProjectLeader(app.getUserByID(app.getUserID()));
					break;
			}
			break;
		case 4:

			break;
//		case 5:
//			app.selectFruit(Fruit.APPLE);
//			break;
//		case 6:
//			app.cancel();
//			break;
		default:
			break;
		}
	}

	String projectName;

	private void showMenu(int processStep, PrintStream out) throws OperationNotAllowedException {
		int tmp = 0;
		switch (processStep) {
			case 0:

				out.println("Enter UserID: ");
				break;
			case 1:
				out.println("   0) Logout");
				out.println("   1) Select Projects");
				out.println("   2) Create Project");
				out.println("   3) Users");
				out.println("   4) Log Time");
				out.println("   5) Back");
				out.println("Select a number (0-5): ");
				break;
			case 2:
				 tmp = 0;
				out.println("Select Project: ");
				for (Project project : app.getProject()) {
					out.println("   " + tmp + " )  " + project.getName());
					tmp++;
				}
				break;
			case 3:
				out.println("   0) Logout");
				out.println("   1) Select Activity");
				out.println("   2) Create Activity");
				out.println("   3) Assign Project Leader");
				out.println("   4) Back");
				out.println("Select a number (0-4): ");
				break;
			case 4:
				tmp = 0;
				for (Activity activity : app.getProjectByName(projectName).getActivities()) {
					out.println("   " + tmp + " )  " + activity.getName());
					tmp++;
				}
				break;
			case 5:
				out.println(processStep);
				break;
			case 6:
				out.println(processStep);
				break;
			case 7:
				out.println(processStep);
				break;
			case 8:
				out.println(processStep);
				break;
			case 9:
				out.println(processStep);
				break;
			default:
				break;
		}



	}






	@Override
	public void propertyChange(PropertyChangeEvent evt) {


	}
}
