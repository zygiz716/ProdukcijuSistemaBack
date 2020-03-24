package com.staxrt.tutorial.service;

import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.model.view.ProdukcijaView;

import java.util.List;

public interface ProdukcijaService {

    void save(ProdukcijaView produkcija);

    void trinti(Long id);

    void atnaujinti(ProdukcijaView produkcija);

    List<Produkcija> getProdukcijos();
}
