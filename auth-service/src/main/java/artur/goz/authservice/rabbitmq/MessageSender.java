package artur.goz.authservice.rabbitmq;

import artur.goz.authservice.dto.LoginDto;
import artur.goz.authservice.dto.MyUserVO;
import artur.goz.authservice.dto.RegisterDto;
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
public class MessageSender {
    @Value("${queue.register}")
    private String registerQueue;

    @Value("${queue.login}")
    private String loginQueue;

    private final AmqpTemplate rabbitTemplate;
    @Autowired
    public MessageSender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public MyUserVO sendRegister(RegisterDto registerDto) {
        log.info("Sending RegisterDto: {}", registerDto);
        MyUserVO myUserVO = rabbitTemplate.convertSendAndReceiveAsType(registerQueue, registerDto,
                new ParameterizedTypeReference<MyUserVO>() {});
        log.info("Received response: {}", myUserVO);
        return myUserVO;
    }

    public MyUserVO sendLogin(LoginDto loginDto) {
        log.info("Sending LoginDto: {}", loginDto);
        MyUserVO myUserVO = rabbitTemplate.convertSendAndReceiveAsType(loginQueue, loginDto,
                new ParameterizedTypeReference<MyUserVO>() {});
        log.info("Received response : {}", myUserVO);
        return myUserVO;
    }

}
