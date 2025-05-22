package artur.goz.userservice.dto;


import lombok.Data;

import java.util.List;

@Data
public class RabbitRequest<T> {
    private T data;

    public static <T> RabbitRequest<T> createRabbitRequest(T data) {
        RabbitRequest<T> r = new RabbitRequest<>();
        r.setData(data);
        return r;
    }
}

