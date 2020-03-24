package com.staxrt.tutorial.service;

import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.model.ProdukcijuGrandine;
import com.staxrt.tutorial.model.view.ProdukcijaView;
import com.staxrt.tutorial.model.view.ProdukcijuGrandineView;

import java.util.List;

public interface ProdukcijuGrandineService {
    void save(ProdukcijuGrandineView produkcija);

    void trinti(Long id);

    void atnaujinti(ProdukcijuGrandineView grandine);

    List<ProdukcijuGrandine> getGrandines();

    ProdukcijuGrandine getGrandine(Long id);
}
