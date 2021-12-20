package security.exceptions;

public class TokenNotFoundException extends Exception {

    public TokenNotFoundException(String message) {
        super(message);
    }
    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
