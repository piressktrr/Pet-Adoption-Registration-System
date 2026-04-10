package Exceptions;

public class NameAndLastNameInvalidFormat extends RuntimeException {
    public NameAndLastNameInvalidFormat(String message) {
        super(message);
    }
}
