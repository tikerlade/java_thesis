package ru.spbstu.icst.problems;

import java.util.HashSet;
import java.util.Scanner;

public class IndependentSet extends Problem {
    private final static String shortname = "IS";
    private final static String fullname = "Independent set problem";

    private int independentSetSize;
    private Graph graph;

    public HashSet<Integer> independentSet;

    Scanner scanner = new Scanner(System.in);

    public IndependentSet() {
        this.graph = new Graph();
    }

    public IndependentSet(int independentSetSize, Graph graph) {
        this.independentSetSize = independentSetSize;
        this.graph = graph;
    }

    public int getIndependentSetSize() {
        return independentSetSize;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public String getShortname() {
        return IndependentSet.shortname;
    }

    @Override
    public void readData() {
        // Get information about size of cover we'll try to find
        System.out.print("Number of nodes required in independent set: ");
        this.independentSetSize = scanner.nextInt();

        // Read graph part of input
        this.graph.readGraph();
    }
}
