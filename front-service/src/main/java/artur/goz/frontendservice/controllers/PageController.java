package artur.goz.frontendservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {

    @GetMapping("getRegister")
    public String register() {
        return "register";
    }

    @GetMapping("getLogin")
    public String login() {
        return "login";
    }

    @GetMapping("/getAnalyze")
    public String getAnalyzerPage() {
        return "parser";
    }

    @GetMapping("/getProfile")
    public String getProfilePage() {
        return "profile";
    }

    @GetMapping("/getResults")
    public String getResultsPage() {
        return "results";
    }

    @GetMapping("/getAdmin")
    public String getAdminPage() {
        return "admin";
    }
}
