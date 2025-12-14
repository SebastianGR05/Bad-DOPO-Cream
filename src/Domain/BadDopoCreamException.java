package Domain;

/**
 * Custom exception class for Bad Dopo-Cream game errors.
 */
public class BadDopoCreamException extends Exception {
    
    /**
     * Creates a new exception with a descriptive error message.
     * @param message a description of what went wrong
     */
    public BadDopoCreamException(String message) {
        super(message);
    }
    
    /**
     * Creates a new exception with a message and the original cause.
     * @param message a description of what went wrong
     * @param cause the original exception that caused this error
     */
    public BadDopoCreamException(String message, Throwable cause) {
        super(message, cause);
    }
}