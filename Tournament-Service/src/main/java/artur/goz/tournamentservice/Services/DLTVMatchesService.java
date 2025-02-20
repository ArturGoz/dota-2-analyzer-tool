package artur.goz.tournamentservice.Services;

import artur.goz.tournamentservice.DLTVParser.DLTVDocParser;
import artur.goz.tournamentservice.DLTVParser.ParseTourDocuments;
import artur.goz.tournamentservice.models.MatchResults;
import artur.goz.tournamentservice.models.Tournament;
import org.jsoup.nodes.Document;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DLTVMatchesService {
    private static final Logger logger = LoggerFactory.getLogger(DLTVMatchesService.class);
    @Autowired
    DLTVDocParser dltvDocParser;
    @Autowired
    TournamentService tournamentService;

    public void addMatches(Tournament tournament, String tournamentUrl) {
        logger.info("початок додавання матчів");
        logger.info("настройка драйвера для гугла");
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NONE); // Установлюємо стратегію завантаження

        WebDriver driver = null;
        try {
            driver = new ChromeDriver(options);
            driver.manage().deleteAllCookies();
            logger.info("настройка завершена та переходимо до парсингу");
            Set<Document> documents = ParseTourDocuments.getParsedDocument(driver, tournamentUrl);

            for (Document doc : documents) {
                List<MatchResults> parsedDocument = dltvDocParser.parseDoc(doc, tournament);
                tournament.getMatchResultsList().addAll(parsedDocument);
                tournamentService.addTournament(tournament);
            }
            logger.info("data is saved successfully");
        }  finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

}
