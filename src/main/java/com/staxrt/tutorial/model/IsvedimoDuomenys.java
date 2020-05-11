package com.staxrt.tutorial.model;

import com.staxrt.tutorial.model.view.ProdukcijaView;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IsvedimoDuomenys {
    private String isvedimoInfo = "";
    private List<Long> produkcijosIds;

    public void pridetiEilute(String line) {
        this.isvedimoInfo += line + "\n";
    }

    public void pridetiEilute() {
        this.isvedimoInfo += "\n";
    }

    public void pridetiTeksta(String tekstas) {
        this.isvedimoInfo += tekstas;
    }
}
