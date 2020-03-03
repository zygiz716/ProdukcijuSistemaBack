package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.model.Production;
import com.staxrt.tutorial.repository.ProductionRepository;
import com.staxrt.tutorial.service.ForwardChainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/productions")
public class ProductionController {

    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private ForwardChainingService forwardChainingService;

    @GetMapping("/productions")
    public List<Production> getAllProductions() {
        return productionRepository.findAll();
    }

    @GetMapping("/forward-chaining/{goal}/{fact}")
    public String getForwardChainingOutput(@PathVariable(value = "goal") String goal, @PathVariable(value = "fact") String fact) {
        List<String> facts = new ArrayList<>();
        facts.add(fact);
        return forwardChainingService.forwardChainingOutput(goal, facts);
    }
}
