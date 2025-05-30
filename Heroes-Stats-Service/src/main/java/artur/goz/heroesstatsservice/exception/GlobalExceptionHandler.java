package artur.goz.heroesstatsservice.exception;

import artur.goz.heroesstatsservice.dto.RemoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<RemoteResponse> handleEntityAlreadyExistsException(RuntimeException ex){
        RemoteResponse remoteResponse =
                RemoteResponse.create(false,ex.getMessage(),null);
        return new ResponseEntity<>(remoteResponse, HttpStatus.BAD_REQUEST);
    }
}
