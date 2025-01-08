package artur.goz.userservice.controlles;

import artur.goz.userservice.dto.LoginDto;
import artur.goz.userservice.dto.RegisterDto;
import artur.goz.userservice.securityConfigs.JWTGenerator;
import artur.goz.userservice.services.MyUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private MyUserService myUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGeneretor;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user",new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getName(),
                        loginDto.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGeneretor.generateJWT(authentication);

        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/parser";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user",new RegisterDto());
        return "registerForm";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterDto registerDto, BindingResult bindingResult,
                               Model model) {
        // Перевірка загальних помилок валідації
        if (bindingResult.hasErrors()) {
            return "registerForm";
        }

        // Перевірка рівності password та confirmPassword
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            model.addAttribute("errorConfirmPassword", "Passwords do not match");
            return "registerForm";
        }

        // Перевірка, чи користувач вже існує
        if (myUserService.checkUser(registerDto.getName())) {
            model.addAttribute("errorMessage", "User already exists");
            return "registerForm";
        }

        // Реєстрація користувача
        myUserService.registerUser(registerDto);
        model.addAttribute("message", "User registered successfully");
        return "login";
    }




}
