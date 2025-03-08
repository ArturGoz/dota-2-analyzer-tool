package artur.goz.userservice.exception;

import lombok.Getter;

@Getter
public class MyException extends RuntimeException {
    private String errorCode;
    public MyException(String message) {
        super(message);
    }

    public MyException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
