package artur.goz.tournamentservice.Services;


import artur.goz.tournamentservice.Repositories.MatchResultsRepo;
import artur.goz.tournamentservice.dto.MatchResultsDTO;
import artur.goz.tournamentservice.models.MatchResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchResultsService {

    private final MatchResultsRepo matchResultsRepo;

    @Autowired
    public MatchResultsService(MatchResultsRepo matchResultsRepo) {
        this.matchResultsRepo = matchResultsRepo;
    }

    public void addMatchResults(MatchResults matchResults) {
        matchResultsRepo.save(matchResults);
    }

    public List<MatchResults> getAllMatchResults() {
        return matchResultsRepo.findAll();
    }

    public Optional<MatchResults> getMatchResultsById(Long id) {
        return matchResultsRepo.findById(id);
    }

    public List<MatchResults> getMatchResultsByTournament(String tournamentName) {
        return matchResultsRepo.findMatchResultsByTournamentName(tournamentName);
    }

    public List<MatchResultsDTO> getMatchResultsDTOByTournamentName(String tournamentName) {
        List<MatchResults> matchResultsList = matchResultsRepo.findMatchResultsByTournamentName(tournamentName);
        return matchResultsList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MatchResultsDTO convertToDTO(MatchResults matchResults) {
        return new MatchResultsDTO(
                matchResults.getTeamHeroesWinner(),
                matchResults.getTeamHeroesLoser(),
                matchResults.getTeamWinnerName(),
                matchResults.getTeamLoserName(),
                matchResults.getEmptyLineUps(),
                matchResults.getTeamWinnerWinrate()
        );
    }


}
