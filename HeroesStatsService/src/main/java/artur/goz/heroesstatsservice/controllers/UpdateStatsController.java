package artur.goz.heroesstatsservice.controllers;


import artur.goz.heroesstatsservice.services.D2PTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class UpdateStatsController {
    @Autowired
    D2PTService d2PTService;

    @PostMapping("/update-data")
    public ResponseEntity<String> updateData(@RequestHeader(value = "X-Roles") String roles){
        if(!roles.matches("ROLE_ADMIN")){
            return  ResponseEntity.badRequest().body("Only ADMIN role.");
        }
        try {
            d2PTService.updateHeroStatsData();
            return ResponseEntity.ok("Successfully updated data");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
