package man.joseph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Saves test cases to a text file and loads them back.
 * One test case = one line, fields separated by "|".
 */
public class FileStorage {

    private static final String FILE_NAME = "testcases.txt";

    /** Writes all test cases to the file (the old content is replaced). */
    public void save(ArrayList<TestCase> testCases) throws IOException {
        // try-with-resources closes the file automatically, even if an error happens
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (TestCase tc : testCases) {
                writer.println(tc.toFileLine());
            }
        }
    }

    /** Reads the file and rebuilds the test cases. Returns an empty list if there is no file. */
    public ArrayList<TestCase> load() {
        ArrayList<TestCase> testCases = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return testCases; // first run: nothing saved yet, that is fine
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                TestCase tc = parseLine(line);
                if (tc != null) {
                    testCases.add(tc);
                } else {
                    System.out.println("Warning: skipped a damaged line in " + FILE_NAME);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + FILE_NAME + ". Starting with an empty list.");
        }
        return testCases;
    }

    /** Converts one line of the file back into a test case. Returns null if the line is damaged. */
    private TestCase parseLine(String line) {
        // The -1 keeps empty fields (for example an empty description) at the end of the line
        String[] parts = line.split("\\|", -1);
        if (parts.length != 8) {
            return null;
        }

        try {
            String type = parts[0];
            int id = Integer.parseInt(parts[1]);
            Status status = Status.valueOf(parts[6]);

            TestCase tc;
            if (type.equals("Manual")) {
                tc = new ManualTestCase(id, parts[2], parts[3], parts[4], parts[5], parts[7]);
            } else if (type.equals("Automated")) {
                tc = new AutomatedTestCase(id, parts[2], parts[3], parts[4], parts[5], parts[7]);
            } else {
                return null;
            }
            tc.setStatus(status); // restore the saved status
            return tc;

        } catch (IllegalArgumentException | ValidationException e) {
            // NumberFormatException and a bad Status name are both IllegalArgumentExceptions
            return null;
        }
    }
}
