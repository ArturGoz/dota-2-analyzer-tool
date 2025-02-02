package artur.goz.tournamentservice.Controllers;

import artur.goz.tournamentservice.Services.DLTVMatchesService;
import artur.goz.tournamentservice.Services.MatchResultsService;
import artur.goz.tournamentservice.Services.TournamentService;
import artur.goz.tournamentservice.dto.MatchResultsDTO;
import artur.goz.tournamentservice.dto.TournamentInfo;
import artur.goz.tournamentservice.models.MatchResults;

import artur.goz.tournamentservice.models.Tournament;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournament")
@Slf4j
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private MatchResultsService matchResultsService;
    @Autowired
    private DLTVMatchesService dltvMatchesService;

    @GetMapping("/getList")
    public ResponseEntity<List<String>> getTournamentList() {
        try {
            log.info("Getting list of tournaments");
            List<String> tournaments = tournamentService.getAllTournamentNames();
            log.info("List of tournaments: {}", tournaments);
            return ResponseEntity.ok(tournaments);
        } catch (Exception e) {
            log.error("Error retrieving tournament list: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getResults")
    public ResponseEntity<List<MatchResultsDTO>> getMatchResultsByTournamentName(@RequestParam String tournamentName) {
        try {
            log.info("Getting match results by name: {}", tournamentName);
            List<MatchResultsDTO> matchResults = matchResultsService.getMatchResultsDTOByTournamentName(tournamentName);
            log.info("Match results: {}", matchResults);
            return ResponseEntity.ok(matchResults);
        } catch (Exception e) {
            log.error("Error retrieving tournament results: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addingTournament(@RequestBody TournamentInfo tournamentInfo) {
        try {
            log.info("Retrieve TournamentInfo : {}", tournamentInfo);
            Tournament createdTournament = tournamentService.addTournament(tournamentInfo.getTournament());
            dltvMatchesService.addMatches(createdTournament, tournamentInfo.getTournamentUrl());
            log.info("Tournament added: {}", tournamentInfo);
            return ResponseEntity.ok("Tournament added");
        } catch (Exception e) {
            log.error("Error in adding tournament: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in adding tournament");
        }
    }

}
