package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.model.Production;
import com.staxrt.tutorial.model.TipasA;
import com.staxrt.tutorial.model.view.IsvedimasSearch;
import com.staxrt.tutorial.repository.ProductionRepository;
import com.staxrt.tutorial.service.ForwardChainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/produkciju-isvedimas")
public class ProductionController {

    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private ForwardChainingService forwardChainingService;

    @GetMapping("/productions")
    public List<Production> getAllProductions() {
        return productionRepository.findAll();
    }

    @PostMapping("/tiesioginis-isvedimas")
    public TipasA getForwardChainingOutput(@RequestBody IsvedimasSearch search) {
        TipasA tipasA = new TipasA();
        tipasA.setDuomenys(forwardChainingService.forwardChainingOutput(search.getIsvestis(), search.getIvestys()));
        return tipasA;
    }
}
