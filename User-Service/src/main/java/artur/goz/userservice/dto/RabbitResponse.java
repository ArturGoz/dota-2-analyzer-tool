package artur.goz.userservice.dto;

import artur.goz.userservice.exception.MyException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class RabbitResponse<T> {
    private T data;
    private String exceptionMessage;
    private Boolean success;

    public static <T> RabbitResponse<T> create(T data, String exceptionMessage, Boolean success) {
        RabbitResponse<T> r = new RabbitResponse<>();
        r.data = data;
        r.exceptionMessage = exceptionMessage;
        r.success = success;
        log.info("RabbitResponse created data: {}", r);
        return r;
    }
}
