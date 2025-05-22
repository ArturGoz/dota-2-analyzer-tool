package artur.goz.userservice.exception;

public class LimitException extends MyException {
    public LimitException(String message) {
        super(message);
    }
    public LimitException(String message, String statusCode) {
        super(message,statusCode);
    }
}
