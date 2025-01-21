package artur.goz.frontendservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/get")
public class getUserData {
    @RestController
    @RequestMapping("/api")
    public class UserController {

        @GetMapping("/user-info")
        public ResponseEntity<Map<String, String>> getUserInfo(
                @RequestHeader(value = "X-User-Name", required = false) String username,
                @RequestHeader(value = "X-Roles", required = false) String roles) {

            if (username == null || roles == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing headers or invalid token"));
            }

            Map<String, String> response = new HashMap<>();
            response.put("username", username);
            response.put("roles", roles);
            return ResponseEntity.ok(response);
        }
    }

}
