package ru.spbstu.icst.problems;

import java.util.Scanner;

public class VertexCover extends Problem {
    private int coverSize;
    private Graph graph;

    Scanner scanner = new Scanner(System.in);

    public VertexCover() {
        this.shortname = "VC";
        this.fullname = "Vertex cover problem";
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
}
