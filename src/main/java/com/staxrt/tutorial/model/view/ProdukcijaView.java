package com.staxrt.tutorial.model.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class ProdukcijaView {
    private Long id;
    private String pavadinimas;
    private String ivestis;
    private String isvestis;
    private List<String> ivestys;
}
