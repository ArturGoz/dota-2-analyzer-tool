package artur.goz.frontendservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class AuthPage {

    @GetMapping("getRegister")
    public String register() {
        return "register";
    }

    @GetMapping("getLogin")
    public String login() {
        return "login";
    }

    @GetMapping("/getAnalyze")
    public String getPage() {
        return "parser";
    }

}
