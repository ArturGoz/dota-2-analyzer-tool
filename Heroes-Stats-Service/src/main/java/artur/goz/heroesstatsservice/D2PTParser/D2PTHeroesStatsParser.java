package artur.goz.heroesstatsservice.D2PTParser;



import artur.goz.heroesstatsservice.models.HeroMatchUps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Slf4j
public class D2PTHeroesStatsParser {
    public static List<HeroMatchUps> parseDocuments(List<Document> doc, String hero) {
            List<HeroMatchUps> heroMatchUps = new ArrayList<>();
            for (Document document : doc) {
                try {
                    heroMatchUps.addAll(parseDoc(document, hero));
                }
                 catch (RuntimeException e) {
                    log.error("{} problems with parsing", String.valueOf(e));
                }
            }
            return heroMatchUps;
    }

    public static List<HeroMatchUps> parseDoc(Document doc, String hero) {
        List<HeroMatchUps> heroMatchUpsList = new ArrayList<>();

        String hero1Position = heroPositionParser(doc);
        Elements rows = rowParser(doc);

        for (Element row : rows) {
            try {
                String hero2 = enemyHeroParser(row);
                float winrate = winrateParser(row);
                int matchCount = matchCounterParser(row);
                String hero2Position = enemyHeroPositionParser(row);
                HeroMatchUps heroMatchUps =
                        new HeroMatchUps(hero, hero2, winrate, matchCount, hero1Position , hero2Position);
                log.info(String.valueOf(heroMatchUps));
                if(hero1Position != null && hero2Position != null) {
                    heroMatchUpsList.add(heroMatchUps);
                    log.info(String.valueOf(heroMatchUps));
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info("enemy hero error");
            }
        }

        if (heroMatchUpsList.isEmpty())
            throw new RuntimeException("heroMatchUpsList is empty ");

        return heroMatchUpsList;
    }

    private static Elements rowParser(Document doc) {
        Element matchupTable = doc.select("div.overflow-y-scroll.tbody.h-96").first();

        if (matchupTable == null)
            throw new RuntimeException("matchupTable is empty");

        return matchupTable.select("div.overflow-y-scroll.tbody.h-96 > div");
    }

    private static String getPosition(Element roleElement) {
        if (roleElement == null)
            return null;
        String src = roleElement.select("img").attr("src");
        return src.substring(src.lastIndexOf("/") + 1, src.lastIndexOf("."));
    }

    private static int matchCounterParser(Element row) {
        Element matchCountElement = row.select("div").get(4);
        String matchCountText = matchCountElement.text().replace(",", "").trim();
        int matchCount = matchCountText.isEmpty() ? 0 : Integer.parseInt(matchCountText);
        log.info("Parsed data count : " + matchCount);
        return matchCount;
    }

    private static float winrateParser(Element row) {
        float winrate = Float.parseFloat(row.select("div").get(2).text().replace("%", ""));
        log.info("Parsed data winrate : " + winrate);
        return winrate;
    }

    private static String enemyHeroParser(Element row) {
        String enemyHero = row.select("div img.recent_icon").attr("alt");
        log.info("Parsed data enemyHero : " + enemyHero);
        return enemyHero;
    }

    private static String enemyHeroPositionParser(Element row) {
        String enemyHeroPosition = getPosition(row.select("div").get(5));
        log.info("Parsed data enemyHeroPosition : " + enemyHeroPosition);
        return enemyHeroPosition;
    }

    private static String heroPositionParser(Document doc) {

        Element buttonElement = doc.selectFirst(
                "button[class=\"opacity-100 py-1 relative items-center font-medium "
                        + "border-t border-l border-r border-solid rounded-t-md "
                        + "bg-white/20 border-white/20\"]"
        );

        if (buttonElement == null) {
            // Якщо кнопки немає — нічого не парсимо
            return null;
        }

        // 2) У цьому button шукаємо <img> з точно таким самим атрибутом class
        Element imgElement = buttonElement.selectFirst(
                "img[class=\"h-[20px] w-[20px]\"]"
        );
        String heroPosition = getPosition(imgElement);
        log.info("Parsed heroPosition : " + heroPosition);
        return heroPosition;
    }
}
