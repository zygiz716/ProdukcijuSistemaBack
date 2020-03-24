package com.staxrt.tutorial.service.impl.converters;

import com.staxrt.tutorial.model.TipasA;
import com.staxrt.tutorial.model.TipasB;
import com.staxrt.tutorial.model.TipasC;
import com.staxrt.tutorial.service.ConverterABService;
import org.springframework.stereotype.Service;

@Service
public class ConverterABServiceImpl implements ConverterABService {

    @Override
    public TipasA convertBtoA(TipasB duomenys) {
        TipasA a = new TipasA();
        a.setDuomenys(duomenys.getDuomenys().toUpperCase());
        return a;
    }

    @Override
    public TipasB convertAtoB(TipasA duomenys) {
        TipasB b = new TipasB();
        b.setDuomenys(duomenys.getDuomenys().toUpperCase());
        return b;
    }

    @Override
    public TipasC convertAtoC(TipasA duomenys) {
        TipasC c = new TipasC();
        c.setDuomenys(duomenys.getDuomenys().toLowerCase());
        return c;
    }
}
