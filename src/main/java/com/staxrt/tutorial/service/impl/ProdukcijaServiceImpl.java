package com.staxrt.tutorial.service.impl;

import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.model.view.ProdukcijaView;
import com.staxrt.tutorial.repository.ProdukcijaRepository;
import com.staxrt.tutorial.service.ProdukcijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdukcijaServiceImpl implements ProdukcijaService {

    @Autowired
    private ProdukcijaRepository produkcijaRepository;

    @Override
    public void save(ProdukcijaView produkcija) {
        produkcijaRepository.save(this.mapProdukcijaView(produkcija));
    }

    @Override
    public void atnaujinti(ProdukcijaView produkcija) {
        produkcijaRepository.save(this.mapProdukcijaView(produkcija));
    }

    @Override
    public void trinti(Long id) {
        produkcijaRepository.deleteById(id);
    }

    @Override
    public List<Produkcija> getProdukcijos() {
        return produkcijaRepository.findAll();
    }

    private Produkcija mapProdukcijaView(ProdukcijaView view) {
        Produkcija produkcija = new Produkcija();
        if (view.getId() != null) {
            produkcija.setId(view.getId());
        }
        produkcija.setPavadinimas(view.getPavadinimas());
        produkcija.setIsvestis(view.getIsvestis());
        produkcija.setIvestys(view.getIvestys());
        return produkcija;
    }
}
