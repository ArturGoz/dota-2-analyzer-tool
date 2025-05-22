package artur.goz.tournamentservice.Repositories;


import artur.goz.tournamentservice.models.MatchResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchResultsRepo extends JpaRepository<MatchResults,Long> {
    @Query(value = "SELECT match_results.* " +
            "FROM match_results " +
            "INNER JOIN tournament " +
            "ON match_results.tournament = tournament.id " +
            "WHERE tournament.name = :tournamentName", nativeQuery = true)
    List<MatchResults> findMatchResultsByTournamentName(@Param("tournamentName") String tournamentName);
}
