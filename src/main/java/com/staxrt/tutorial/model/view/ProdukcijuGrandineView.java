package com.staxrt.tutorial.model.view;

import com.staxrt.tutorial.model.Produkcija;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class ProdukcijuGrandineView {
    private Long id;
    private String pavadinimas;
    private List<String> ivestys;
    private String isvestis;
    private List<Produkcija> produkcijos;
}
