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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        HashMap<String, List<Document>> heroesMap = new HashMap<>();

        fillHeroMatchUpsMap(driver, heroesMap);
        HashMap<String, List<HeroMatchUps>> parsedHeroesMap = parseData(heroesMap);
        saveOrAddParsedHeroesData(isUpdate,parsedHeroesMap);
    }

    private void fillHeroMatchUpsMap(WebDriver driver, HashMap<String, List<Document>> heroesMap) {
        for (String hero : dota2Heroes) {
            String finalUrl = BASE_URL + hero;
            driver.get(finalUrl);
            try {
                waitForPageLoad();
                List<WebElement> buttons = driver.findElements(By.cssSelector(".rounded-t-md"));

                List<Document> documentList = new ArrayList<>();
                heroesMap.put(hero, documentList);
                for (int i = 1; i <= Math.min(5, buttons.size() - 1); i++) {
                    waitForTableLoad();
                    buttons.get(i).click();

                    Document doc = Jsoup.parse(Objects.requireNonNull(driver.getPageSource()));
                    documentList.add(doc);
                }
            } catch (NoSuchElementException e) {
                log.error("Element not found for hero: {}", hero, e);
            } catch (Exception e) {
                log.error("Unexpected error with hero: {}", hero, e);
            }
        }
    }

    private void saveOrAddParsedHeroesData(boolean isUpdate, HashMap<String, List<HeroMatchUps>> parsedHeroesMap) {
           if (isUpdate) {
               for (String hero : parsedHeroesMap.keySet()) {
                   heroMatchUpsService.saveAllHeroMatchUpsList(parsedHeroesMap.get(hero));
               }
           } else {
               for (String hero : parsedHeroesMap.keySet()) {
                   heroMatchUpsService.addHeroMatchUps(parsedHeroesMap.get(hero), hero);
               }
           }
    }

    private HashMap<String, List<HeroMatchUps>> parseData(HashMap<String, List<Document>> heroesMap){
        try{
            HashMap<String, List<HeroMatchUps>> heroesMapNew = new HashMap<>();
            heroesMap.keySet().forEach(hero -> {
                List<HeroMatchUps> heroMatchUpsList = D2PTHeroesStatsParser.parseDocuments(heroesMap.get(hero), hero);
                heroesMapNew.put(hero, heroMatchUpsList);
            });
            return heroesMapNew;
        } catch (RuntimeException e) {
            log.error("failed to parse");
            throw new RuntimeException(e.getMessage());
        }
    }


    private void waitForPageLoad() {
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitForTableLoad() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public WebDriver webDriver() {
        try {
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\chromedriver-win64\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            return new ChromeDriver(options);
        } catch (RuntimeException e) {
            throw new RuntimeException("failed to initialize chrome driver", e);
        }
    }
}
