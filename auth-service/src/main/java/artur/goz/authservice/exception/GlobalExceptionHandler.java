package artur.goz.authservice.exception;

import artur.goz.authservice.dto.RemoteResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RemoteResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse(ex.getMessage());


        RemoteResponse remoteResponse = RemoteResponse.create(
                false,
                StatusCode.INVALID_DATA.name() + " " + errors,
                null
        );

        return new ResponseEntity<>(remoteResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<RemoteResponse> handleEntityAlreadyExistsException(RuntimeException ex){
        RemoteResponse remoteResponse =
                RemoteResponse.create(false,ex.getMessage(),null);
        return new ResponseEntity<>(remoteResponse, HttpStatus.BAD_REQUEST);
    }
}
