package artur.goz.tournamentservice.Repositories;


import artur.goz.tournamentservice.models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TournamentRepo extends JpaRepository<Tournament,Long> {
    Optional<Tournament> findByName(String name);

    @Query("SELECT t.name FROM Tournament t")
    List<String> findAllTournamentNames();
}

