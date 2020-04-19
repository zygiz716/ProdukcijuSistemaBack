package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.model.InfoPaveiksleliui;
import com.staxrt.tutorial.model.Tekstas;
import com.staxrt.tutorial.model.TipasA;
import com.staxrt.tutorial.model.TipasB;
import com.staxrt.tutorial.model.view.GrandinesInfo;
import com.staxrt.tutorial.service.ConverterABService;
import com.staxrt.tutorial.service.PaveikslelisService;
import com.staxrt.tutorial.service.TekstoRedagavimasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/produkciju-vykdymas")
public class GrandinesVykdymasController {

    @Autowired
    private TekstoRedagavimasService tekstoRedagavimasService;

    @Autowired
    private PaveikslelisService paveikslelisService;

    @GetMapping(value = "/teksto-didinimas")
    public Tekstas didintiTeksta(@NotNull String paprastasTekstas) {
        System.out.println(paprastasTekstas);
        Tekstas tekstas = new Tekstas();
        tekstas.setTekstas(tekstoRedagavimasService.padidintiTeksta(paprastasTekstas));
        return tekstas;
    }

    @PostMapping(value = "/teksto-mazinimas")
    public String mazintiTeksta(@RequestBody String paprastasTekstas) {
        System.out.println(paprastasTekstas);
        return tekstoRedagavimasService.sumazintiTeksta(paprastasTekstas);
    }

    @PostMapping(value = "/teksto-sujungimas")
    public String mazintiTeksta(@RequestBody String paprastasTekstas, @RequestBody String tekstas2) {
        System.out.println(paprastasTekstas);
        return tekstoRedagavimasService.sujungtiTeksta(paprastasTekstas, tekstas2);
    }

    @PostMapping(value = "/paveikslelio-sukurimas", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@RequestBody InfoPaveiksleliui info) throws IOException {

        System.out.println(info.getTekstas() + info.getSpalva());
        return paveikslelisService.sukurtiPaveiksleli(info);
    }
}
