package com.staxrt.tutorial.service.impl;

import com.staxrt.tutorial.model.IsvedimoDuomenys;
import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.repository.ProdukcijaRepository;
import com.staxrt.tutorial.service.AtbulinisIsvedimasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

@Service
public class AtbulinisIsvedimasServiceImpl implements AtbulinisIsvedimasService {
    private static List<Produkcija> produkcijos = new ArrayList<Produkcija>();
    private static List<String> faktai;
    private static List<Long> kelias = new ArrayList();
    private static List<String> naujiFaktai = new ArrayList();
    private static List<String> tikslai = new ArrayList();
    private static String tikslas = "";
    private static IsvedimoDuomenys duomenys = new IsvedimoDuomenys();
    private static int gylis = 0;
    public static int zingsnis = 0;

    @Autowired
    private ProdukcijaRepository produkcijaRepository;

    public IsvedimoDuomenys AtliktiAtbuliniIsvedima(String goal1, List<String> facts1) {
        duomenys = new IsvedimoDuomenys();
        naujiFaktai.clear();
        kelias.clear();
        tikslai.clear();
        duomenys.setProdukcijosIds(new ArrayList<>());
        zingsnis = 0;
        tikslas = goal1;
        faktai = facts1;
        this.produkcijos = produkcijaRepository.findAll();

        duomenys.pridetiEilute("1 DALIS. Duomenys");
        duomenys.pridetiEilute();
        duomenys.pridetiEilute("  1) Taisyklės");
        for(int i = 0; i < produkcijos.size(); i++){
            duomenys.pridetiEilute("     P" + (i + 1) + ": " + produkcijos.get(i).printIvestys() + " -> " + produkcijos.get(i).getIsvestis());
        }
        duomenys.pridetiEilute();
        duomenys.pridetiEilute("  2) Faktai");
        duomenys.pridetiTeksta("     ");
        printFacts();
        duomenys.pridetiEilute("\n");
        duomenys.pridetiEilute("  3) Tikslas");
        duomenys.pridetiTeksta("     " + tikslas + "\n\n");
        if(faktai.contains(tikslas)){
            duomenys.pridetiEilute("\n3 DALIS. Rezultatai");
            duomenys.pridetiEilute("  Tikslas " + tikslas + " tarp faktų. Kelias tuščias.");
        }else{
            duomenys.pridetiEilute("2 DALIS. Vykdymas\n");
            boolean find = false;
            find = backwardChaining(tikslas,0);
            if (find){
                duomenys.pridetiEilute();
                duomenys.pridetiEilute("\n3 DALIS. Rezultatai\n");
                duomenys.pridetiEilute("  1) Tikslas " + tikslas + " pasiektas.");
                duomenys.pridetiTeksta("  2) Kelias: ");
                printPath();
                duomenys.pridetiEilute();
            }else{
                duomenys.pridetiEilute();
                duomenys.pridetiEilute("\n3 DALIS. Rezultatai\n");
                duomenys.pridetiEilute("  1) Tikslas " + tikslas + " nepasiektas.");
                duomenys.pridetiEilute("  2) Kelias neegzistuoja.");
            }
        }
        duomenys.setProdukcijosIds(kelias);
        return duomenys;
    }

    public static void printFacts(){
        for(int i = 0; i < faktai.size() - 1; i++){
            duomenys.pridetiTeksta(faktai.get(i) + ", ");
        }
        duomenys.pridetiTeksta(faktai.get(faktai.size() - 1));
    }

    public static void printPath() {
        List<Produkcija> panaudotosProdukcijos = new ArrayList<>();
        produkcijos.forEach(produkcija -> {
            if (kelias.contains(produkcija.getId())) {
                panaudotosProdukcijos.add(produkcija);
            }
        });

        for (int i = 0; i < panaudotosProdukcijos.size(); i++) {
            if (i != panaudotosProdukcijos.size() - 1) {
                duomenys.pridetiTeksta( panaudotosProdukcijos.get(i).getPavadinimas() + ", ");
            } else {
                duomenys.pridetiTeksta(panaudotosProdukcijos.get(i).getPavadinimas() + ". ");
            }
        }
    }

    public static boolean backwardChaining(String goal, int depth) {
        if (tikslai.contains(goal)) {
            ++zingsnis;
            duomenys.pridetiTeksta(Integer.toString(zingsnis));
            indent(depth);
            duomenys.pridetiTeksta(".Tikslas " + goal + ".");
            duomenys.pridetiEilute(" Ciklas. Grįžtame, FAIL.");
            return false;
        } else if (faktai.contains(goal)) {
            ++zingsnis;
            duomenys.pridetiTeksta(Integer.toString(zingsnis));
            indent(depth);
            duomenys.pridetiTeksta(".Tikslas " + goal + ".");
            duomenys.pridetiEilute(" Faktas (duotas), nes faktai " + goal + ". Grįžtame, sėkmė.");
            return true;
        } else if (naujiFaktai.contains(goal)) {
            ++zingsnis;
            duomenys.pridetiTeksta(Integer.toString(zingsnis));
            indent(depth);
            duomenys.pridetiTeksta(".Tikslas " + goal + ".");
            duomenys.pridetiEilute(" Faktas (buvo gautas). Grįžtame, sėkmė.");
            return true;
        } else {
            tikslai.add(goal);

            int i;
            for(i = 0; i < produkcijos.size(); i++) {
/*                duomenys.pridetiEilute(produkcijos.get(i));*/
                if ((produkcijos.get(i)).getIsvestis().equals(goal)) {
                    ++zingsnis;
                    duomenys.pridetiTeksta(Integer.toString(zingsnis));
                    indent(depth);
                    duomenys.pridetiTeksta(".Tikslas " + goal + ".");
                    duomenys.pridetiTeksta(" Randame ");
                    duomenys.pridetiTeksta("P" + (i + 1) + ": " + (produkcijos.get(i)).getIvestys() + " -> " + (produkcijos.get(i)).getIsvestis() + ". Nauji tikslai ");

                    for(int j = 0; j < (produkcijos.get(i)).getIvestys().size() - 1; ++j) {
                        duomenys.pridetiTeksta((produkcijos.get(i)).getIvestys().get(j) + ", ");
                    }

                    duomenys.pridetiEilute((produkcijos.get(i)).getIvestys().get((produkcijos.get(i)).getIvestys().size() - 1) + ".");
                    boolean galimaTaikyti = true;
                    ArrayList<String> ankstesniFaktai = new ArrayList();
                    ArrayList<Long> ankstesnisKelias = new ArrayList();
                    ankstesniFaktai.addAll(naujiFaktai);
                    ankstesnisKelias.addAll(kelias);
                    //Iterator var8 = (productions.get(i)).getIvestys().iterator();

                    for(int j = 0; j < (produkcijos.get(i)).getIvestys().size(); ++j){
                        if (!backwardChaining(produkcijos.get(i).getIvestys().get(j), depth + 1)) {
                            galimaTaikyti = false;
                            naujiFaktai = ankstesniFaktai;
                            kelias = ankstesnisKelias;
                            kelias.forEach(System.out::println);
                            break;
                        }
                    }

                    if (galimaTaikyti) {
                        naujiFaktai.add(produkcijos.get(i).getIsvestis());
                        kelias.add(produkcijos.get(i).getId());
                        zingsnis++;
                        duomenys.pridetiTeksta(Integer.toString(zingsnis));
                        indent(depth);
                        duomenys.pridetiTeksta(".Tikslas " + goal + ". Faktas (dabar gautas). Faktai ");

                        int j;
                        for(j = 0; j < faktai.size(); j++) {
                            duomenys.pridetiTeksta(faktai.get(j) + " ir ");
                        }

                        for(j = 0; j < naujiFaktai.size(); j++) {
                            if(j == naujiFaktai.size()-1){
                                duomenys.pridetiTeksta(naujiFaktai.get(j) + ". Grįžtame, sėkmė.");
                            } else{
                                duomenys.pridetiTeksta(naujiFaktai.get(j) + ", ");
                            }
                        }
                        j = 0;

                        for(int z = 0; z < tikslai.size(); ++z) {
                            if (tikslai.get(z).equals(goal)) {
                                j = z;
                                break;
                            }
                        }

                        tikslai.remove(j);
                        return true;
                    }
                }
            }

            zingsnis++;
            duomenys.pridetiEilute();
            duomenys.pridetiTeksta(Integer.toString(zingsnis));
            indent(depth);
            duomenys.pridetiTeksta(".Tikslas " + goal + ".");
            duomenys.pridetiEilute(" Nėra daugiau taisyklių jo išvedimui. Grįžtame, FAIL.");
            i = 0;

            for(int z = 0; z < tikslai.size(); ++z) {
                if (tikslai.get(z) == goal) {
                    i = z;
                    break;
                }
            }

            tikslai.remove(i);
            return false;
        }
    }

    public static void indent(int depth) {
        for(int i = 0; i < depth; ++i) {
            duomenys.pridetiTeksta(".");
        }

    }


}
