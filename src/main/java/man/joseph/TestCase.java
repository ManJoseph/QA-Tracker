package man.joseph;


/**
 * Parent class for every test case.
 * It holds everything that Manual and Automated test cases have in common.
 * It is abstract, so you cannot write "new TestCase(...)".
 * You must create a ManualTestCase or an AutomatedTestCase instead.
 */
public abstract class TestCase {

    private int id;
    private String title;
    private String description;
    private String preconditions;
    private String expectedResult;
    private Status status;

    public TestCase(int id, String title, String description,
                    String preconditions, String expectedResult) throws ValidationException {

        // Validation rules: title and expected result are required
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Title cannot be empty.");
        }
        if (expectedResult == null || expectedResult.trim().isEmpty()) {
            throw new ValidationException("Expected result cannot be empty.");
        }

        this.id = id;
        this.title = clean(title);
        this.description = clean(description);
        this.preconditions = clean(preconditions);
        this.expectedResult = clean(expectedResult);
        this.status = Status.NOT_RUN; // default status for a new test case
    }

    /**
     * Removes extra spaces and replaces the "|" character.
     * We use "|" to separate fields in the save file, so it must not appear inside the text.
     */
    protected static String clean(String text) {
        if (text == null) {
            return "";
        }
        return text.trim().replace("|", "/");
    }

    // Each child class must say what its type is and what its extra field is
    public abstract String getType();       // "Manual" or "Automated"
    public abstract String getExtraInfo();  // tester name OR script path

    /**
     * Turns the test case into one line of text for the save file.
     * Format: TYPE|ID|TITLE|DESCRIPTION|PRECONDITIONS|EXPECTED|STATUS|EXTRA
     */
    public String toFileLine() {
        return getType() + "|" + id + "|" + title + "|" + description + "|"
                + preconditions + "|" + expectedResult + "|" + status + "|" + getExtraInfo();
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPreconditions() { return preconditions; }
    public String getExpectedResult() { return expectedResult; }
    public Status getStatus() { return status; }

    // Only the status can change after creation
    public void setStatus(Status status) {
        this.status = status;
    }
}