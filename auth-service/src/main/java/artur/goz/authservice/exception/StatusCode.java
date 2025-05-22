package artur.goz.authservice.exception;

public enum StatusCode {
    OK,
    INVALID_DATA,
    ENTITY_NOT_FOUND,
    DUPLICATE_USERNAME,
    WRONG_PASSWORD,
    LIMIT_EXCEEDED;
}
