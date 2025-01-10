package artur.goz.heroesstatsservice.rabbitmq;

import artur.goz.heroesstatsservice.dto.GameStats;
import artur.goz.heroesstatsservice.dto.HeroesInfo;
import artur.goz.heroesstatsservice.utilities.GameStatsService;
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
    public GameStats receive(HeroesInfo heroesInfo) {
        log.info(heroesInfo.getClass());
        GameStats gameStats = gameStatsService.getGameStats(heroesInfo);
        return gameStats;
    }
}
