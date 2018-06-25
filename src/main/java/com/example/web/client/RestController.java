package com.example.web.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RestController {

    @GetMapping({"/rest"})
    public String index(Model model) {
        model.addAttribute("name", "World");
        return "rest";
    }
}
