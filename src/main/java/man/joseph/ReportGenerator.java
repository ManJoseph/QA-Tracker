package man.joseph;

/**
 * Prints the test execution summary report.
 */
public class ReportGenerator {

    public void print(TestCaseManager manager) {
        int total = manager.getAll().size();
        int pass = manager.countByStatus(Status.PASS);

        // Avoid dividing by zero when there are no test cases
        double passRate = 0.0;
        if (total > 0) {
            passRate = (pass * 100.0) / total;
        }

        System.out.println("================================");
        System.out.println("      TEST EXECUTION REPORT");
        System.out.println("================================");
        System.out.printf("Total Test Cases : %2d%n", total);
        System.out.printf("  - Manual       : %2d%n", manager.countByType("Manual"));
        System.out.printf("  - Automated    : %2d%n", manager.countByType("Automated"));
        System.out.println("--------------------------------");
        System.out.printf("PASS             : %2d%n", pass);
        System.out.printf("FAIL             : %2d%n", manager.countByStatus(Status.FAIL));
        System.out.printf("BLOCKED          : %2d%n", manager.countByStatus(Status.BLOCKED));
        System.out.printf("NOT RUN          : %2d%n", manager.countByStatus(Status.NOT_RUN));
        System.out.println("--------------------------------");
        System.out.printf("Pass Rate        : %.1f%%%n", passRate);
        System.out.println("================================");
    }
}
