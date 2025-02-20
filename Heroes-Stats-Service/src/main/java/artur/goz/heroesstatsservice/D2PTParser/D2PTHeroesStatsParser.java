package artur.goz.heroesstatsservice.D2PTParser;



import artur.goz.heroesstatsservice.models.HeroMatchUps;
import lombok.Getter;
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
public class D2PTHeroesStatsParser {

    private static final Logger log = LogManager.getLogger(D2PTHeroesStatsParser.class);

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

                if(hero1Position != null && hero2Position != null) {
                    heroMatchUpsList.add(heroMatchUps);
                    log.info(heroMatchUps);
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

    private static String getPosition(Element roleElement) {
        if (roleElement == null)
            return null;
        String src = roleElement.select("img").attr("src");
        return src.substring(src.lastIndexOf("/") + 1, src.lastIndexOf("."));
    }

    private static int matchCounterParser(Element row) {
        Element matchCountElement = row.select("div").get(3);
        String matchCountText = matchCountElement.text().replace(",", "").trim();
        return matchCountText.isEmpty() ? 0 : Integer.parseInt(matchCountText);
    }

    private static float winrateParser(Element row) {
        return Float.parseFloat(row.select("div").get(2).text().replace("%", ""));
    }

    private static String enemyHeroParser(Element row) {
        return row.select("div img.recent_icon").attr("alt");
    }

    private static String enemyHeroPositionParser(Element row) {
        return getPosition(row.select("div").get(4));
    }

    private static Elements rowParser(Document doc) {
        Element matchupTable = doc.select("div.overflow-y-scroll.tbody.h-96").first();
        return matchupTable.select("div.overflow-y-scroll.tbody.h-96 > div");
    }

    private static String heroPositionParser(Document doc) {
        return getPosition(doc.select("div.mt-6.flex.gap-2 > div.d-box-1.p-2.px-4.flex.gap-1.items-center").first());
    }
}
