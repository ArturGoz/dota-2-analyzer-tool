package artur.goz.heroesstatsservice.services;


import artur.goz.heroesstatsservice.D2PTParser.D2PTHeroesStatsParser;
import artur.goz.heroesstatsservice.models.HeroMatchUps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

import static artur.goz.heroesstatsservice.D2PTParser.Heroes.dota2Heroes;


@Service
public class D2PTService {

    private static final Logger log = LogManager.getLogger(D2PTService.class);
    private static final String BASE_URL = "https://dota2protracker.com/hero/";

    private final HeroMatchUpsService heroMatchUpsService;

    @Autowired
    public D2PTService(HeroMatchUpsService heroMatchUpsService) {
        this.heroMatchUpsService = heroMatchUpsService;
    }

    public void updateHeroStatsData() {
        heroMatchUpsService.truncateTable();
        processHeroStatsData(true);
    }

    public void addHeroStatsData() {
        processHeroStatsData(false);
    }

    private void processHeroStatsData(boolean isUpdate) {
        WebDriver driver = webDriver();
        for (String hero : dota2Heroes) {
            String finalUrl = BASE_URL + hero;
            driver.get(finalUrl);

            try {
                waitForPageLoad(driver);
                List<WebElement> buttons = driver.findElements(By.cssSelector(".rounded-t-md"));

                for (int i = 0; i < Math.min(5, buttons.size()); i++) {  // Corrected index to start at 0
                    buttons.get(i).click();
                    parseAndSaveHeroData(finalUrl, isUpdate, driver);
                }

            } catch (NoSuchElementException e) {
                log.error("Element not found for hero: {}", hero, e);
            } catch (Exception e) {
                log.error("Unexpected error with hero: {}", hero, e);
            }
        }
    }

    private void parseAndSaveHeroData(String url, boolean isUpdate, WebDriver driver) {
        try {
            waitForPageLoad(driver);
            Document doc = Jsoup.parse(driver.getPageSource());
            List<HeroMatchUps> heroMatchUpsList = D2PTHeroesStatsParser.parseDoc(doc, url);
            if (isUpdate) {
                heroMatchUpsService.updateHeroMatchUpsList(heroMatchUpsList);
            } else {
                heroMatchUpsService.addHeroMatchUps(heroMatchUpsList);
            }
        } catch (Exception e) {
            log.warn("Error parsing hero data: {}", e.getMessage());
        }
    }

    private void waitForPageLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".rounded-t-md")));
    }

    public WebDriver webDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        return new ChromeDriver(options);
    }
}
