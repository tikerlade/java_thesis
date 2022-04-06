package ru.spbstu.icst.problems;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Clique extends Problem {
    public static final String shortname = "Clique";
    public static final String fullaname = "Clique problem";
    private final static String description = "For given graph G and number x," +
            " required to find subset of x vertices, all adjacent to each other in the graph.";

    private final Scanner scanner = new Scanner(System.in);
    private final Graph graph;
    private Set<Integer> clique;
    private boolean isSolved;
    private int cliqueSize;

    public Clique() {
        this.graph = new Graph();
    }

    public Clique(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void readInput() {
        System.out.print("Number of required vertices in Clique: ");
        this.cliqueSize = scanner.nextInt();
        this.graph.readGraph();
    }

    @Override
    public void solve() {
        this.clique = this.graph.getClique();
        this.isSolved = this.clique.size() >= this.cliqueSize;

        // If size of maximum clique found is greater than required just pop some elements
        if (this.isSolved) {
            this.clique = this.clique.stream().limit(cliqueSize).collect(Collectors.toSet());
        } else {
            this.clique = new HashSet<>();
        }
    }

    @Override
    public void printSolution() {
        if (this.isSolved) {
            System.out.println("Vertices which constructs Clique in your graph:");
            System.out.println(this.graph.makeSetPrintable(this.clique));
        } else {
            System.out.printf("No solution found for %s problem.", this.getShortname());
        }
    }

    @Override
    public void readSolution() {

    }

    @Override
    public String getShortname() {
        return Clique.shortname;
    }
}
