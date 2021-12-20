package security.exceptions;

public class UniquePlayerIdExistException extends Exception {
    public UniquePlayerIdExistException(String message) {
        super(message);
    }

    public UniquePlayerIdExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
