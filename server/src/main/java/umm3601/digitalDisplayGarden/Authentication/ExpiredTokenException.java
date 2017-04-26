package umm3601.digitalDisplayGarden.Authentication;

/**
 * This exception should be thrown to indicate that someone
 * tried to use a token that has a limited lifespan and is
 * past that limit.
 */
public class ExpiredTokenException extends Exception {
    public ExpiredTokenException(String message) {
        super(message);
    }
}
