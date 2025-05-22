package artur.goz.tournamentservice.Controllers;

import artur.goz.tournamentservice.Services.DLTVMatchesService;
import artur.goz.tournamentservice.Services.MatchResultsService;
import artur.goz.tournamentservice.Services.TournamentService;
import artur.goz.tournamentservice.dto.MatchResultsDTO;
import artur.goz.tournamentservice.dto.RemoteResponse;
import artur.goz.tournamentservice.dto.TournamentInfo;
import artur.goz.tournamentservice.models.MatchResults;

import artur.goz.tournamentservice.models.Tournament;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournament")
@Slf4j
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;
    private final MatchResultsService matchResultsService;
    private final DLTVMatchesService dltvMatchesService;

    @GetMapping("/getList")
    public ResponseEntity<RemoteResponse> getTournamentList() {
        try {
            log.info("Getting list of tournaments");
            List<String> tournaments = tournamentService.getAllTournamentNames();
            RemoteResponse remoteResponse =
                    RemoteResponse.create(true, "List successfully retrieved", List.of(tournaments));
            return ResponseEntity.ok(remoteResponse);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error retrieving tournament list", e);
        }
    }

    @GetMapping("/getResults")
    public ResponseEntity<RemoteResponse> getMatchResultsByTournamentName(@RequestParam String tournamentName) {
        try {
            log.info("Getting match results by name: {}", tournamentName);
            List<MatchResultsDTO> matchResults = matchResultsService.getMatchResultsDTOByTournamentName(tournamentName);
            RemoteResponse remoteResponse = RemoteResponse
                    .create(true, "List successfully retrieved", List.of(matchResults));
            return ResponseEntity.ok(remoteResponse);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error retrieving match results by tournamentName", e);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<RemoteResponse> addingTournament(@RequestBody TournamentInfo tournamentInfo) {
        try {
            log.info("Retrieve TournamentInfo : {}", tournamentInfo);
            Tournament createdTournament = tournamentService.addTournament(tournamentInfo.getTournament());
            dltvMatchesService.addMatches(createdTournament, tournamentInfo.getTournamentUrl());
            RemoteResponse remoteResponse = RemoteResponse
                    .create(true, "Tournament successfully added", List.of(tournamentInfo));
            return ResponseEntity.ok(remoteResponse);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error adding tournament " + tournamentInfo.getTournament(), e);
        }
    }

}
