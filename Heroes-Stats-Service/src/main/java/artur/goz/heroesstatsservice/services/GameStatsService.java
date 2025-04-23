package artur.goz.heroesstatsservice.services;


import artur.goz.heroesstatsservice.dto.GameStats;
import artur.goz.heroesstatsservice.dto.HeroStats;
import artur.goz.heroesstatsservice.dto.HeroesInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class GameStatsService {
    private static final Logger log = LogManager.getLogger(GameStatsService.class);
    private final Map<String, HeroStats> heroStatsMap = new HashMap<>();
    public  static  String[] heroPositions = {"pos_1","pos_2","pos_3","pos_4","pos_5"};
    private static final int MIN_MATCHES_REQUIRED = 20;

    private final HeroMatchUpsService heroMatchUpsService;

    public GameStats getGameStats(HeroesInfo heroesInfo) {
        GameStats gameStats = new GameStats();
        int heroesWithWinrate = heroesInfo.getHeroes().length;
        float generalWinrate = 0;

        for (int i = 0; i < heroesInfo.getHeroes().length; i++) {
            float heroWinrate = calculateHeroWinrate(heroesInfo.getHeroes()[i], heroesInfo.getEnemies(), heroPositions[i], gameStats);
            if (heroWinrate == -1) {
                heroesWithWinrate--;
            } else {
                generalWinrate += heroWinrate;
            }
        }

        gameStats.setHeroStatsMap(heroStatsMap);

        if (heroesWithWinrate == 0) {
            log.warn("All heroes do not have needed stats");
            gameStats.setLeftSideWinner(-1.0f);
            return gameStats;
        }

        generalWinrate /= heroesWithWinrate;
        gameStats.setLeftSideWinner(generalWinrate);
        log.info("Winrate of the first team is {}", generalWinrate);
        return gameStats;
    }

    private float calculateHeroWinrate(String hero, String[] enemies, String heroPosition, GameStats gameStats) {
        int validMatches = enemies.length;
        float totalWinrate = 0;

        for (int j = 0; j < enemies.length; j++) {
            float winrate = getHeroStats(hero, enemies[j], heroPosition, heroPositions[j]);
            if (winrate == -1) {
                log.debug("{}{} has no 20 matches with {}{}", heroPosition, hero, heroPositions[j], enemies[j]);
                validMatches--;
                gameStats.setNoWinrateForHeroesCounter(gameStats.getNoWinrateForHeroesCounter() + 1);;
            } else {
                totalWinrate += winrate;
            }
        }

        if (validMatches == 0) {
            log.debug("{} {} has no 20 matches with enemy heroes at all", heroPosition, hero);
            return -1;
        }

        return totalWinrate / validMatches;
    }

    public  float getHeroStats(String hero, String enemyHero, String positionForHero, String positionForEnemyHero){
        try
        {
            HeroStats heroStats = heroMatchUpsService.findHeroStats(hero, enemyHero, positionForHero, positionForEnemyHero);
            heroStatsMap.put(hero +"_vs_" + enemyHero,heroStats);
            return heroStats.getMatches() > MIN_MATCHES_REQUIRED ? heroStats.getWinrate() : -1;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.error("heroes : {} and {} has a trouble", hero, enemyHero);
            return -1;
        }
    }

}
