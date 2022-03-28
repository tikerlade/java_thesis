package ru.spbstu.icst.problems;

import org.jgrapht.alg.clique.BronKerboschCliqueFinder;

import java.util.Scanner;

public class Clique extends Problem {
    public static final String shortname = "Clique";
    public static final String fullaname = "Clique problem";

    private Scanner scanner = new Scanner(System.in);

    private Graph graph;
    private BronKerboschCliqueFinder cliqueFinder;
    private int numVertices;

    public Clique() {
        this.graph = new Graph();
    }

    public Clique(int numVertices, Graph graph) {
        this.graph = graph;
        this.numVertices = numVertices;
    }

    @Override
    public String getShortname() {
        return Clique.shortname;
    }

    @Override
    public void readData() {
        System.out.print("Number of required vertices in Clique: ");
        this.numVertices = scanner.nextInt();
        this.graph.readGraph();
    }
}
