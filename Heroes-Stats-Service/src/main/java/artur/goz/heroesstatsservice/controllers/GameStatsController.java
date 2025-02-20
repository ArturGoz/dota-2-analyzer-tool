package artur.goz.heroesstatsservice.controllers;

import artur.goz.heroesstatsservice.dto.GameStats;
import artur.goz.heroesstatsservice.dto.HeroesInfo;
import artur.goz.heroesstatsservice.rabbitmq.RabbitManager;
import artur.goz.heroesstatsservice.services.D2PTService;
import artur.goz.heroesstatsservice.services.GameStatsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
@RequestMapping("/stats")
public class GameStatsController {
    private static final Logger logger = LoggerFactory.getLogger(GameStatsController.class);

    RabbitManager rabbitManager;
    GameStatsService gameStatsService;
    D2PTService d2PTService;

    @Autowired
    public GameStatsController(RabbitManager rabbitManager, GameStatsService gameStatsService, D2PTService d2PTService) {
        this.rabbitManager = rabbitManager;
        this.gameStatsService = gameStatsService;
        this.d2PTService = d2PTService;
    }

    @PostMapping("/game")
    public ResponseEntity<Map<String, Object>> getWinrate(@RequestBody String[] allHeroes,
                                                          @RequestHeader(value = "X-User-Name") String username) {
        Map<String, Object> response = new HashMap<>();

        try {
            rabbitManager.decrementUserLimit(username);
            GameStats gameStats = gameStatsService.getGameStats(new HeroesInfo
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

    @PostMapping("/update-data")
    public ResponseEntity<String> updateData(@RequestHeader(value = "X-Roles") String roles){
        if(!roles.contains("ROLE_ADMIN")){
            return  ResponseEntity.badRequest().body("Only ADMIN role.");
        }
        try {
            d2PTService.updateHeroStatsData();
            return ResponseEntity.ok("Successfully updated data");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add-data")
    public ResponseEntity<String> addData(@RequestHeader(value = "X-Roles") String roles){
        if(!roles.contains("ROLE_ADMIN")){
            logger.error("Roles is not ADMIN{}", roles);
            return  ResponseEntity.badRequest().body("Only ADMIN role.");
        }
        try {
            d2PTService.addHeroStatsData();
            return ResponseEntity.ok("Successfully updated data");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
