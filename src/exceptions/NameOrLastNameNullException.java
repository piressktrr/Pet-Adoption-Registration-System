package exceptions;

public class NameOrLastNameNullException extends RuntimeException {
    public NameOrLastNameNullException(String message) {
        super(message);
    }
}
