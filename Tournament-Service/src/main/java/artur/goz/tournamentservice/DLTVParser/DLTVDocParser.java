package artur.goz.tournamentservice.DLTVParser;



import artur.goz.tournamentservice.models.MatchResults;
import artur.goz.tournamentservice.models.Tournament;
import org.jsoup.nodes.Document;

import java.util.List;

public interface DLTVDocParser {
    List<MatchResults> parseDoc(Document doc, Tournament tournament);
}

