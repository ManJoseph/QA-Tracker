package man.joseph;

/**
 * A test case that a human tester executes by hand.
 * Extra field: the name of the tester.
 */
public class ManualTestCase extends TestCase {

    private String testerName;

    public ManualTestCase(int id, String title, String description, String preconditions,
                          String expectedResult, String testerName) throws ValidationException {
        super(id, title, description, preconditions, expectedResult);

        if (testerName == null || testerName.trim().isEmpty()) {
            throw new ValidationException("Tester name cannot be empty for a Manual test case.");
        }
        this.testerName = clean(testerName);
    }

    @Override
    public String getType() {
        return "Manual";
    }

    @Override
    public String getExtraInfo() {
        return testerName;
    }
}