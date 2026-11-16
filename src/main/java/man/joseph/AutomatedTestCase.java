package man.joseph;

/**
 * A test case that runs through an automation script.
 * Extra field: the path of the script.
 */
public class AutomatedTestCase extends TestCase {

    private String scriptPath;

    public AutomatedTestCase(int id, String title, String description, String preconditions,
                             String expectedResult, String scriptPath) throws ValidationException {
        super(id, title, description, preconditions, expectedResult);

        if (scriptPath == null || scriptPath.trim().isEmpty()) {
            throw new ValidationException("Script path cannot be empty for an Automated test case.");
        }
        this.scriptPath = clean(scriptPath);
    }

    @Override
    public String getType() {
        return "Automated";
    }

    @Override
    public String getExtraInfo() {
        return scriptPath;
    }
}
