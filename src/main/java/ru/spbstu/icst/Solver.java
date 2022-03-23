package ru.spbstu.icst;

import ru.spbstu.icst.exceptions.InputException;
import ru.spbstu.icst.reductions.CnfTo3CnfReduction;
import ru.spbstu.icst.reductions.IsToVc;
import ru.spbstu.icst.reductions.Reduction;

import java.util.List;
import java.util.Scanner;

public class Solver {
    private List<Reduction> reductions;

    public Solver() {
        reductions = List.of(new CnfTo3CnfReduction(), new IsToVc());
    }

    public void start() throws InputException {
        // Intro messages
        System.out.println("Hello, this program will complete reduction of your problem to other NPC problem with your data.");
        System.out.println("Please select, which one of the following reductions you would like to complete:");

        // Print all reductions we have
        for (int i = 0; i < reductions.size(); i++) {
            Reduction reduction = reductions.get(i);
            System.out.println(i + ". " + reduction.toString());
        }

        // Read user data
        Scanner scanner = new Scanner(System.in);
        int userChoice = scanner.nextInt();

        // Check validaty of number just read
        if (userChoice < 0 || userChoice > reductions.size()) {
            throw new InputException("Your choice is out of bounds.");
        }

        // If input was correct - start reduction
        Reduction reduction = reductions.get(userChoice);
        reduction.start();
    }
}