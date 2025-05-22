package artur.goz.tournamentservice.rabbitmq;

import artur.goz.tournamentservice.dto.GameStats;
import artur.goz.tournamentservice.dto.HeroesInfo;
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
    @Value("${queue.stats}")
    private String gameStatsQueue;

    private final AmqpTemplate rabbitTemplate;
    @Autowired
    public MessageSender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public GameStats getGameStats(HeroesInfo heroesInfo) {
        log.info("Sending HeroesInfo: {}", heroesInfo);
        RabbitRequest<HeroesInfo> rabbitRequest = RabbitRequest.createRabbitRequest(heroesInfo);
        RabbitResponse<GameStats> gameStats = rabbitTemplate.convertSendAndReceiveAsType(gameStatsQueue, rabbitRequest,
                new ParameterizedTypeReference<RabbitResponse<GameStats>>() {});
        log.info("Received response: {}", gameStats);
        return getDataFromResponse(gameStats);
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
