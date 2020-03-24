package com.staxrt.tutorial.service;

import com.staxrt.tutorial.model.TipasA;
import com.staxrt.tutorial.model.TipasB;
import com.staxrt.tutorial.model.TipasC;

public interface ConverterABService {
    TipasB convertAtoB(TipasA duomenys);

    TipasC convertAtoC(TipasA duomenys);

    TipasA convertBtoA(TipasB duomenys);
}
