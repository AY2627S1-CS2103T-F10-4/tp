package seedu.address.commons.exceptions;

/**
 * Signals that some given data does not fulfill some constraints.
 */
public class IllegalValueException extends Exception {
    /**
     * Constructs an {@code IllegalValueException} with the specified detail {@code message}.
     *
     * @param message should contain relevant information on the failed constraint(s).
     */
    public IllegalValueException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code IllegalValueException} with the specified detail {@code message} and {@code cause}.
     *
     * @param message should contain relevant information on the failed constraint(s).
     * @param cause of the main exception.
     */
    public IllegalValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
