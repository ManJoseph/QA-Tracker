package man.joseph;

/**
 * Our own exception, thrown when the user gives data that breaks a rule
 * (for example an empty title). The message is shown directly to the user.
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
