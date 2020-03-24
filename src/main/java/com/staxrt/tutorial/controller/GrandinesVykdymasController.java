package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.model.TipasA;
import com.staxrt.tutorial.model.TipasB;
import com.staxrt.tutorial.model.view.GrandinesInfo;
import com.staxrt.tutorial.model.view.ProdukcijaView;
import com.staxrt.tutorial.repository.ProductionRepository;
import com.staxrt.tutorial.service.ConverterABService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produkciju-vykdymas")
public class GrandinesVykdymasController {

    @Autowired
    private ConverterABService converterABService;

    @PostMapping
    public TipasB vykdytiGrandine(@RequestBody GrandinesInfo info) {
        System.out.println(info);
        //produkcijaService.save(produkcija);
        if (info.getIvestis().equals("A") && info.getIsvestis().equals("B")) {
            TipasA a = new TipasA();
            a.setDuomenys(info.getIvestiesDuomenys());
            return converterABService.convertAtoB(a);
        }

/*        if (info.getIvestis().equals("A") && info.getIsvestis().equals("C")) {
            TipasA a = new TipasA();
            a.setDuomenys(info.getIvestiesDuomenys());
            return converterABService.convertAtoC(a);
        }*/

        return null;
    }
}
