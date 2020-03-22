package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.model.view.ProdukcijaView;
import com.staxrt.tutorial.service.ProdukcijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/produkcijos")
public class ProdukcijaController {

    @Autowired
    private ProdukcijaService produkcijaService;

    @GetMapping
    public List<Produkcija> getProdukcijos() {
        return produkcijaService.getProdukcijos();
    }

    @PostMapping("/kurti-nauja")
    public long kurtiProdukcija(@RequestBody ProdukcijaView produkcija) {
        produkcijaService.save(produkcija);
        return 1;
    }

    @DeleteMapping("/trinti/{id}")
    public long trintiProdukcija(@PathVariable("id") long id) {
        produkcijaService.trinti(id);
        return 1;
    }

    @PatchMapping("/atnaujinti")
    public long atnaujintiProdukcija(@RequestBody ProdukcijaView produkcija) {
        produkcijaService.atnaujinti(produkcija);
        return 1;
    }
}
