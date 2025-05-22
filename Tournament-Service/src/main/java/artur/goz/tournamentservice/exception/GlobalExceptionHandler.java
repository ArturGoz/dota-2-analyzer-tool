package artur.goz.tournamentservice.exception;

import artur.goz.tournamentservice.dto.RemoteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<RemoteResponse> handleEntityAlreadyExistsException(RuntimeException ex){
        RemoteResponse remoteResponse =
                RemoteResponse.create(false,ex.getMessage(),null);
        return new ResponseEntity<>(remoteResponse, HttpStatus.BAD_REQUEST);
    }
}
