package ru.spbstu.icst.problems;

import org.jgrapht.alg.interfaces.VertexCoverAlgorithm;
import org.jgrapht.graph.DefaultEdge;

import java.util.Scanner;

public class VertexCover extends Problem {
    // General information about problem
    public static final String shortname = "VC";
    public static final String fullname = "Vertex cover problem";
    private final static String description = "For given graph G and number x," +
            " required to find subset of x vertices, such that every edge in G has at least one of vertex in the subset.";

    // Data which is required for problem
    private int coverSize;
    private Graph graph;

    private boolean isSolved = false;

    // Answer will be stored here
    public VertexCoverAlgorithm.VertexCover<Integer> vertexCover;
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
    public void readInput() {
        // Get information about size of cover we'll try to find
        System.out.print("Number of nodes required in vertex cover: ");
        this.coverSize = scanner.nextInt();

        // Read graph part of input
        this.graph.readGraph();
    }

    @Override
    public void solve() {
        // TODO consider Exceptions
        if (coverSize <= 0) {
            System.out.println("Cover size cannot be less then 1!");
        } else if (coverSize > this.graph.getVertexCount()) {
            String answer = String.format("No solution can be found. Cover size is greater then number of nodes (%d > %d).",
                    coverSize, this.graph.getVertexCount());
            System.out.println(answer);
        } else {
            vertexCover = this.graph.getVertexCover();
            isSolved = vertexCover.size() <= coverSize;

            for(Integer i : vertexCover) {
                System.out.println(i);
            }
        }
    }

    @Override
    public void printSolution() {
        // TODO print NO_SOLUTION if there is no solution
        System.out.println("Vertices which constructs vertex cover in your graph:");

        StringBuilder stringBuilder = new StringBuilder();
        for (int node: vertexCover) {
            stringBuilder.append(node).append(" ");
        }

        // Delete last space
        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        System.out.println(stringBuilder);
    }

    @Override
    public void readSolution() {

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

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        // First line - cover size
        stringBuilder.append(coverSize).append('\n');

        // Second line - <number of nodes> <number of edges>
        stringBuilder.append(graph.getVertexCount()).append(" ").append(graph.getEdgesCount()).append("\n");

        // Add all edges
        for (DefaultEdge edge: graph.getEdges()) {
            stringBuilder.append(edge.toString()).append("\n");
        }

        return stringBuilder.toString();
    }
}
