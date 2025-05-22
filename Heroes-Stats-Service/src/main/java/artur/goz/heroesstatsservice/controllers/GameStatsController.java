package artur.goz.heroesstatsservice.controllers;

import artur.goz.heroesstatsservice.dto.GameStats;
import artur.goz.heroesstatsservice.dto.HeroesInfo;
import artur.goz.heroesstatsservice.dto.RabbitRequest;
import artur.goz.heroesstatsservice.dto.RemoteResponse;
import artur.goz.heroesstatsservice.rabbitmq.RabbitManager;
import artur.goz.heroesstatsservice.services.D2PTService;
import artur.goz.heroesstatsservice.services.GameStatsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stats")
@Slf4j
public class GameStatsController {
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
    public ResponseEntity<RemoteResponse> getWinrate(@RequestBody String[] allHeroes,
                                                          @RequestHeader(value = "X-User-Name") String username) {
        try {
            Map<String, Object> response = new HashMap<>();

            rabbitManager.decrementUserLimit(RabbitRequest.createRabbitRequest(username));
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

            RemoteResponse remoteResponse = RemoteResponse.create(true,
                    "User successfully obtained data", List.of(response));

            log.info("Radiant winrate: {}", leftSideWinner);
            return ResponseEntity.ok(remoteResponse);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw  new RuntimeException("Error while retrieving data", e);
        }
    }

    @PostMapping("/update-data")
    public ResponseEntity<RemoteResponse> updateData(@RequestHeader(value = "X-Roles") String roles){
        if(!roles.contains("ROLE_ADMIN")){
            throw  new RuntimeException("Unauthorized for updating data");
        }
        try {
            d2PTService.updateHeroStatsData();
            RemoteResponse remoteResponse = RemoteResponse
                    .create(true, "Data successfully updated", null);
            return ResponseEntity.ok(remoteResponse);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw  new RuntimeException("Error while updating data", e);
        }
    }

    @PostMapping("/add-data")
    public ResponseEntity<RemoteResponse> addData(@RequestHeader(value = "X-Roles") String roles){
        if(!roles.contains("ROLE_ADMIN")){
            throw  new RuntimeException("Unauthorized for updating data");
        }
        try {
            d2PTService.addHeroStatsData();
            RemoteResponse remoteResponse = RemoteResponse
                    .create(true, "Data successfully added", null);
            return ResponseEntity.ok(remoteResponse);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw  new RuntimeException("Error while adding data", e);
        }
    }

}
