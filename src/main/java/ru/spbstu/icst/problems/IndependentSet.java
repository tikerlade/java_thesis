package ru.spbstu.icst.problems;

import java.util.Scanner;
import java.util.Set;

public class IndependentSet extends Problem {
    private final static String shortname = "IS";
    private final static String fullname = "Independent set problem";
    private final static String description = "For given graph G and number x," +
            " required to find subset of x vertices, non of which are adjacent to each other (have an edge) in the graph.";

    private int independentSetSize;
    private final Graph graph;

    public Set<Integer> independentSet;
    public boolean isSolved;

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
    public void readInput() {
        // Get information about size of cover we'll try to find
        System.out.print("Number of nodes required in independent set: ");
        this.independentSetSize = scanner.nextInt();

        // Read graph part of input
        this.graph.readGraph();
    }

    @Override
    public void solve() {
        // TODO consider Exceptions
        if (independentSetSize <= 0) {
            System.out.println("Independent set size cannot be less then 1!");
        } else if (independentSetSize > this.graph.getVertexCount()) {
            String answer = String.format("No solution can be found. Independent set size is greater then number of nodes (%d > %d).",
                    independentSetSize, this.graph.getVertexCount());
            System.out.println(answer);
        } else {
            independentSet = this.graph.getIndependentSet();
            isSolved = independentSet.size() >= independentSetSize;
        }
    }

    @Override
    public void printSolution() {
        // TODO print NO_SOLUTION if there is no solution
        System.out.println("Vertices which constructs independent set in your graph:");

        StringBuilder stringBuilder = new StringBuilder();
        for (int node: independentSet) {
            stringBuilder.append(node).append(" ");
        }

        // Delete last space
        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        System.out.println(stringBuilder);
    }

    @Override
    public void readSolution() {

    }
}
