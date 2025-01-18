/*
package artur.goz.userservice.controlles;

import artur.goz.userservice.dto.LoginDto;
import artur.goz.userservice.dto.RegisterDto;
import artur.goz.userservice.securityConfigs.JWTGenerator;
import artur.goz.userservice.services.MyUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private MyUserService myUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGeneretor;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getName(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Генерація JWT токена
        String token = jwtGeneretor.generateJWT(authentication);
        // Додавання токена у заголовок
        response.setHeader("Authorization", "Bearer " + token);
        return ResponseEntity.ok("Login successful");
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDto registerDto, BindingResult bindingResult,
                               Model model) {
        // Перевірка загальних помилок валідації
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }
        // Перевірка, чи користувач вже існує
        if (myUserService.checkUser(registerDto.getName())) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        // Перевірка рівності password та confirmPassword
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        myUserService.registerUser(registerDto);
        return ResponseEntity.ok("User registered successfully");
        //redirect to login page
    }
}
*/
