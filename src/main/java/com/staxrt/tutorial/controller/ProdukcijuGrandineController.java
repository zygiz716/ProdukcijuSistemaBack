package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.model.ProdukcijuGrandine;
import com.staxrt.tutorial.model.view.ProdukcijaView;
import com.staxrt.tutorial.model.view.ProdukcijuGrandineView;
import com.staxrt.tutorial.service.ProdukcijaService;
import com.staxrt.tutorial.service.ProdukcijuGrandineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/produkciju-grandines")
public class ProdukcijuGrandineController {

    @Autowired
    private ProdukcijuGrandineService produkcijuGrandineService;

    @GetMapping
    public List<ProdukcijuGrandine> getGrandines() {
        return produkcijuGrandineService.getGrandines();
    }

    @GetMapping("/{id}")
    public ProdukcijuGrandine gautiProdukcija(@PathVariable("id") Long id) {
        return produkcijuGrandineService.getGrandine(id);
    }

    @PostMapping("/kurti-nauja")
    public long kurtiProdukcija(@RequestBody ProdukcijuGrandineView grandine) {
        produkcijuGrandineService.save(grandine);
        return 1;
    }

    @DeleteMapping("/trinti/{id}")
    public long trintiProdukcija(@PathVariable("id") Long id) {
        produkcijuGrandineService.trinti(id);
        return 1;
    }

    @PatchMapping("/atnaujinti")
    public long atnaujintiProdukcija(@RequestBody ProdukcijuGrandineView grandine) {
        produkcijuGrandineService.atnaujinti(grandine);
        return 1;
    }
}
