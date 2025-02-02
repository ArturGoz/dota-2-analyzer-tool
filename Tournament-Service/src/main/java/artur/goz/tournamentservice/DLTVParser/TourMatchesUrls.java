package artur.goz.tournamentservice.DLTVParser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.util.HashSet;
import java.util.Set;

public class TourMatchesUrls {
    public static Set<String> getTourMatchesUrls(WebDriver driver, String tournamentUrl) {
        driver.get(tournamentUrl);
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);
        Elements rows = doc.select("div.row");
        Set<String> matchesSet = new HashSet<>();
        for (Element row : rows) {
            // Вибір всіх <div class="col-xl-4">
            Elements cols = row.select("div.col-xl-4 div.table div.table__body a.table__body-row");
            // Додавання всіх знайдених URL до списку
            for (Element col : cols) {
                matchesSet.add(col.attr("href"));
            }
        }
        return matchesSet;
    }
}
