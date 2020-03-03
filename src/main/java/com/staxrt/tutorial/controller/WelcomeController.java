package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.model.Production;
import com.staxrt.tutorial.repository.ProductionRepository;
import com.staxrt.tutorial.service.ForwardChainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class WelcomeController {

    @Autowired
    private ForwardChainingService forwardChainingService;

    @Autowired
    private ProductionRepository productionRepository;

    // inject via application.properties
    @Value("LABAS VAKARAS")
    private String message;

    private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);
        List<String> facts = new ArrayList<>();
        List<Production> productions = new ArrayList<>();
        facts.add("A");
        model.addAttribute("forwardChaining", forwardChainingService.forwardChainingOutput("B", facts));
        model.addAttribute("productions", productionRepository.findAll());

        return "welcome"; //view
    }

    // /hello?name=kotlin
    @GetMapping("/hello")
    public String mainWithParam(
            @RequestParam(name = "name", required = false, defaultValue = "")
                    String name, Model model) {

        model.addAttribute("message", name);

        return "welcome"; //view
    }

}