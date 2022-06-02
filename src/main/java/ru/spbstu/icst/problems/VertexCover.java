package ru.spbstu.icst.problems;

import org.jgrapht.graph.DefaultEdge;
import ru.spbstu.icst.exceptions.InputException;
import ru.spbstu.icst.exceptions.SolutionNotFoundException;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
    public Set<Integer> vertexCover;
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
    public void solve() throws SolutionNotFoundException {
        // TODO consider Exceptions
        if (coverSize < 0) {
            System.out.println("Cover size cannot be less then 0!");
        } else if (coverSize > this.graph.getVertexCount()) {
            String answer = String.format("No solution can be found. Cover size is greater then number of nodes (%d > %d).",
                    coverSize, this.graph.getVertexCount());
            System.out.println(answer);
        } else {
            vertexCover = this.graph.getVertexCover();
            isSolved = vertexCover.size() <= coverSize;

            // If size of minimum vertex cover found is less than required just add some nodes
            if (this.isSolved) {
                // Add nodes here
                for (int i = 0; i < this.graph.getVertexCount(); i++) {
                    if (vertexCover.size() < coverSize) {
                        vertexCover.add(i);
                    } else {
                        break;
                    }
                }
            } else {
                vertexCover = new HashSet<>();
                throw new SolutionNotFoundException(String.format("No vertex cover of size %d found", this.coverSize));
            }
        }
    }

    @Override
    public void printSolution() {
        if (this.isSolved) {
            System.out.println("Vertices which constructs vertex cover in your graph:");
            System.out.println(this.graph.makeSetPrintable(this.vertexCover));
        } else {
            System.out.printf("No solution found for %s problem.", this.getShortname());
        }
    }

    @Override
    public void readSolution() {

    }

    public boolean isVertexCover(HashSet<Integer> vertexCoverCandidate) {

        if (vertexCoverCandidate.size() != coverSize) {
            return false;
        }

        for (DefaultEdge edge : graph.getEdges()) {
            boolean containsSource = vertexCoverCandidate.contains(this.graph.getEdgeSource(edge));
            boolean containsTarget = vertexCoverCandidate.contains(this.graph.getEdgeTarget(edge));

            if (!containsSource && !containsTarget) {
                return false;
            }
        }

        return true;
    }

    public void setVertexCover(HashSet<Integer> vertexCoverCandidate) throws InputException {
        if (!isVertexCover(vertexCoverCandidate)) {
            throw new InputException("Selected vertices don't create vertex cover of given size.");
        }
        this.vertexCover = vertexCoverCandidate;
        this.isSolved = true;
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

    public boolean getIsSolved() {
        return this.isSolved;
    }
}
