package ru.spbstu.icst.problems;

import java.util.HashSet;
import java.util.Scanner;

public class VertexCover extends Problem {
    public static final String shortname = "VC";
    public static final String fullname = "Vertex cover problem";

    private int coverSize;
    private Graph graph;

    public HashSet<Integer> vertexCover;

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

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setCoverSize(int coverSize) {
        this.coverSize = coverSize;
    }

    public Graph getGraph() {
        return graph;
    }
}
