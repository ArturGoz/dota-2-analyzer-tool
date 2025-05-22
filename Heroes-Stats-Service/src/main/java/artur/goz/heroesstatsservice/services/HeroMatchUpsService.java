package artur.goz.heroesstatsservice.services;



import artur.goz.heroesstatsservice.dto.HeroStats;
import artur.goz.heroesstatsservice.models.HeroMatchUps;
import artur.goz.heroesstatsservice.repositories.HeroMatchUpsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class HeroMatchUpsService {
    HeroMatchUpsRepo heroMatchUpsRepo;

    @Autowired
    public HeroMatchUpsService(HeroMatchUpsRepo heroMatchUpsRepo) {
        this.heroMatchUpsRepo = heroMatchUpsRepo;
    }


    public void addHeroMatchUps(List<HeroMatchUps> heroMatchUpsListToAdd, String heroName) {
        try{
            List<HeroMatchUps> mainHeroMatchUpsList = heroMatchUpsRepo.findByHeroName(heroName);
            if(mainHeroMatchUpsList == null){
                throw new RuntimeException("Hero Match Ups list is null");
            }
            HashMap<String, HeroMatchUps> mainHeroMatchUpsMap = new HashMap<>();

            for(HeroMatchUps heroMatchUps : mainHeroMatchUpsList){
                mainHeroMatchUpsMap.put(convertToUniqueString(heroMatchUps), heroMatchUps);
            }

            for(HeroMatchUps heroMatchUps : heroMatchUpsListToAdd){
                HeroMatchUps hero1 = mainHeroMatchUpsMap.get(convertToUniqueString(heroMatchUps));
                if(hero1 != null){
                    hero1.setUpdateCount(hero1.getUpdateCount() + 1);
                    Float generalAverageWinrate = hero1.getWinrate() +
                            ((heroMatchUps.getWinrate() - hero1.getWinrate()) / hero1.getUpdateCount());
                    hero1.setWinrate(generalAverageWinrate);
                    int totalMatches = hero1.getMatchCount() + heroMatchUps.getMatchCount();
                    hero1.setMatchCount(totalMatches);
                }
                else{
                    mainHeroMatchUpsList.add(heroMatchUps);
                }
            }
            heroMatchUpsRepo.saveAll(mainHeroMatchUpsList);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    public void saveAllHeroMatchUpsList(List<HeroMatchUps> heroMatchUps) {
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

    private String convertToHeroVsEnemyHero(HeroMatchUps heroMatchUps){
        return heroMatchUps.getHeroName() + " vs " + heroMatchUps.getEnemyHeroName();
    }
    private String convertToHeroPositions(HeroMatchUps heroMatchUps){
        return heroMatchUps.getHeroPosition() + " vs " + heroMatchUps.getEnemyHeroPosition();
    }
    private String convertToUniqueString(HeroMatchUps heroMatchUps){
        return convertToHeroVsEnemyHero(heroMatchUps) + " " + convertToHeroPositions(heroMatchUps);
    }
}
