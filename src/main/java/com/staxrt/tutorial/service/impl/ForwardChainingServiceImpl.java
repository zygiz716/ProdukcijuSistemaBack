package com.staxrt.tutorial.service.impl;

import com.staxrt.tutorial.model.Production;
import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.repository.ProductionRepository;
import com.staxrt.tutorial.repository.ProdukcijaRepository;
import com.staxrt.tutorial.service.ForwardChainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Service
public class ForwardChainingServiceImpl implements ForwardChainingService {

    private static String filename = "";
    private static List<Produkcija> productions = new ArrayList<Produkcija>();
    private static List<String> facts;
    private static String goal;
    private static char consistent;
    private static List<String> antecedents;
    private static List<String> GDB = new ArrayList<String>();
    private static boolean isFlag1 = false;
    private static int counter = 0;
    private static String missingAntecedent = "";
    private static Boolean consistentInFacts = false;
    private static Boolean isFlag2 = false;
    private static List<Integer> path = new ArrayList<Integer>();
    private static Scanner scanner = new Scanner(System.in);

    @Autowired
    private ProdukcijaRepository produkcijaRepository;

    @Override
    public String forwardChainingOutput(String goal1, List<String> facts1) {

        counter = 0;
        path = new ArrayList<>();
        goal = goal1;
        facts = facts1;
        GDB = facts;
        List<Produkcija> productions = produkcijaRepository.findAll();

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
        if (GDB.contains(goal)) {
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
        } else {
            return "Tikslas negautas.";
        }
    }
        System.out.flush();
        System.setOut(old);

        return baos.toString();
}

    private static void printFacts() {
        for (int i = 0; i < facts.size() - 1; i++) {
            System.out.print(facts.get(i) + ", ");
        }
        System.out.print(facts.get(facts.size() - 1));
    }

    private static void forwardChaining() {
        if ((!GDB.contains(goal))) {
            System.out.println("\n " + (++counter) + " ITERACIJA");
            for (int i = 0; i < productions.size(); i++) {
                if ((!GDB.contains(goal))) {
                    isFlag1 = false;
                    isFlag2 = false;
                    consistentInFacts = false;
                    if (canApply(i)) {
                        productions.get(i).setFlag1(true);  // pakeliame - true
                        GDB.add(String.valueOf(productions.get(i).getIsvestis()));
                        System.out.print("   R" + (i + 1) + ":" + productions.get(i).printIvestys() + "->" + productions.get(i).getIsvestis() + " taikome. Pakeliame flag1. Faktai ");
                        printFacts();
                        System.out.println(".");
                        path.add(i + 1);
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
        }
    }

    public static boolean canApply(int i) {   // FLAGUS TIKRINTI
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
            if (GDB.contains(productions.get(i).getIvestys().get(j))) {
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
}
