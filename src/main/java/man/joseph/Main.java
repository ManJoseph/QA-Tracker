package man.joseph;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point. Shows the menu, reads the user's input and calls the other classes.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static TestCaseManager manager = new TestCaseManager();
    private static FileStorage storage = new FileStorage();
    private static ReportGenerator report = new ReportGenerator();

    public static void main(String[] args) {
        // Load saved test cases (empty list if there is no file yet)
        for (TestCase tc : storage.load()) {
            manager.add(tc);
        }

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readNumber();

            switch (choice) {
                case 1:
                    addTestCase();
                    break;
                case 2:
                    viewAll();
                    break;
                case 3:
                    updateStatus();
                    break;
                case 4:
                    searchTestCases();
                    break;
                case 5:
                    report.print(manager);
                    break;
                case 6:
                    running = saveAndExit();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 6.");
            }
        }
    }

    // ---------- Menu ----------

    private static void showMenu() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("        QA TRACKER v1.0");
        System.out.println("==============================");
        System.out.println("1. Add Test Case");
        System.out.println("2. View All Test Cases");
        System.out.println("3. Update Test Status");
        System.out.println("4. Search Test Cases");
        System.out.println("5. Generate Report");
        System.out.println("6. Save & Exit");
        System.out.println("==============================");
        System.out.print("Enter your choice: ");
    }

    // ---------- Feature 1: Add ----------

    private static void addTestCase() {
        System.out.print("Title (required): ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Preconditions: ");
        String preconditions = scanner.nextLine();
        System.out.print("Expected Result (required): ");
        String expected = scanner.nextLine();

        String type = readType(); // keeps asking until Manual or Automated
        int id = manager.getNextId();

        try {
            TestCase testCase;
            if (type.equals("Manual")) {
                System.out.print("Tester name: ");
                String tester = scanner.nextLine();
                testCase = new ManualTestCase(id, title, description, preconditions, expected, tester);
            } else {
                System.out.print("Script path: ");
                String script = scanner.nextLine();
                testCase = new AutomatedTestCase(id, title, description, preconditions, expected, script);
            }
            manager.add(testCase);
            System.out.println("Test case added with ID " + id + ".");

        } catch (ValidationException e) {
            // The test case was NOT created, we just show why
            System.out.println("Error: " + e.getMessage());
        }
    }

    /** Asks for the type until the user types Manual or Automated (any capitalisation). */
    private static String readType() {
        while (true) {
            System.out.print("Type (Manual / Automated): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Manual")) {
                return "Manual";
            }
            if (input.equalsIgnoreCase("Automated")) {
                return "Automated";
            }
            System.out.println("Error: type must be Manual or Automated.");
        }
    }

    // ---------- Feature 2: View ----------

    private static void viewAll() {
        if (manager.isEmpty()) {
            System.out.println("No test cases found.");
            return;
        }
        printTable(manager.getAll());
    }

    /** Prints a list of test cases as a table (used by View and Search). */
    private static void printTable(ArrayList<TestCase> list) {
        String format = "%-4s %-25s %-10s %-10s %s%n";
        System.out.printf(format, "ID", "TITLE", "TYPE", "STATUS", "TESTER / SCRIPT");
        System.out.println("-------------------------------------------------------------------------");

        for (TestCase tc : list) {
            System.out.printf(format, tc.getId(), shorten(tc.getTitle(), 25),
                    tc.getType(), tc.getStatus(), tc.getExtraInfo());
        }
    }

    /** Cuts long text so the table columns stay aligned. */
    private static String shorten(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    // ---------- Feature 3: Update status ----------

    private static void updateStatus() {
        System.out.print("Enter test case ID: ");
        int id = readNumber();

        TestCase testCase = manager.findById(id);
        if (testCase == null) {
            System.out.println("Error: no test case with ID " + id + ".");
            return;
        }

        System.out.println("Current status: " + testCase.getStatus());
        System.out.println("Select new status:");
        System.out.println("1. PASS");
        System.out.println("2. FAIL");
        System.out.println("3. BLOCKED");
        System.out.println("4. NOT_RUN");

        Status newStatus = null;
        while (newStatus == null) {
            System.out.print("Your choice: ");
            int choice = readNumber();
            switch (choice) {
                case 1: newStatus = Status.PASS; break;
                case 2: newStatus = Status.FAIL; break;
                case 3: newStatus = Status.BLOCKED; break;
                case 4: newStatus = Status.NOT_RUN; break;
                default: System.out.println("Error: please choose 1, 2, 3 or 4.");
            }
        }

        testCase.setStatus(newStatus);
        System.out.println("Status updated to " + newStatus + ".");
    }

    // ---------- Feature 4: Search ----------

    private static void searchTestCases() {
        System.out.print("Enter keyword (leave empty to show all): ");
        String keyword = scanner.nextLine();

        ArrayList<TestCase> results = manager.search(keyword);
        if (results.isEmpty()) {
            System.out.println("No test cases match \"" + keyword.trim() + "\".");
            return;
        }
        printTable(results);
    }

    // ---------- Feature 6: Save & Exit ----------

    /** Returns false (stop the program) if saving worked, true (keep running) if saving failed. */
    private static boolean saveAndExit() {
        try {
            storage.save(manager.getAll());
            System.out.println("Test cases saved. Goodbye!");
            return false;
        } catch (IOException e) {
            System.out.println("Error: could not save the file (" + e.getMessage() + ").");
            System.out.println("Your data was not saved. Please try again.");
            return true;
        }
    }

    // ---------- Helper ----------

    /** Reads a whole number. Returns -1 if the user typed something that is not a number. */
    private static int readNumber() {
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}