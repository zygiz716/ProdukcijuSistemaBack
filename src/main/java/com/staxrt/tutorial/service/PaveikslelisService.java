package com.staxrt.tutorial.service;

import com.staxrt.tutorial.model.InfoPaveiksleliui;

import java.io.IOException;

public interface PaveikslelisService {
    byte[] sukurtiPaveiksleli(InfoPaveiksleliui info) throws IOException;
}
