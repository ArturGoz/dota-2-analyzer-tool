package artur.goz.userservice.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class RabbitResponse<T> {
    private T data;
    private String errorMessage;
    private Boolean success;
    public static <T> RabbitResponse<T> create(T data, String errorMessage, Boolean success) {
        RabbitResponse<T> r = new RabbitResponse<>();
        r.data = data;
        r.errorMessage = errorMessage;
        r.success = success;
        log.info("RabbitResponse created data: {}", r);
        return r;
    }
}
