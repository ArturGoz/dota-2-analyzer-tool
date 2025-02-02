package artur.goz.heroesstatsservice.rabbitmq;

import artur.goz.heroesstatsservice.dto.GameStats;
import artur.goz.heroesstatsservice.dto.HeroesInfo;
import artur.goz.heroesstatsservice.services.GameStatsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitReceiver {
    private static final Logger log = LogManager.getLogger(RabbitReceiver.class);

    @Autowired
    private GameStatsService gameStatsService;

    @RabbitListener(queues = {"FirstQueue"})
    public GameStats receiveMessage(HeroesInfo heroesInfo) {
        try {
            log.info("Received HeroesInfo: {}", heroesInfo);
            // Логіка обробки даних
            GameStats gameStats = gameStatsService.getGameStats(heroesInfo);
            log.info("Returning GameStats: {}", gameStats);
            return gameStats; // Цей об'єкт буде відправлений у *reply queue* як відповідь.
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
