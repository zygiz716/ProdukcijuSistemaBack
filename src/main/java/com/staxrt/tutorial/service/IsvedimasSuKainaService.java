package com.staxrt.tutorial.service;

import com.staxrt.tutorial.model.IsvedimoDuomenys;

import java.util.List;

public interface IsvedimasSuKainaService {
    IsvedimoDuomenys AtliktiIsvedimaSuKaina(String goal1, List<String> facts1);
}
