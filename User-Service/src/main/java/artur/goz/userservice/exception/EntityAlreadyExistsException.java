package artur.goz.userservice.exception;

public class EntityAlreadyExistsException extends MyException {
  public EntityAlreadyExistsException(String message) {
    super(message);
  }
  public EntityAlreadyExistsException(String message, String statusCode) {
    super(message,statusCode);
  }
}
