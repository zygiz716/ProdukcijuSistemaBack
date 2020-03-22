package com.staxrt.tutorial.model.view;

import com.staxrt.tutorial.model.Produkcija;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class ProdukcijuGrandineView {
    private Long id;
    private String pavadinimas;
    private List<Produkcija> produkcijos;
}
