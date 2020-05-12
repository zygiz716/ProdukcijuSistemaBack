package com.staxrt.tutorial.service.impl;

import com.staxrt.tutorial.model.IsvedimoDuomenys;
import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.repository.ProdukcijaRepository;
import com.staxrt.tutorial.service.IsvedimasSuKainaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IsvedimasSuKainaServiceImpl implements IsvedimasSuKainaService {
    private static List<Produkcija> produkcijos = new ArrayList<Produkcija>();
    private static List<String> faktai;
    private static List<Long> kelias = new ArrayList();
    //private static List<Long> galutinisKelias = new ArrayList();
    private static List<String> naujiFaktai = new ArrayList();
    private static List<String> tikslai = new ArrayList();
    private static String tikslas = "";
    private static IsvedimoDuomenys duomenys = new IsvedimoDuomenys();
    private static int gylis = 0;
    private static long kaina = 0;
    public static int zingsnis = 0;
    //private static long temp_kaina = 0;
    //private static long min_kaina = -1;

    @Autowired
    private ProdukcijaRepository produkcijaRepository;

    @Override
    public IsvedimoDuomenys AtliktiIsvedimaSuKaina(String goal1, List<String> facts1) {
        duomenys = new IsvedimoDuomenys();
        naujiFaktai.clear();
        kelias.clear();
        tikslai.clear();
        duomenys.setProdukcijosIds(new ArrayList<>());
        zingsnis = 0;
        tikslas = goal1;
        faktai = facts1;
        kaina = 0;
        this.produkcijos = produkcijaRepository.findAll();

        duomenys.pridetiEilute("1 DALIS. Duomenys");
        duomenys.pridetiEilute();
        duomenys.pridetiEilute("  1) Taisyklės");
        for(int i = 0; i < produkcijos.size(); i++){
            duomenys.pridetiEilute("     R" + (i + 1) + ": " + produkcijos.get(i).printIvestys() + " -> " + produkcijos.get(i).getIsvestis());
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
            find = isvedimasPagalKaina(tikslas,0);
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

    public static boolean isvedimasPagalKaina(String goal, int rekursijosGylis) {
        if (tikslai.contains(goal)) {
            ++zingsnis;
            duomenys.pridetiTeksta(Integer.toString(zingsnis));
            pazymetiGyli(rekursijosGylis);
            duomenys.pridetiTeksta(".Tikslas " + goal + ".");
            duomenys.pridetiEilute(" Ciklas. Grįžtame, FAIL.");
            return false;
        } else if (faktai.contains(goal)) {
            ++zingsnis;
            duomenys.pridetiTeksta(Integer.toString(zingsnis));
            pazymetiGyli(rekursijosGylis);
            duomenys.pridetiTeksta(".Tikslas " + goal + ".");
            duomenys.pridetiEilute(" Faktas (duotas), nes faktai " + goal + ". Grįžtame, sėkmė.");
            return true;
        } else if (naujiFaktai.contains(goal)) {
            ++zingsnis;
            duomenys.pridetiTeksta(Integer.toString(zingsnis));
            pazymetiGyli(rekursijosGylis);
            duomenys.pridetiTeksta(".Tikslas " + goal + ".");
            duomenys.pridetiEilute(" Faktas (buvo gautas). Grįžtame, sėkmė.");
            return true;
        } else {
            long min_kaina = -1;
            ArrayList<Long> geriausiasKelias = new ArrayList();
            tikslai.add(goal);

            int i;
            for(i = 0; i < produkcijos.size(); i++) {
                /*                duomenys.pridetiEilute(produkcijos.get(i));*/
                if ((produkcijos.get(i)).getIsvestis().equals(goal)) {
                    ++zingsnis;
                    duomenys.pridetiTeksta(Integer.toString(zingsnis));
                    pazymetiGyli(rekursijosGylis);
                    duomenys.pridetiTeksta(".Tikslas " + goal + ".");
                    duomenys.pridetiTeksta(" Randame ");
                    duomenys.pridetiTeksta("R" + (i + 1) + ": " + (produkcijos.get(i)).getIvestys() + " -> " + (produkcijos.get(i)).getIsvestis() + ". Nauji tikslai ");

                    for(int j = 0; j < (produkcijos.get(i)).getIvestys().size() - 1; ++j) {
                        duomenys.pridetiTeksta((produkcijos.get(i)).getIvestys().get(j) + ", ");
                    }

                    duomenys.pridetiEilute((produkcijos.get(i)).getIvestys().get((produkcijos.get(i)).getIvestys().size() - 1) + ".");
                    boolean produkcijaIsvedama = true;
                    ArrayList<String> ankstesniFaktai = new ArrayList();
                    ArrayList<Long> ankstesnisKelias = new ArrayList();
                    ArrayList<Long> laikinasKelias = new ArrayList();
                    boolean neraKelio = geriausiasKelias.isEmpty();
                    long ankstesneKaina = kaina;

                    ankstesniFaktai.addAll(naujiFaktai);
                    ankstesnisKelias.addAll(kelias);

                    for(int j = 0; j < (produkcijos.get(i)).getIvestys().size(); ++j){
                        if ( !isvedimasPagalKaina(produkcijos.get(i).getIvestys().get(j), rekursijosGylis + 1) ||( (min_kaina <= (kaina + produkcijos.get(i).getKaina()) && min_kaina != -1))) {
                            produkcijaIsvedama = false;
                            naujiFaktai = ankstesniFaktai;
                            kelias = ankstesnisKelias;
                            kaina = ankstesneKaina;
                            kelias.forEach(System.out::println);
                            if(neraKelio){
                                laikinasKelias.clear();
                            }
                            break;
                        }
                        else if(!faktai.contains(produkcijos.get(i).getIvestys().get(j))){
                            Set<Long> set = new HashSet<>(kelias);
                            laikinasKelias.addAll(set);
                            naujiFaktai = ankstesniFaktai;
                            kelias = ankstesnisKelias;
                        }
                    }

                    if (((produkcijaIsvedama && (min_kaina > (kaina + produkcijos.get(i).getKaina()) || min_kaina == -1)))) {
                        geriausiasKelias.clear();
                        geriausiasKelias.addAll(laikinasKelias);
                        kaina = kaina + produkcijos.get(i).getKaina();
                        min_kaina = kaina;
                        geriausiasKelias.add(produkcijos.get(i).getId());
                        duomenys.pridetiEilute("\nRasta nauja geriausia tikslo " + produkcijos.get(i).getIsvestis() + " Kaina " + min_kaina);
                        kaina = ankstesneKaina;
                        kelias = ankstesnisKelias;
                        naujiFaktai = ankstesniFaktai;
                    }
                }
            }

            ++zingsnis;
            duomenys.pridetiEilute();
            duomenys.pridetiTeksta(Integer.toString(zingsnis));
            pazymetiGyli(rekursijosGylis);
            duomenys.pridetiTeksta(".Tikslas " + goal + ".");
            duomenys.pridetiEilute(" Nėra daugiau taisyklių jo išvedimui. Grįžtame.");
            i = 0;

            for(int z = 0; z < tikslai.size(); ++z) {
                if (tikslai.get(z) == goal) {
                    i = z;
                    break;
                }
            }

            tikslai.remove(i);
            if(min_kaina >= 0){
                kelias.clear();
                kelias.addAll(geriausiasKelias);
                kaina = min_kaina;
                return true;
            }
            return false;
        }
    }

    public static void pazymetiGyli(int gylis) {
        for(int i = 0; i < gylis; ++i) {
            duomenys.pridetiTeksta(".");
        }

    }


}








/*
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

*/
/*    List<Produkcija> produkcijos;
    List<Gaminys> atidarytiGaminiai;
    List<Gaminys> ivestys = new ArrayList<>();
    long kaina;
    boolean yraAtidarytosIvestys;*//*


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
*/
/*        atidarytiGaminiai = new ArrayList<>();
        uzdarytiGaminiai = new ArrayList<>();
        facts1.forEach(fact -> atidarytiGaminiai.add(new Gaminys(fact, 0L, new ArrayList<>())));
        duomenys = new IsvedimoDuomenys();
        this.produkcijos = produkcijaRepository.findAll();

        return duomenys;*//*

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
*/
/*                        GDB.add(String.valueOf(productions.get(i).getIsvestis()));*//*

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

*/
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

    }*//*

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
*/
