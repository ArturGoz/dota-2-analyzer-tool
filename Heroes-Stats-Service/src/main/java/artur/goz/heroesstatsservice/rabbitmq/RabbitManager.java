package artur.goz.heroesstatsservice.rabbitmq;

import artur.goz.heroesstatsservice.dto.GameStats;
import artur.goz.heroesstatsservice.dto.HeroesInfo;
import artur.goz.heroesstatsservice.dto.RabbitRequest;
import artur.goz.heroesstatsservice.dto.RabbitResponse;
import artur.goz.heroesstatsservice.services.GameStatsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitManager {
    private final AmqpTemplate rabbitTemplate;
    private final GameStatsService gameStatsService;

    @Value("${queue.decrementLimitForUser}")
    private String queueDecrementLimitForUser;


    @Autowired
    public RabbitManager(AmqpTemplate rabbitTemplate, GameStatsService gameStatsService) {
        this.rabbitTemplate = rabbitTemplate;
        this.gameStatsService = gameStatsService;
    }

    public void decrementUserLimit(RabbitRequest<String> usernameRequest) {
        log.debug("Sending username to decrement limit : {}", usernameRequest.getData());
        RabbitResponse<String> rabbitResponse = rabbitTemplate.convertSendAndReceiveAsType(queueDecrementLimitForUser, usernameRequest,
                new ParameterizedTypeReference<RabbitResponse<String>>() {});
        isDataExist(rabbitResponse);
        log.debug("Limit was decremented for user {}", usernameRequest.getData());
    }

    @RabbitListener(queues = {"StatsQueue"},
            errorHandler = "myCustomErrorHandler")
    public RabbitResponse<GameStats> receivingAndSendingStats(RabbitRequest<HeroesInfo> heroesInfo) {
        try {
            log.debug("Receiving  heroesInfo : {}", heroesInfo);
            GameStats gameStats = gameStatsService.getGameStats(heroesInfo.getData());

            log.debug("sending gameStats: {}", gameStats);
            return RabbitResponse.create(gameStats, "game stats successfully sent", true);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("error occurred while sending gameStats and " + e.getMessage());
        }
    }

    private <T> void isDataExist(RabbitResponse<T> rabbitResponse) {
        if(rabbitResponse == null) {
            throw new RuntimeException("RabbitResponse is null");
        }
        if (rabbitResponse.getExceptionMessage() != null || rabbitResponse.getData() == null) {
            throw new RuntimeException(
                    rabbitResponse.getExceptionMessage()
            );
        }
    }

}
