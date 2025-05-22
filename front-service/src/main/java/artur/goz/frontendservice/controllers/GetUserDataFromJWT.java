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
public class GetUserDataFromJWT {

        @GetMapping("/user-info-jwt")
        public ResponseEntity<Map<String, String>> getUserInfo(
                @RequestHeader(value = "X-User-Name", required = false) String username,
                @RequestHeader(value = "X-Roles", required = false) String roles) {

            Map<String, String> response = new HashMap<>();
            response.put("username", username);
            response.put("roles", roles);
            return ResponseEntity.ok(response);
        }

    }

