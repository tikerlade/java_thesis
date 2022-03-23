package ru.spbstu.icst.problems;

import java.util.Scanner;

public class VertexCover implements Problem {
    // Metainformation
    public static String shortname = "VC";

    private int coverSize;
    private Graph graph;

    Scanner scanner = new Scanner(System.in);

    public VertexCover() {
        // Get information about size of cover we'll try to find
        coverSize = scanner.nextInt();

        // Initialize graph
        graph = new Graph();
        graph.readGraph();
    }

    public VertexCover(int coverSize, Graph graph) {
        this.coverSize = coverSize;
        this.graph = graph;
    }

    public int getCoverSize() {
        return coverSize;
    }
}
