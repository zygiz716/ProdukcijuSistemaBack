package com.staxrt.tutorial.service.impl;

import com.staxrt.tutorial.model.Gaminys;
import com.staxrt.tutorial.model.IsvedimoDuomenys;
import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.repository.ProdukcijaRepository;
import com.staxrt.tutorial.service.IsvedimasSuKainaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

@Service
public class IsvedimasSuKainaServiceImpl implements IsvedimasSuKainaService {

    private static List<Produkcija> productions = new ArrayList<Produkcija>();
    private static List<Gaminys> facts;
    private static String goal;
    private static List<Gaminys> tikslai;
    private static List<Gaminys> GDB = new ArrayList<Gaminys>();
    private static boolean isFlag1 = false;
    private static int counter = 0;
    private static String missingAntecedent = "";
    private static Boolean consistentInFacts = false;
    private static Boolean isFlag2 = false;
    private static List<Integer> path = new ArrayList<Integer>();
    private static List<Long> produkcijosIds = new ArrayList<Long>();
    private static IsvedimoDuomenys duomenys = new IsvedimoDuomenys();
    private static Scanner scanner = new Scanner(System.in);

/*    List<Produkcija> produkcijos;
    List<Gaminys> atidarytiGaminiai;
    List<Gaminys> ivestys = new ArrayList<>();
    long kaina;
    boolean yraAtidarytosIvestys;*/

    @Autowired
    private ProdukcijaRepository produkcijaRepository;

    @Override
    public IsvedimoDuomenys AtliktiIsvedimaSuKaina(String goal1, List<String> facts1){
        duomenys = new IsvedimoDuomenys();
        duomenys.setProdukcijosIds(new ArrayList<>());

        counter = 0;
        path = new ArrayList<>();
        produkcijosIds = new ArrayList<>();
        tikslai = new ArrayList<>();
        goal = goal1;
        facts = new ArrayList<>();
        facts1.forEach(fact -> facts.add(new Gaminys(fact, 0L, new ArrayList<>())));
        GDB = facts;
        this.productions = produkcijaRepository.findAll();

        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
// IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
// Tell Java to use your special stream
        System.setOut(ps);

        System.out.println("1 DALIS. Duomenys");
        System.out.println();
        System.out.println("  1) Taisyklės");
        for (int i = 0; i < productions.size(); i++) {
            System.out.println("     R" + (i + 1) + ": " + productions.get(i).printIvestys() + " -> " + productions.get(i).getIsvestis());
        }
        System.out.println();
        System.out.println("  2) Faktai");
        System.out.print("     ");
        printFacts();
        System.out.println("\n");
        System.out.println("  3) Tikslas");
        System.out.print("     " + goal + "\n\n");
        if (facts.contains(goal)) {
            // System.out.println("\n3 DALIS. Rezultatai");
            System.out.println("  Tikslas " + goal + " tarp faktų. Kelias tuščias.");
        } else {
            System.out.println("2 DALIS. Vykdymas");
            forwardChaining();
            Gaminys gautasGaminys = ieskotiGaminio(goal);
            if (gautasGaminys != null) {
                System.out.println("   Tikslas gautas.");
                System.out.println("\n3 DALIS. Rezultatai");
                System.out.println("    1) Tikslas " + goal + " išvestas.");
                System.out.print("    2) Kelias: ");
                for (int i = 0; i < path.size(); i++) {
                    if (i != path.size() - 1) {
                        System.out.print("R" + path.get(i) + ", ");
                    } else {
                        System.out.print("R" + path.get(i) + ".");
                    }
                }
                duomenys.setProdukcijosIds(gautasGaminys.getProdukcijosIds());
            } else {
                duomenys.setIsvedimoInfo("Tikslas negautas.");
                return duomenys;
            }
        }
        System.out.flush();
        System.setOut(old);

        duomenys.setIsvedimoInfo(baos.toString());
        return duomenys;
/*        atidarytiGaminiai = new ArrayList<>();
        uzdarytiGaminiai = new ArrayList<>();
        facts1.forEach(fact -> atidarytiGaminiai.add(new Gaminys(fact, 0L, new ArrayList<>())));
        duomenys = new IsvedimoDuomenys();
        this.produkcijos = produkcijaRepository.findAll();

        return duomenys;*/
    }

    private static void printFacts() {
        for (int i = 0; i < facts.size() - 1; i++) {
            System.out.print(facts.get(i) + ", ");
        }
        System.out.print(facts.get(facts.size() - 1));
    }

    private void forwardChaining() {
            System.out.println("\n " + (++counter) + " ITERACIJA");
            for (int i = 0; i < productions.size(); i++) {
                System.out.println(i);
                    isFlag1 = false;
                    isFlag2 = false;
                    consistentInFacts = false;
                    if (canApply(i)) {
                        productions.get(i).setFlag1(true);  // pakeliame - true
/*                        GDB.add(String.valueOf(productions.get(i).getIsvestis()));*/
                        Gaminys naujasGaminys = new Gaminys(productions.get(i).getIsvestis(), this.skaiciuotiKaina(productions.get(i)), nustatytiProdukcijas(productions.get(i)));
                        naujasGaminys.getProdukcijosIds().add(productions.get(i).getId());
                        Gaminys senasGaminys = ieskotiGaminio(naujasGaminys.getPavadinimas());
                        if(senasGaminys != null){
                            if(senasGaminys.getKaina() > naujasGaminys.getKaina()){
                                GDB.remove(senasGaminys);
                                GDB.add(naujasGaminys);
                            }
                        } else {
                            GDB.add(naujasGaminys);
                        }
                        System.out.print("   R" + (i + 1) + ":" + productions.get(i).printIvestys() + "->" + productions.get(i).getIsvestis() + " taikome. Pakeliame flag1. Faktai ");
                        printFacts();
                        System.out.println(".");
                        path.add(i + 1);
                        System.out.println(productions.get(i).getId());
                        duomenys.getProdukcijosIds().add(productions.get(i).getId());
                        forwardChaining();
                    } else if (isFlag1) {
                        System.out.println("   R" + (i + 1) + ":" + productions.get(i).printIvestys() + "->" + productions.get(i).getIsvestis() + " praleidžiame, nes pakelta flag1.");
                    } else if (consistentInFacts) {
                        System.out.println("   R" + (i + 1) + ":" + productions.get(i).printIvestys() + "->" + productions.get(i).getIsvestis() + " netaikome, nes konsekventas faktuose. Pakeliame flag2.");
                    } else if (isFlag2) {
                        System.out.println("   R" + (i + 1) + ":" + productions.get(i).printIvestys() + "->" + productions.get(i).getIsvestis() + " praleidžiame, nes pakelta flag2.");
                    } else {
                        System.out.println("   R" + (i + 1) + ":" + productions.get(i).printIvestys() + "->" + productions.get(i).getIsvestis() + " netaikome, nes trūksta " + missingAntecedent + ".");
                    }
            }
    }

    public boolean canApply(int i) {   // FLAGUS TIKRINTI
        int counter = 0;
        for (int j = 0; j < productions.get(i).getIvestys().size(); j++) {
            if (productions.get(i).isFlag1()) {
                isFlag1 = true;
                return false;
            }
            if (productions.get(i).isFlag2()) {
                isFlag2 = true;
                return false;
            }
            if (ieskotiGaminio(productions.get(i).getIvestys().get(j)) != null) {
                counter++;
            } else {
                //System.out.println("TRUKSTA " + productions.get(i).getAntecedents().get(j));
                missingAntecedent = productions.get(i).getIvestys().get(j);
                return false;
            }
            if (facts.contains(String.valueOf(productions.get(i).getIsvestis()))) {
                consistentInFacts = true;
                productions.get(i).setFlag2(true);
                return false;
            }
        }
        return counter == productions.get(i).getIvestys().size();
    }

/*    private void isvestiProdukcijas(){
        produkcijos.forEach(produkcija -> {
            if(produkcija.getIvestys().size()<2){
                ivestys.clear();
                yraAtidarytosIvestys = true;
                produkcija.getIvestys().forEach(ivestis -> {
                    Gaminys gaminys = atidarytiGaminiai.stream().filter(gaminys1 -> gaminys1.getPavadinimas().equals(ivestis)).findAny().orElse(null);
                    if(gaminys == null){
                        yraAtidarytosIvestys = false;
                    } else {
                        ivestys.add(gaminys);
                    }
                });
                if(produkcija.getIvestys().size() <= ivestys.size()){

                    Gaminys naujasGaminys = new Gaminys(produkcija.getIsvestis(), skaiciuotiKaina(produkcija.getKaina()), )
                }
            } else{

            }
        });

    }*/
    private long skaiciuotiKaina(Produkcija produkcija){
        long kaina = 0;
        for(int i = 0; i<produkcija.getIvestys().size(); i++){
            Gaminys gaminys = ieskotiGaminio(produkcija.getIvestys().get(i));
            kaina = kaina + gaminys.getKaina();
        }
        kaina = kaina + produkcija.getKaina();
        return kaina;
    }

    private List<Long> nustatytiProdukcijas(Produkcija produkcija){
        List<Long> produkcijuIds = new ArrayList<>();
        for(int i = 0; i<produkcija.getIvestys().size(); i++){
            Gaminys gaminys = ieskotiGaminio(produkcija.getIvestys().get(i));
            produkcijuIds.addAll(gaminys.getProdukcijosIds());
        }
        return produkcijuIds;
    }

    private Gaminys ieskotiGaminio(String pavadinimas) {
        return GDB.stream().filter(gaminys -> gaminys.getPavadinimas().equals(pavadinimas)).findAny().orElse(null);
    }
}
