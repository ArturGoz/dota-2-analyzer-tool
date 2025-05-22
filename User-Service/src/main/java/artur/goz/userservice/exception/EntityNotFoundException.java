package artur.goz.userservice.exception;

public class EntityNotFoundException extends MyException {
    public EntityNotFoundException(String message) {
        super(message);
    }
    public EntityNotFoundException(String message, String statusCode) {
        super(message,statusCode);
    }
}
