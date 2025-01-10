package artur.goz.winnercalcutator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStats implements Serializable {
    private Float leftSideWinner;
    private  Map<String, HeroStats> heroStatsMap = new HashMap<>();
    private int noWinrateForHeroesCounter;
}
