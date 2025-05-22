package artur.goz.tournamentservice.dto;

import artur.goz.tournamentservice.models.Tournament;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentInfo {
    private Tournament tournament;
    private String tournamentUrl;
}
