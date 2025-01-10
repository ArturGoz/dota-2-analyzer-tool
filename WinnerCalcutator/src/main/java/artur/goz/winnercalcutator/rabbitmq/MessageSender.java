package artur.goz.winnercalcutator.rabbitmq;

import artur.goz.winnercalcutator.dto.GameStats;
import artur.goz.winnercalcutator.dto.HeroesInfo;
import lombok.Setter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Setter
@Service
public class MessageSender {
    @Value("${queue.name}")
    private String queueName;

    private final AmqpTemplate rabbitTemplate;
    @Autowired
    public MessageSender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public GameStats sendMessage(HeroesInfo heroesInfo) {
        return (GameStats) rabbitTemplate.convertSendAndReceive(queueName,heroesInfo);
    }
}
