package artur.goz.winnercalcutator.rabbitmq;

import artur.goz.winnercalcutator.dto.GameStats;
import artur.goz.winnercalcutator.dto.HeroesInfo;
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
    @Value("${queue.name}")
    private String queueName;

    @Value("${queue.decrementLimitForUser}")
    private String queueDecrementLimitForUser;

    private final AmqpTemplate rabbitTemplate;
    @Autowired
    public MessageSender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public GameStats sendMessage(HeroesInfo heroesInfo) {
        // Відправка повідомлення і очікування відповіді
        log.debug("Sending HeroesInfo: {}", heroesInfo);
        GameStats response = rabbitTemplate.convertSendAndReceiveAsType(queueName, heroesInfo,
                new ParameterizedTypeReference<GameStats>() {});
        log.debug("Received response: {}", response);
        return response;
    }

    public void decrementUserLimit(String username) {
        log.debug("Sending username to decrement limit : ", username);
        rabbitTemplate.convertAndSend(queueDecrementLimitForUser,username);
        log.debug("Limit was decremented for user ", username);

    }
}
