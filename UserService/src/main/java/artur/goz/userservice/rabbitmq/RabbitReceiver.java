package artur.goz.userservice.rabbitmq;

import artur.goz.userservice.dto.LoginDto;
import artur.goz.userservice.dto.MyUserVO;
import artur.goz.userservice.dto.RegisterDto;
import artur.goz.userservice.models.MyUser;
import artur.goz.userservice.services.MyUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitReceiver {
    private static final Logger log = LogManager.getLogger(RabbitReceiver.class);

    @Autowired
    private MyUserService myUserService;

    @RabbitListener(queues = {"RegisterQueue"})
    public MyUserVO receiveMessage(RegisterDto registerDto) {
        log.debug("Received RegisterDto: {}", registerDto);

        MyUserVO myUserVO = new MyUserVO();
        myUserVO.setEmail(registerDto.getEmail());
        myUserVO.setName(registerDto.getName());
        myUserVO.setPassword(registerDto.getPassword());
        myUserVO.setRole("ROLE_USER");

        myUserService.registerUser(registerDto);

        log.debug("Returning MyUserVO: {}", myUserVO);
        return myUserVO; // Цей об'єкт буде відправлений у *reply queue* як відповідь.
    }

    @RabbitListener(queues = {"LoginQueue"})
    public MyUserVO receiveMessage(LoginDto loginDto) {
        log.debug("Received LoginDto: {}", loginDto);

        MyUser myUser = myUserService.getMyUserByName(loginDto.getName()).orElse(null);

        if (myUser == null) {
            return null;
        }
        MyUserVO myUserVO = new MyUserVO();
        myUserVO.setEmail(myUser.getEmail());
        myUserVO.setName(myUser.getName());
        myUserVO.setPassword(myUser.getPassword());
        myUserVO.setRole(String.join(",", myUser.getRoles()));


        log.debug("Returning MyUserVO : {}", myUserVO);
        return myUserVO; // Цей об'єкт буде відправлений у *reply queue* як відповідь.
    }

    @RabbitListener(queues = {"decrementLimitForUserQueue"})
    public void receiveUsername(String username) {
        log.debug("Receiving  username : {}", username);
        myUserService.decrementUserLimit(username);
        log.debug("decrementing is done for: {}", username);
    }
}
