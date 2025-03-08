package artur.goz.userservice.rabbitmq;

import artur.goz.userservice.dto.*;
import artur.goz.userservice.services.MyUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitReceiver {
    private static final Logger log = LogManager.getLogger(RabbitReceiver.class);
    private final MyUserServiceImpl myUserService;


/*    @RabbitListener(queues = {"RegisterQueue"})
    public MyUserVO receiveRegisterDto(RegisterDto registerDto) {
        try {
            log.debug("Received RegisterDto: {}", registerDto);

            MyUserVO myUserVO = new MyUserVO();
            myUserVO.setEmail(registerDto.getEmail());
            myUserVO.setName(registerDto.getName());
            myUserVO.setPassword(registerDto.getPassword());
            myUserVO.setRole("ROLE_USER");

            myUserService.registerUser(registerDto);

            log.debug("Returning MyUserVO: {}", myUserVO);
            return myUserVO; // Цей об'єкт буде відправлений у *reply queue* як відповідь.
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }*/

    @RabbitListener(queues = {"RegisterQueue"})
    public RabbitResponse<MyUserDTO> receiveRegisterDto(RabbitRequest<RegisterDto> rabbitRequest) {
            log.debug("Received rabbitRequest: {}", rabbitRequest);
            RegisterDto registerDto = rabbitRequest.getData();

            MyUserVO myUserVO = new MyUserVO();
            myUserVO.setEmail(registerDto.getEmail());
            myUserVO.setName(registerDto.getName());
            myUserVO.setPassword(registerDto.getPassword());
            myUserVO.setRole("ROLE_USER");

            MyUserDTO newUserDto = myUserService.registerUser(registerDto);

            log.debug("Returning MyUserVO: {}", myUserVO);
            return RabbitResponse.create(newUserDto,null,true);
    }

    @RabbitListener(queues = {"LoginQueue"})
    public MyUserVO receiveLoginDto(LoginDto loginDto) {
        log.debug("Received LoginDto: {}", loginDto);
        try {
            MyUserVO myUserVO = myUserService.auth(loginDto.getName(),loginDto.getPassword());
            log.debug("Returning MyUserVO : {}", myUserVO);
            return myUserVO; // Цей об'єкт буде відправлений у *reply queue* як відповідь.
        } catch (Exception e)
        {
            log.error(e);
            return null;
        }
    }

    @RabbitListener(queues = {"decrementLimitForUserQueue"})
    public void receiveUsername(String username) {
        try {
            log.debug("Receiving  username : {}", username);
            myUserService.decrementUserLimit(username);
            log.debug("decrementing is done for: {}", username);
        } catch (Exception e) {
            log.error(e);
        }
    }
}
