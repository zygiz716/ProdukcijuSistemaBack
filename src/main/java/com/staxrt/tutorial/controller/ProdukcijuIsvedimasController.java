package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.model.IsvedimoDuomenys;
import com.staxrt.tutorial.model.view.IsvedimasSearch;
import com.staxrt.tutorial.service.AtbulinisIsvedimasService;
import com.staxrt.tutorial.service.TiesioginisIsvedimasService;
import com.staxrt.tutorial.service.IsvedimasSuKainaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produkciju-isvedimas")
public class ProdukcijuIsvedimasController {

    @Autowired
    private TiesioginisIsvedimasService tiesioginisIsvedimasService;
    @Autowired
    private AtbulinisIsvedimasService atbulinisIsvedimasService;
    @Autowired
    private IsvedimasSuKainaService isvedimasSuKainaService;

    @PostMapping("/tiesioginis-isvedimas")
    public IsvedimoDuomenys getForwardChainingOutput(@RequestBody IsvedimasSearch search) {
        IsvedimoDuomenys duomenys;
        duomenys = tiesioginisIsvedimasService.forwardChainingOutput(search.getIsvestis(), search.getIvestys());
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
