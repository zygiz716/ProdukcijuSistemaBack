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
    private static List<Produkcija> productions = new ArrayList<Produkcija>();
    private static List<String> facts;
    private static List<String> newFacts = new ArrayList<String>();
    private static List<String> savedFacts = new ArrayList<String>();
    private static Stack goals = new Stack();
    private static String goal = "";
    private static String finalGoal = "";
    private static List<String> GDB = new ArrayList<String>();
    private static int counter = 0;
    private static IsvedimoDuomenys duomenys = new IsvedimoDuomenys();
    private static int depthCounter = 0;
    private static boolean canBeApplied = false;
    private static List<Integer> path = new ArrayList<>();
    private static List<Integer> savedPath = new ArrayList<>();
    private static String test1 = "test1.txt";
    private static List<Long> produkcijosIds = new ArrayList<Long>();

    @Autowired
    private ProdukcijaRepository produkcijaRepository;

    public IsvedimoDuomenys AtliktiAtbuliniIsvedima(String goal1, List<String> facts1) {
        duomenys = new IsvedimoDuomenys();
        duomenys.setProdukcijosIds(new ArrayList<>());
        newFacts.clear();
        counter = 0;
        path = new ArrayList<>();
        produkcijosIds = new ArrayList<>();
        goal = goal1;
        goal1 = goal1;
        facts = facts1;
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
        for(int i = 0; i < productions.size(); i++){
            System.out.println("     R" + (i + 1) + ": " + productions.get(i).printIvestys() + " -> " + productions.get(i).getIsvestis());
        }
        System.out.println();
        System.out.println("  2) Faktai");
        System.out.print("     ");
        printFacts();
        System.out.println("\n");
        System.out.println("  3) Tikslas");
        System.out.print("     " + goal + "\n\n");
        if(facts.contains(goal)){
            System.out.println("\n3 DALIS. Rezultatai");
            System.out.println("  Tikslas " + goal + " tarp faktų. Kelias tuščias.");
        }else{
            System.out.println("2 DALIS. Vykdymas\n");
            boolean find = false;
            find = backwardChaining(goal);
            if (find){
                System.out.println("\n3 DALIS. Rezultatai\n");
                System.out.println("  1) Tikslas " + goal + " pasiektas.");
                System.out.print("  2) Kelias: ");
                printPath();
                System.out.println();
            }else{
                System.out.println("\n3 DALIS. Rezultatai\n");
                System.out.println("  1) Tikslas " + goal + " nepasiektas.");
                System.out.println("  2) Kelias neegzistuoja.");
            }
        }
        duomenys.setIsvedimoInfo(baos.toString());
        return duomenys;
    }

    public static void printFacts(){
        for(int i = 0; i < facts.size() - 1; i++){
            System.out.print(facts.get(i) + ", ");
        }
        System.out.print(facts.get(facts.size() - 1));
    }
    public static void printNewFacts(){
        for(int z = 0; z < newFacts.size(); z++){
            if (newFacts.size() == 1 || z == newFacts.size() - 1){
                System.out.print(newFacts.get(z) + ". ");
            }else{
                System.out.print(newFacts.get(z) + ", ");
            }
        }
    }

    public static void printDepth(){
        for(int i = 0; i < depthCounter; i++){
            System.out.print("-");
        }
    }

    public static void printInfo(String text){
        System.out.printf(String.format("%3s", ++counter) + ") ");
        printDepth();
        System.out.print(text);
    }

    public static void printPath() {
        List<Produkcija> panaudotosProdukcijos = new ArrayList<>();
        productions.forEach(produkcija -> {
            if (produkcijosIds.contains(produkcija.getId())) {
                panaudotosProdukcijos.add(produkcija);
            }
        });

        for (int i = 0; i < path.size(); i++) {
            if (i != path.size() - 1) {
                System.out.print("R" + path.get(i) + ", ");
            } else {
                System.out.print("R" + path.get(i) + ". ");
            }
        }
    }

    private static boolean isProd = false;

    public static boolean backwardChaining(String goal){
        goals.push(goal);
        if (facts.contains(goal)){
            printInfo("Tikslas " + goal + ". Faktas (duotas), nes faktai ");  printFacts(); System.out.println(". Grįžtame, sėkmė. ");
            goals.pop();
            depthCounter--;
            return true;
        }else if (newFacts.contains(goal)){
            printInfo("Tikslas " + goal + ". Faktas (buvo gautas). Faktai "); printFacts(); System.out.print(" ir "); printNewFacts();
            System.out.println("Grįžtame, sėkmė.");
            goals.pop();
            depthCounter--;
            return true;
        }else if (Collections.frequency(goals, goal) > 1){
            printInfo("Tikslas " + goal + ". Ciklas. Grįžtame, FAIL. \n");
            goals.pop();
            depthCounter--;
            return false;
        }

        else {
            savedFacts.clear();
            for(int i = 0; i < newFacts.size(); i++){
                savedFacts.add(newFacts.get(i));
            }
            savedPath.clear();
            for(int i = 0; i < path.size(); i++){
                savedPath.add(path.get(i));
            }
            for (int i = 0; i < productions.size(); i++) {
                canBeApplied = true;
                productions.get(i).setFlag1(true);



                if (productions.get(i).getIsvestis().equals(goal)) {
                    isProd = true;
                    printInfo("Tikslas " + goal + ". Randame R" + (i + 1) + ":" + productions.get(i).printIvestys() + "->" + goal + ". ");
                    System.out.print("Nauji tikslai ");
                    for(int z = 0; z < productions.get(i).getIvestys().size(); z++){
                        if (productions.get(i).getIvestys().size() == 1 || z == productions.get(i).getIvestys().size() - 1){
                            System.out.print(productions.get(i).getIvestys().get(z) + ".");
                        }else{
                            System.out.print(productions.get(i).getIvestys().get(z) + ",");
                        }
                    }
                    System.out.println();
                    for (int j = 0; j < productions.get(i).getIvestys().size(); j++) {
                        depthCounter++;
                        if (!backwardChaining(productions.get(i).getIvestys().get(j))) {
                            canBeApplied = false;
                            path.clear();
                            for(int z = 0; z < savedPath.size(); z++){
                                path.add(savedPath.get(z));
                            }
                            newFacts.clear();
                            for(int z = 0; z < savedFacts.size(); z++){
                                newFacts.add(savedFacts.get(z));
                            }
                            productions.get(i).setFlag1(false);
                            break;
                        }
                    }
                    if (canBeApplied){
                        newFacts.add(goal);
                        printInfo("Tikslas " + goal + ". Faktas (dabar gautas). Faktai "); printFacts(); System.out.print(" ir "); printNewFacts();
                        if (goals.size() == 1 && goals.lastElement().equals(finalGoal)){
                            System.out.println("Sėkmė.");
                        }else {
                            System.out.println("Grįžtame, sėkmė.");
                        }
                        goals.pop();
                        path.add(i + 1);
                        duomenys.getProdukcijosIds().add(productions.get(i).getId());
                        depthCounter--;
                        return true;
                    }
                }



            }
            if (isProd) {
                productions.forEach(produkcija -> {
                    if (produkcija.getIvestys().contains(goal)) {
                        produkcija.getIvestys().forEach(ivestis -> {
                            savedFacts.remove(ivestis);
                            newFacts.remove(ivestis);
                            facts.remove(ivestis);
                            System.out.println(ivestis);
                            int i;
                            for(i=0; i<productions.size(); i++){
                                if(productions.get(i).getIsvestis().equals(ivestis)){
                                    path.remove(i);
                                    savedPath.remove(i);
                                    duomenys.getProdukcijosIds().remove(productions.get(i).getId());
                                }
                            }
                        });
                    }
                });
                printInfo("Tikslas " + goal + ". Nėra taisyklių jo išvedimui. Grįžtame, FAIL. \n");
                isProd = false;
            }else {
                printInfo("Tikslas " + goal + ". Nėra daugiau taisyklių jo išvedimui. Grįžtame, FAIL. \n");
            }
            goals.pop();
            depthCounter--;
        }
        return false;
    }
}
