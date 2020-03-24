package com.staxrt.tutorial.service.impl;

import com.staxrt.tutorial.model.ProdukcijuGrandine;
import com.staxrt.tutorial.model.view.ProdukcijuGrandineView;
import com.staxrt.tutorial.repository.ProdukcijuGrandineRepository;
import com.staxrt.tutorial.service.ProdukcijuGrandineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdukcijuGrandineServiceImpl implements ProdukcijuGrandineService {
    @Autowired
    private ProdukcijuGrandineRepository produkcijuGrandineRepository;

    @Override
    public void save(ProdukcijuGrandineView view) {
        produkcijuGrandineRepository.save(this.mapGrandine(view));
    }

    @Override
    public void atnaujinti(ProdukcijuGrandineView view) {
        produkcijuGrandineRepository.save(this.mapGrandine(view));
    }

    @Override
    public void trinti(Long id) {
        produkcijuGrandineRepository.deleteById(id);
    }

    @Override
    public ProdukcijuGrandine getGrandine(Long id) {
        return produkcijuGrandineRepository.findById(id).orElseThrow();
    }

    @Override
    public List<ProdukcijuGrandine> getGrandines() {
        return produkcijuGrandineRepository.findAll();
    }

    private ProdukcijuGrandine mapGrandine(ProdukcijuGrandineView view) {
        ProdukcijuGrandine grandine = new ProdukcijuGrandine();
        if (view.getId() != null) {
            grandine.setId(view.getId());
        }
        grandine.setPavadinimas(view.getPavadinimas());
        grandine.setProdukcijos(view.getProdukcijos());
        return grandine;
    }
}
