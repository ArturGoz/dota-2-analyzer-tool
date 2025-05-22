package artur.goz.heroesstatsservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Data
public class HeroMatchUps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String heroName;
    private String enemyHeroName;
    private Float winrate;
    private Integer matchCount;
    private String heroPosition;
    private String enemyHeroPosition;
    private Integer updateCount;


    public HeroMatchUps(String heroName, String enemyHeroName, Float winrate,
                        Integer matchCount, String heroPosition, String enemyHeroPosition) {
        this.heroName = heroName;
        this.enemyHeroName = enemyHeroName;
        this.winrate = winrate;
        this.matchCount = matchCount;
        this.heroPosition = heroPosition;
        this.enemyHeroPosition = enemyHeroPosition;
        this.updateCount = 1;
    }

    public HeroMatchUps() {

    }
}