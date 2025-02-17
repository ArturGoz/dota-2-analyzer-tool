package artur.goz.heroesstatsservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
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


    public HeroMatchUps(String heroName, String enemyHeroName, Float winrate,
                        Integer matchCount, String heroPosition, String enemyHeroPosition) {
        this.heroName = heroName;
        this.enemyHeroName = enemyHeroName;
        this.winrate = winrate;
        this.matchCount = matchCount;
        this.heroPosition = heroPosition;
        this.enemyHeroPosition = enemyHeroPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeroMatchUps that = (HeroMatchUps) o;
        return Objects.equals(id, that.id) && Objects.equals(heroName, that.heroName) && Objects.equals(enemyHeroName, that.enemyHeroName) && Objects.equals(winrate, that.winrate) && Objects.equals(matchCount, that.matchCount) && Objects.equals(heroPosition, that.heroPosition) && Objects.equals(enemyHeroPosition, that.enemyHeroPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, heroName, enemyHeroName, winrate, matchCount, heroPosition, enemyHeroPosition);
    }

    @Override
    public String toString() {
        return "HeroMatchUps{" +
                "id=" + id +
                ", heroName='" + heroName + '\'' +
                ", enemyHeroName='" + enemyHeroName + '\'' +
                ", winrate=" + winrate +
                ", matchCount=" + matchCount +
                ", heroPosition='" + heroPosition + '\'' +
                ", enemyHeroPosition='" + enemyHeroPosition + '\'' +
                '}';
    }
}