package artur.goz.heroesstatsservice.dto;


import lombok.Data;

@Data
public class RabbitRequest<T> {
    private T data;

    public static <T> RabbitRequest<T> createRabbitRequest(T data) {
        RabbitRequest<T> r = new RabbitRequest<>();
        r.setData(data);
        return r;
    }
}

