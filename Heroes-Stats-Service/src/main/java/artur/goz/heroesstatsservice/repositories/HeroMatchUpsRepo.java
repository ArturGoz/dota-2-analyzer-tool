package artur.goz.heroesstatsservice.repositories;


import artur.goz.heroesstatsservice.models.HeroMatchUps;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HeroMatchUpsRepo extends JpaRepository<HeroMatchUps, Long> {
    @Query(value = "SELECT winrate, match_count FROM hero_match_ups WHERE hero_name = :heroName AND enemy_hero_name = :enemyHeroName AND match_count > 20 AND hero_position = :heroPosition AND enemy_hero_position = :enemyHeroPosition", nativeQuery = true)
    List<Object[]> findWinrateAndMatchCount(@Param("heroName") String heroName,
                                            @Param("enemyHeroName") String enemyHeroName,
                                            @Param("heroPosition") String heroPosition,
                                            @Param("enemyHeroPosition") String enemyHeroPosition);
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE hero_match_ups", nativeQuery = true)
    void truncateTable();

    List<HeroMatchUps> findByHeroName(String heroName);
}

