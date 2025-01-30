package artur.goz.tournamentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultsDTO {
    private String[] teamHeroesWinner;
    private String[] teamHeroesLoser;

    private String teamWinnerName;
    private String teamLoserName;
    private Integer emptyLineUps;
    private Float teamWinnerWinrate;
}
