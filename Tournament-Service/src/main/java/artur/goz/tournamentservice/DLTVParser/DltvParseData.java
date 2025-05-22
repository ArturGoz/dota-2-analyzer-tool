package artur.goz.tournamentservice.DLTVParser;

import artur.goz.tournamentservice.dto.GameStats;
import artur.goz.tournamentservice.dto.HeroesInfo;
import artur.goz.tournamentservice.models.MatchResults;
import artur.goz.tournamentservice.models.Tournament;
import artur.goz.tournamentservice.rabbitmq.MessageSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DltvParseData implements DLTVDocParser{
    private static final Logger log = LogManager.getLogger(DltvParseData.class);

    @Autowired
    private MessageSender messageSender;

    @Override
    public List<MatchResults> parseDoc(Document doc, Tournament tournament) {
        List<MatchResults> matchResultsList = new ArrayList<>();
        try {
            Elements matches = doc.select("div.map__finished-v2");
            for (Element match : matches) {
                MatchResults matchResults = parseMatchesElements(match, tournament);
                matchResultsList.add(matchResults);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error in parsing document");
        }
        return matchResultsList;
    }

    private MatchResults parseMatchesElements(Element match, Tournament tournament) {
        // Логіка отримання назв команд, героїв, обчислення winrate і створення MatchResults
        String[] WinnerHeroes = new String[5];
        String[] LoserHeroes = new String[5];

        // Знайти блоки з командами та героями
        Elements teamElements = match.select("div.map__finished-v2__scores div.team");
        Elements heroElements = match.select("div.map__finished-v2__pick div.heroes");

        Element firstTeamElement = teamElements.get(0);
        Element secondTeamElement = teamElements.get(1);

        String firstTeamName = firstTeamElement.selectFirst("span.name").text();
        String secondTeamName = secondTeamElement.selectFirst("span.name").text();

        // Перевірити, яка команда виграла
        boolean firstTeamWon = firstTeamElement.selectFirst("div.winner") != null;

        // Витягти героїв переможця
        Elements winningHeroes = firstTeamWon ? heroElements.get(0).select("div.heroes__player div.pick") : heroElements.get(1).select("div.heroes__player div.pick");
        Elements losingHeroes = !firstTeamWon ? heroElements.get(0).select("div.heroes__player div.pick") : heroElements.get(1).select("div.heroes__player div.pick");

        log.info("Команда-переможець: {}", firstTeamWon ? firstTeamName : secondTeamName);

        for(int i =0; i< winningHeroes.size() && i < losingHeroes.size() ;i++){
            WinnerHeroes[i] = winningHeroes.get(i).attr("data-tippy-content");
            LoserHeroes[i] = losingHeroes.get(i).attr("data-tippy-content");
        }

        GameStats gameStats = messageSender.getGameStats(new HeroesInfo(WinnerHeroes,LoserHeroes));

        float teamWinnerWinrate = gameStats.getLeftSideWinner();

        MatchResults matchResults = new MatchResults();
        matchResults.setTeamWinnerName(firstTeamWon ? firstTeamName : secondTeamName);
        matchResults.setTeamHeroesWinner(WinnerHeroes);
        matchResults.setTeamLoserName(!firstTeamWon ? firstTeamName : secondTeamName);
        matchResults.setTeamHeroesLoser(LoserHeroes);
        matchResults.setTeamWinnerWinrate(teamWinnerWinrate);
        matchResults.setEmptyLineUps(gameStats.getNoWinrateForHeroesCounter());
        matchResults.setTournament(tournament);
        return matchResults;
    }
}
