package artur.goz.userservice.rabbitmq;

import artur.goz.userservice.dto.LoginDTO;
import artur.goz.userservice.dto.RabbitRequest;
import artur.goz.userservice.dto.RabbitResponse;
import artur.goz.userservice.dto.UserDTO;
import artur.goz.userservice.mapper.UserMapper;
import artur.goz.userservice.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitManager {
    private static final Logger log = LogManager.getLogger(RabbitManager.class);
    private final UserServiceImpl myUserService;
    private final UserMapper userMapper;


    @RabbitListener(queues = {"RegisterQueue"},
            errorHandler = "myCustomErrorHandler")
    public RabbitResponse<UserDTO> registerHandler(RabbitRequest<UserDTO> rabbitRequest) {
        UserDTO newUserDto = myUserService.register(rabbitRequest.getData());
        log.debug("Returning newUserDto: {}", newUserDto);
        return RabbitResponse.create(newUserDto, null, true);
    }

    @RabbitListener(queues = {"LoginQueue"},
            errorHandler = "myCustomErrorHandler")
    public RabbitResponse<UserDTO> loginHandler(RabbitRequest<LoginDTO> rabbitRequest) {
        UserDTO userDTO = myUserService.login(rabbitRequest.getData());
        log.debug("Returning userDTO : {}", userDTO);
        return RabbitResponse.create(userDTO, null, true);
    }

    @RabbitListener(queues = {"decrementLimitForUserQueue"},
            errorHandler = "myCustomErrorHandler")
    //String -> username
    public RabbitResponse<Boolean> decrementLimitHandler(RabbitRequest<String> rabbitRequest) {
        myUserService.decrementUserLimit(rabbitRequest.getData());
        log.debug("decrementing is done for: {}", rabbitRequest.getData());
        return RabbitResponse.create(true, null, true);
    }
}
