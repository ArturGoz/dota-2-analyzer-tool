package artur.goz.userservice.controlles;

import artur.goz.userservice.securityConfigs.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetUserFromJwtController {
    @Autowired
    JWTGenerator jwtGenerator;

    @PostMapping("/getUserFromToken")
    public ResponseEntity<?> getUserFromToken(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Обрізаємо префікс "Bearer "
        }
        if (jwtGenerator.validateJWT(token)) {
            String username = jwtGenerator.getUsernameFromJWT(token);
            return ResponseEntity.ok(Map.of("username", username));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
