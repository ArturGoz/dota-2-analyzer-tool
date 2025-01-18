package artur.goz.authservice.controllers;

import artur.goz.authservice.dto.JWTResponse;
import artur.goz.authservice.dto.LoginDto;
import artur.goz.authservice.dto.MyUserVO;
import artur.goz.authservice.dto.RegisterDto;
import artur.goz.authservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> loginUser(@RequestBody LoginDto loginDto) {
        try {
            JWTResponse jwtResponse = authService.login(loginDto);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            String errorMessage = "Login failed: " + e.getMessage();
            return null;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<MyUserVO> registerUser(@RequestBody RegisterDto registerDto) {
        try {
            return ResponseEntity.ok(authService.register(registerDto));
        } catch (RuntimeException e) {
            String errorMessage = "Register failed: " + e.getMessage();
            return null;
        }
    }

    @PostMapping("/sayHello")
    public ResponseEntity<String> registerUser() {
        return ResponseEntity.ok("Hello World!");
    }

}
