package ru.spbstu.icst.problems;

import java.util.Scanner;

public class IndependentSet extends Problem {
    private final static String shortname = "IS";
    private final static String fullname = "Independant set problem";

    private int independentSetSize;
    private Graph graph;

    Scanner scanner = new Scanner(System.in);

    public IndependentSet() {
        this.graph = new Graph();

        // TODO implement this as part of other method
//        // Get information about size of cover we'll try to find
//        this.independentSetSize = scanner.nextInt();
//
//        // Initialize graph
//        graph = new Graph();
//        graph.readGraph();
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


    public String getShortname() {
        return IndependentSet.shortname;
    }
}
