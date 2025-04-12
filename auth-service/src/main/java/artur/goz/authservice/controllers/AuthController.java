package artur.goz.authservice.controllers;

import artur.goz.authservice.dto.*;
import artur.goz.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<RemoteResponse> loginUser(@RequestBody LoginDTO loginDto) {
        JWTResponse jwtResponse = authService.login(loginDto);
        RemoteResponse remoteResponse = RemoteResponse.create(
                true, "User successfully authenticated", List.of(jwtResponse));
        return new ResponseEntity<>(remoteResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RemoteResponse> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO userDTO1 = authService.register(userDTO);
        RemoteResponse remoteResponse = RemoteResponse.create(
                true, "User successfully registered", List.of(userDTO1));
        return new ResponseEntity<>(remoteResponse, HttpStatus.OK);
    }
}
