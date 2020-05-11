package com.staxrt.tutorial.service.impl;

import com.staxrt.tutorial.model.IsvedimoDuomenys;
import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.repository.ProdukcijaRepository;
import com.staxrt.tutorial.service.TiesioginisIsvedimasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TiesioginisIsvedimasServiceImpl implements TiesioginisIsvedimasService {

    private List<Produkcija> produkcijos = new ArrayList<Produkcija>();
    private List<String> pradiniaiFaktai;
    private String pradinisTikslas;
    private List<String> turimiFaktai = new ArrayList<String>();
    private boolean produkcijaJauPanaudota = false;
    private boolean produkcijaIsvedama;

    private int counter = 0;
    private String trukstamaIvestis = "";
    private Boolean isvestisFaktuose = false;
    private Boolean produkcijosIsvestisFaktuose = false;
    private List<Integer> path = new ArrayList<Integer>();
    private IsvedimoDuomenys duomenys = new IsvedimoDuomenys();

    @Autowired
    private ProdukcijaRepository produkcijaRepository;

    @Override
    public IsvedimoDuomenys forwardChainingOutput(String tikslas, List<String> faktai) {

        duomenys = new IsvedimoDuomenys();
        duomenys.setProdukcijosIds(new ArrayList<>());

        counter = 0;
        path = new ArrayList<>();
        pradinisTikslas = tikslas;
        pradiniaiFaktai = faktai;
        turimiFaktai = pradiniaiFaktai;
        this.produkcijos = produkcijaRepository.findAll();

        duomenys.pridetiEilute("1 DALIS. Duomenys");
        duomenys.pridetiEilute();
        duomenys.pridetiEilute("  1) Taisyklės");
        for (int i = 0; i < produkcijos.size(); i++) {
            duomenys.pridetiEilute("     R" + (i + 1) + ": " + produkcijos.get(i).printIvestys() + " -> " + produkcijos.get(i).getIsvestis());
        }
        duomenys.pridetiEilute();
        duomenys.pridetiEilute("  2) Faktai");
        duomenys.pridetiTeksta("     ");
        printFacts();
        duomenys.pridetiEilute("\n");
        duomenys.pridetiEilute("  3) Tikslas");
        duomenys.pridetiTeksta("     " + pradinisTikslas + "\n\n");
        if (pradiniaiFaktai.contains(pradinisTikslas)) {
            duomenys.pridetiEilute("\n3 DALIS. Rezultatai");
            duomenys.pridetiEilute("  Tikslas " + pradinisTikslas + " tarp faktų. Kelias tuščias.");
        } else {
            duomenys.pridetiEilute("2 DALIS. Vykdymas");
            forwardChaining();
            if (turimiFaktai.contains(pradinisTikslas)) {
                duomenys.pridetiEilute("   Tikslas gautas.");
                duomenys.pridetiEilute("\n3 DALIS. Rezultatai");
                duomenys.pridetiEilute("    1) Tikslas " + pradinisTikslas + " išvestas.");
                duomenys.pridetiTeksta("    2) Kelias: ");
                for (int i = 0; i < path.size(); i++) {
                    if (i != path.size() - 1) {
                        duomenys.pridetiTeksta("R" + path.get(i) + ", ");
                    } else {
                        duomenys.pridetiTeksta("R" + path.get(i) + ".");
                    }
                }
            } else {
                duomenys.setIsvedimoInfo("Tikslas negautas.");
                return duomenys;
            }
        }
        return duomenys;
    }

    private void printFacts() {
        for (int i = 0; i < pradiniaiFaktai.size() - 1; i++) {
            duomenys.pridetiTeksta(pradiniaiFaktai.get(i) + ", ");
        }
        duomenys.pridetiTeksta(pradiniaiFaktai.get(pradiniaiFaktai.size() - 1));
    }

    private void forwardChaining() {
        if ((!turimiFaktai.contains(pradinisTikslas))) {
            duomenys.pridetiEilute("\n " + (++counter) + " ITERACIJA");
            for (int i = 0; i < produkcijos.size(); i++) {
                if ((!turimiFaktai.contains(pradinisTikslas))) {
                    produkcijaIsvedama = true;
                    int counter = 0;
                    if (produkcijos.get(i).isFlag1()) {
                        duomenys.pridetiEilute("   R" + (i + 1) + ":" + produkcijos.get(i).printIvestys() + "->" + produkcijos.get(i).getIsvestis() + " praleidžiame, nes pakelta flag1.");
                        produkcijaIsvedama = false;
                    }

                    if (produkcijos.get(i).isFlag2()) {
                        duomenys.pridetiEilute("   R" + (i + 1) + ":" + produkcijos.get(i).printIvestys() + "->" + produkcijos.get(i).getIsvestis() + " praleidžiame, nes pakelta flag2.");
                        produkcijaIsvedama = false;
                    }

                    for (int j = 0; j < produkcijos.get(i).getIvestys().size(); j++) {
                        if (turimiFaktai.contains(produkcijos.get(i).getIvestys().get(j))) {
                            counter++;
                        } else {
                            //duomenys.pridetiEilute("TRUKSTA " + productions.get(i).getAntecedents().get(j));
                            trukstamaIvestis = produkcijos.get(i).getIvestys().get(j);
                            duomenys.pridetiEilute("   R" + (i + 1) + ":" + produkcijos.get(i).printIvestys() + "->" + produkcijos.get(i).getIsvestis() + " netaikome, nes trūksta " + trukstamaIvestis + ".");
                            produkcijaIsvedama = false;
                        }
                    }

                    if (pradiniaiFaktai.contains(String.valueOf(produkcijos.get(i).getIsvestis()))) {
                        duomenys.pridetiEilute("   R" + (i + 1) + ":" + produkcijos.get(i).printIvestys() + "->" + produkcijos.get(i).getIsvestis() + " netaikome, nes konsekventas faktuose. Pakeliame flag2.");
                        produkcijos.get(i).setFlag2(true);
                        produkcijaIsvedama = false;
                    }

                    if (produkcijaIsvedama && counter == produkcijos.get(i).getIvestys().size()) {
                        produkcijos.get(i).setFlag1(true);  // pakeliame - true
                        turimiFaktai.add(String.valueOf(produkcijos.get(i).getIsvestis()));
                        duomenys.pridetiTeksta("   R" + (i + 1) + ":" + produkcijos.get(i).printIvestys() + "->" + produkcijos.get(i).getIsvestis() + " taikome. Pakeliame flag1. Faktai ");
                        printFacts();
                        duomenys.pridetiEilute(".");
                        path.add(i + 1);
                        duomenys.getProdukcijosIds().add(produkcijos.get(i).getId());
                        forwardChaining();
                    }
                }
            }
        }
    }
}
