package artur.goz.tournamentservice.DLTVParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

import java.util.HashSet;
import java.util.Set;

public class ParseTourDocuments {

    private static final Logger log = LogManager.getLogger(ParseTourDocuments.class);

    public static Set<Document> getParsedDocument(WebDriver driver, String tournamentUrl) {
        Set<String> urls = TourMatchesUrls.getTourMatchesUrls(driver,tournamentUrl);
        int num = urls.size();
        if(num == 0)
            throw new RuntimeException("no matches to parse");
        try {
            Thread.sleep(2000);
            Set<Document> documents = new HashSet<>();

            for (String url : urls) {
                log.info("{} {}", num, url);
                num--;
                driver.get(url);
                Thread.sleep(3000);
                String updatedHtml = driver.getPageSource();
                Document doc = Jsoup.parse(updatedHtml);
                documents.add(doc);
            }
            return documents;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
