package com.staxrt.tutorial.service.impl;

import com.staxrt.tutorial.service.TekstoRedagavimasService;
import org.springframework.stereotype.Service;

@Service
public class TekstoRedagavimasServiceImpl implements TekstoRedagavimasService {

    @Override
    public String padidintiTeksta(String tekstas){
        return tekstas.toUpperCase();
    }

    @Override
    public String sumazintiTeksta(String tekstas){
        return tekstas.toLowerCase();
    }

    @Override
    public String sujungtiTeksta(String tekstas1, String tekstas2){
        return tekstas1 + tekstas2;
    }
}
