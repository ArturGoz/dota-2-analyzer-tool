package artur.goz.authservice.rabbitmq;

import artur.goz.authservice.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;



@Slf4j
@Setter
@Service
@RequiredArgsConstructor
public class RabbitManager {
    @Value("${queue.register}")
    private String registerQueue;

    @Value("${queue.login}")
    private String loginQueue;

    private final AmqpTemplate rabbitTemplate;

    public UserDTO doRegister(RabbitRequest<UserDTO> rabbitRequest) {
        log.info("Sending userDTO: {}", rabbitRequest.getData());
        RabbitResponse<UserDTO> rabbitResponse = rabbitTemplate.convertSendAndReceiveAsType(registerQueue, rabbitRequest,
                new ParameterizedTypeReference<RabbitResponse<UserDTO>>() {});
        log.info("Received response: {}", rabbitResponse);
        return getDataFromResponse(rabbitResponse);
    }

    public UserDTO doLogin(LoginDto loginDto) {
        log.info("Sending LoginDto: {}", loginDto);
        RabbitResponse<UserDTO> rabbitResponse = rabbitTemplate.convertSendAndReceiveAsType(loginQueue, loginDto,
                new ParameterizedTypeReference<RabbitResponse<UserDTO>>() {});
        log.info("Received response : {}", rabbitResponse);
        return getDataFromResponse(rabbitResponse);
    }

    private <T> T getDataFromResponse(RabbitResponse<T> rabbitResponse) {
        if(rabbitResponse == null) {
            throw new RuntimeException("RabbitResponse is null");
        }
        if (rabbitResponse.getExceptionMessage() != null) {
            throw new RuntimeException(
                    rabbitResponse.getExceptionMessage()
            );
        }
        return rabbitResponse.getData();
    }
}
