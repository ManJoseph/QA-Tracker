package man.joseph;

import java.util.ArrayList;

/**
 * Keeps the list of test cases in memory and answers questions about it
 * (find by ID, search, count by status). It does NOT print anything or read input.
 */
public class TestCaseManager {

    private ArrayList<TestCase> testCases = new ArrayList<>();
    private int nextId = 1;

    /** The ID the next new test case should get. */
    public int getNextId() {
        return nextId;
    }

    /** Adds a test case. Keeps nextId always higher than any existing ID. */
    public void add(TestCase testCase) {
        testCases.add(testCase);
        if (testCase.getId() >= nextId) {
            nextId = testCase.getId() + 1;
        }
    }

    public ArrayList<TestCase> getAll() {
        return testCases;
    }

    public boolean isEmpty() {
        return testCases.isEmpty();
    }

    /** Returns the test case with this ID, or null if it does not exist. */
    public TestCase findById(int id) {
        for (TestCase tc : testCases) {
            if (tc.getId() == id) {
                return tc;
            }
        }
        return null;
    }

    /**
     * Searches title and description, ignoring capital letters.
     * An empty keyword returns every test case.
     */
    public ArrayList<TestCase> search(String keyword) {
        ArrayList<TestCase> results = new ArrayList<>();
        String key = keyword.trim().toLowerCase();

        for (TestCase tc : testCases) {
            boolean inTitle = tc.getTitle().toLowerCase().contains(key);
            boolean inDescription = tc.getDescription().toLowerCase().contains(key);
            // contains("") is always true, so an empty keyword matches everything
            if (inTitle || inDescription) {
                results.add(tc);
            }
        }
        return results;
    }

    public int countByStatus(Status status) {
        int count = 0;
        for (TestCase tc : testCases) {
            if (tc.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    public int countByType(String type) {
        int count = 0;
        for (TestCase tc : testCases) {
            if (tc.getType().equals(type)) {
                count++;
            }
        }
        return count;
    }
}
