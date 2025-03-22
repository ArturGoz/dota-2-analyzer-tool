package artur.goz.heroesstatsservice.rabbitmq;

import artur.goz.heroesstatsservice.dto.GameStats;
import artur.goz.heroesstatsservice.dto.HeroesInfo;
import artur.goz.heroesstatsservice.services.GameStatsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class RabbitManager {
    private static final Logger log = LogManager.getLogger(RabbitManager.class);
    private final AmqpTemplate rabbitTemplate;
    private final GameStatsService gameStatsService;

    @Value("${queue.decrementLimitForUser}")
    private String queueDecrementLimitForUser;


    @Autowired
    public RabbitManager(AmqpTemplate rabbitTemplate, GameStatsService gameStatsService) {
        this.rabbitTemplate = rabbitTemplate;
        this.gameStatsService = gameStatsService;
    }

    public void decrementUserLimit(String username) {
        log.debug("Sending username to decrement limit : ", username);

/*        GameStats gameStats = rabbitTemplate.convertSendAndReceiveAsType(gameStatsQueue, heroesInfo,
                new ParameterizedTypeReference<GameStats>() {});*/

        rabbitTemplate.convertAndSend(queueDecrementLimitForUser,username);
        log.debug("Limit was decremented for user ", username);
    }

    @RabbitListener(queues = {"StatsQueue"})
    public GameStats receivingAndSendingStats(HeroesInfo heroesInfo) {
        try {
            log.debug("Receiving  heroesInfo : {}", heroesInfo);
            GameStats gameStats = gameStatsService.getGameStats(heroesInfo);
            log.debug("sending gameStats: {}", gameStats);
            return gameStats;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

}
