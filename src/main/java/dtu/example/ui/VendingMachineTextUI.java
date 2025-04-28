package dtu.example.ui;


/*
public class VendingMachineTextUI implements PropertyChangeListener {
	VendingMachine vendingMachine;

	public VendingMachineTextUI(int bananas, int apples) {
		vendingMachine = new VendingMachine(bananas, apples);
		vendingMachine.addObserver(this);
	}

	public VendingMachineTextUI(VendingMachine m) {
		vendingMachine = m;
		vendingMachine.addObserver(this);
	}

	public static void main(String[] args) throws Exception {
		int bananas = 5;
		int apples = 5;
		if (args.length == 2) {
			bananas = Integer.valueOf(args[0]);
			apples = Integer.valueOf(args[1]);
		}
		new VendingMachineTextUI(bananas, apples).mainLoop(System.in, System.out);
	}

	public void mainLoop(InputStream in, PrintStream out) throws IOException {
		BufferedReader rs = new BufferedReader(new InputStreamReader(in));
		String choice = null;
		do {
			showMenu(out);
			choice = rs.readLine();
			if (choice == null) {
				break;
			}
			int number = Integer.valueOf(choice);
			if (number == 0) {
				break;
			}
			processChoice(number, out);

		} while (choice != null);
	}

	private void processChoice(int number, PrintStream out) {
		switch (number) {
		case 1:
			vendingMachine.input(1);
			break;
		case 2:
			vendingMachine.input(2);
			break;
		case 3:
			vendingMachine.input(5);
			break;
		case 4:
			vendingMachine.selectFruit(Fruit.BANANA);
			break;
		case 5:
			vendingMachine.selectFruit(Fruit.APPLE);
			break;
		case 6:
			vendingMachine.cancel();
			break;
		default:
			break;
		}
	}

	private void showMenu(PrintStream out) {
		out.println("   0) Exit");
		out.println("   1) Input 1 DKK");
		out.println("   2) Input 2 DKK");
		out.println("   3) Input 5 DKK");
		out.println("   4) Select banana");
		out.println("   5) Select apple");
		out.println("   6) Cancel");
		out.println("Select a number (0-6): ");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String type = evt.getPropertyName();
		switch (type) {
		case NotificationType.DISPENSED_ITEM:
			if (vendingMachine.getDispensedItem() == Fruit.APPLE) {
				System.out.println("Dispensing: Apple");
			}
			if (vendingMachine.getDispensedItem() == Fruit.BANANA) {
				System.out.println("Dispensing: Banana");
			}
			break;
		case NotificationType.REST:
			if (vendingMachine.getRest() != 0) {
				System.out.println("Rest: DKK " + vendingMachine.getRest());
			}
			break;
		case NotificationType.CURRENT_MONEY:
			System.out.println("Current Money: DKK " + vendingMachine.getCurrentMoney());
			break;
		default:
			break;
		}

	}
}
*/