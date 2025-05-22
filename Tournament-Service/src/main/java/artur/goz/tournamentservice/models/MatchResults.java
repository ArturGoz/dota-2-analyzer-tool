package artur.goz.tournamentservice.models;

import artur.goz.tournamentservice.Converters.StringArrayConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class MatchResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = StringArrayConverter.class)
    private String[] teamHeroesWinner;


    @Convert(converter = StringArrayConverter.class)
    private String[] teamHeroesLoser;

    private String teamWinnerName;
    private String teamLoserName;
    private Integer emptyLineUps;
    private Float teamWinnerWinrate;

    @ManyToOne
    @JoinColumn(name = "tournament")
    @JsonBackReference
    private Tournament tournament;
}
