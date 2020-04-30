package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.model.IsvedimoDuomenys;
import com.staxrt.tutorial.model.Production;
import com.staxrt.tutorial.model.TipasA;
import com.staxrt.tutorial.model.view.IsvedimasSearch;
import com.staxrt.tutorial.repository.ProductionRepository;
import com.staxrt.tutorial.service.AtbulinisIsvedimasService;
import com.staxrt.tutorial.service.ForwardChainingService;
import com.staxrt.tutorial.service.IsvedimasSuKainaService;
import com.staxrt.tutorial.service.impl.AtbulinisIsvedimasServiceImpl;
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
    @Autowired
    private AtbulinisIsvedimasService atbulinisIsvedimasService;
    @Autowired
    private IsvedimasSuKainaService isvedimasSuKainaService;

    @GetMapping("/productions")
    public List<Production> getAllProductions() {
        return productionRepository.findAll();
    }

    @PostMapping("/tiesioginis-isvedimas")
    public IsvedimoDuomenys getForwardChainingOutput(@RequestBody IsvedimasSearch search) {
        IsvedimoDuomenys duomenys;
        duomenys = forwardChainingService.forwardChainingOutput(search.getIsvestis(), search.getIvestys());
        return duomenys;
    }

    @PostMapping("/atbulinis-isvedimas")
    public IsvedimoDuomenys getBackwardChainingOutput(@RequestBody IsvedimasSearch search) {
        IsvedimoDuomenys duomenys;
        duomenys = atbulinisIsvedimasService.AtliktiAtbuliniIsvedima(search.getIsvestis(), search.getIvestys());
        return duomenys;
    }

    @PostMapping("/isvedimas-su-kaina")
    public IsvedimoDuomenys getIsvedimasSuKainaOutput(@RequestBody IsvedimasSearch search) {
        IsvedimoDuomenys duomenys;
        duomenys = isvedimasSuKainaService.AtliktiIsvedimaSuKaina(search.getIsvestis(), search.getIvestys());
        return duomenys;
    }
}
