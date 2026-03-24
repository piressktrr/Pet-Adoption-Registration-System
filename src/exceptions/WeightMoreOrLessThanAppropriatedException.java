package exceptions;

public class WeightMoreOrLessThanAppropriatedException extends RuntimeException {
    public WeightMoreOrLessThanAppropriatedException(String message) {
        super(message);
    }
}
