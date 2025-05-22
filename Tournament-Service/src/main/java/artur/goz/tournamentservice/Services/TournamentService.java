package artur.goz.tournamentservice.Services;


import artur.goz.tournamentservice.Repositories.TournamentRepo;
import artur.goz.tournamentservice.models.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepo tournamentRepo;

    // Створення нового турніру
    public Tournament addTournament(Tournament tournament) {
        return tournamentRepo.save(tournament);
    }

    public List<String> getAllTournamentNames() {
        List<String> tournamentNames = tournamentRepo.findAllTournamentNames();
        Collections.reverse(tournamentNames);
        return tournamentNames;
    }
}

