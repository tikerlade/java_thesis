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
    }

    public VertexCover(int coverSize, Graph graph) {
        this.coverSize = coverSize;
        this.graph = graph;
    }

    public int getCoverSize() {
        return coverSize;
    }

    @Override
    public void readData() {
        // Get information about size of cover we'll try to find
        System.out.print("Number of nodes required in vertex cover: ");
        this.coverSize = scanner.nextInt();

        // Read graph part of input
        this.graph.readGraph();
    }

    @Override
    public String getShortname() {
        return VertexCover.shortname;
    }
}
