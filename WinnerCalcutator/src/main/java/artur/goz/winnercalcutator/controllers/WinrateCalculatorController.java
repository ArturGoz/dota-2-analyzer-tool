package artur.goz.winnercalcutator.controllers;

import artur.goz.winnercalcutator.dto.GameStats;
import artur.goz.winnercalcutator.dto.HeroesInfo;
import artur.goz.winnercalcutator.rabbitmq.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/analyze")
public class WinrateCalculatorController {
    private static final Logger logger = LoggerFactory.getLogger(WinrateCalculatorController.class);

    @Autowired
    MessageSender messageSender;

    @PostMapping("/game")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getWinrate(@RequestBody String[] allHeroes,
                                                          @RequestHeader(value = "X-User-Name") String username) {
        Map<String, Object> response = new HashMap<>();

            messageSender.decrementUserLimit(username);


            MyUser user = myUserRepo.findByName(userDetails.getUsername()).orElseThrow();

            // Перевірка чи залишився ліміт
            if (user.getMonthlyLimit() > 0) {
                user.setMonthlyLimit(user.getMonthlyLimit() - 1); // Зменшуємо ліміт
                myUserRepo.save(user);
            } else {
                response.put("error", "Monthly limit exceeded!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        try {
            GameStats gameStats = messageSender.sendMessage(new HeroesInfo
                    (
                            Arrays.copyOfRange(allHeroes, 0, 5),
                            Arrays.copyOfRange(allHeroes, 5, 10)
                    ));

            float leftSideWinner = gameStats.getLeftSideWinner();
            float rightSideWinner = (leftSideWinner != -1) ? (100.0f - leftSideWinner) : -1;


            response.put("radiantWinrate", leftSideWinner);
            response.put("direWinrate", rightSideWinner);
            response.put("emptyLineUps", gameStats.getNoWinrateForHeroesCounter());
            response.put("Stats", gameStats.getHeroStatsMap());
            logger.info("Radiant winrate: {}", leftSideWinner);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred: {}", e.getMessage());
            response.put("error", "An unexpected error occurred!");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
