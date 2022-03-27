package ru.spbstu.icst.problems;

import java.util.Scanner;

public class VertexCover extends Problem {
    public static final String shortname = "VC";
    public static final String fullname = "Vertex cover problem";

    private int coverSize;
    private Graph graph;

    Scanner scanner = new Scanner(System.in);

    public VertexCover() {
        this.graph = new Graph();

        // TODO implement as part of other method
//        // Get information about size of cover we'll try to find
//        coverSize = scanner.nextInt();
//
//        // Initialize graph
//        graph = new Graph();
//        graph.readGraph();
    }

    public VertexCover(int coverSize, Graph graph) {
        this.coverSize = coverSize;
        this.graph = graph;
    }

    public int getCoverSize() {
        return coverSize;
    }

    public String getShortname() {
        return VertexCover.shortname;
    }
}
