package artur.goz.frontendservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analyze")

public class AnalyzerPage {

    @GetMapping("/getPage")
    public String getPage() {
        return "parser"; // Назва вашого HTML-файлу
    }


}
