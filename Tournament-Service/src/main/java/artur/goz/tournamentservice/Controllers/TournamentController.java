package artur.goz.tournamentservice.Controllers;

import artur.goz.tournamentservice.Services.TournamentService;
import artur.goz.tournamentservice.models.Tournament;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tournament")
@Slf4j
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/getList")
    public ResponseEntity<List<String>> getTournamentList() {
        try {
            log.info("Getting list of tournaments");
            List<String> tournaments = tournamentService.getAllTournamentNames();
            log.info("List of tournaments: {}", tournaments);
            return ResponseEntity.ok(tournaments);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
