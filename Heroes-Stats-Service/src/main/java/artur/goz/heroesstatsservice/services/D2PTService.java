package artur.goz.heroesstatsservice.services;


import artur.goz.heroesstatsservice.D2PTParser.D2PTHeroesStatsParser;
import artur.goz.heroesstatsservice.models.HeroMatchUps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import static artur.goz.heroesstatsservice.D2PTParser.Heroes.dota2Heroes;


@Service
public class D2PTService {

    private static final Logger log = LogManager.getLogger(D2PTService.class);
    private final static String url = "https://dota2protracker.com/hero/";
    @Autowired
    private HeroMatchUpsService heroMatchUpsService;

    public  void updateHeroStatsData() {
        heroMatchUpsService.truncateTable();
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        // turn off loading whole web page
       //options.setPageLoadStrategy(PageLoadStrategy.NONE); //!!

        WebDriver driver = new ChromeDriver(options);
        for (String hero : dota2Heroes) {
            String finalUrl = url + hero;
            driver.get(finalUrl);
            try {
                Thread.sleep(3000);
                List<WebElement> buttons = driver.findElements(By.cssSelector(".rounded-t-md"));

                for (int i = 1; i <= Math.min(5, buttons.size()); i++) {
                    buttons.get(i).click();
                    try {
                        Thread.sleep(1500);
                        String updatedHtml = driver.getPageSource();
                        Document doc = Jsoup.parse(updatedHtml);
                        List<HeroMatchUps> heroMatchUpsList = D2PTHeroesStatsParser.parseDoc(doc,finalUrl);
                        heroMatchUpsService.addMatchUpList(heroMatchUpsList);
                    } catch (InterruptedException  | NullPointerException e) {
                        log.warn("exception with hero: {}", hero);
                    }
                }
            } catch (Exception e) {
                log.error("error with hero: {}, {}", hero, e.getMessage());
            }
        }
        driver.quit();
    }

}
