package artur.goz.heroesstatsservice.services;



import artur.goz.heroesstatsservice.dto.HeroStats;
import artur.goz.heroesstatsservice.models.HeroMatchUps;
import artur.goz.heroesstatsservice.repositories.HeroMatchUpsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class HeroMatchUpsService {
    @Autowired
    HeroMatchUpsRepo heroMatchUpsRepo;


    public void addMatchUpList(List<HeroMatchUps> heroMatchUps) throws SQLException {
        heroMatchUpsRepo.saveAll(heroMatchUps);
    }

    public HeroStats findHeroStats(String hero, String enemyHero, String heroPosition, String enemyPosition) {
        List<Object[]> results = heroMatchUpsRepo.findWinrateAndMatchCount(hero, enemyHero, heroPosition, enemyPosition);
        if (!results.isEmpty()) {
            Float winrate = (Float) results.get(0)[0];
            Integer matches = (Integer) results.get(0)[1];
            return new HeroStats(winrate, matches);
        }

        results = heroMatchUpsRepo.findWinrateAndMatchCount(enemyHero, hero, enemyPosition, heroPosition);
        if (!results.isEmpty()) {
            Float winrateOpposite = (Float) results.get(0)[0];
            Integer matches = (Integer) results.get(0)[1];
            return new HeroStats(100.0f - winrateOpposite, matches);
        }

        return new HeroStats(-1f, -1); // Default case when no stats found
    }

    public void truncateTable(){
        heroMatchUpsRepo.truncateTable();
    }
}
