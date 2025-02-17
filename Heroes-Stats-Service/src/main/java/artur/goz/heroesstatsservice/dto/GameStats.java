package artur.goz.heroesstatsservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameStats {
    private Float leftSideWinner;
    private  Map<String, HeroStats> heroStatsMap = new HashMap<>();
    private int noWinrateForHeroesCounter;

    @Override
    public String toString() {
        return "GameStats{" +
                "leftSideWinner=" + leftSideWinner +
                ", heroStatsMap=" + heroStatsMap +
                ", noWinrateForHeroesCounter=" + noWinrateForHeroesCounter +
                '}';
    }
}
