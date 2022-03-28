package ru.spbstu.icst.problems;

public class Clique extends Problem {
    public static final String shortname = "Clique";
    public static final String fullaname = "Clique problem";

    private Graph graph;
    private int numVertices;

    public Clique() {
        this.graph = new Graph();
    }

    public Clique(int numVertices, Graph graph) {
        this.graph = graph;
        this.numVertices = numVertices;
    }

    @Override
    public String getShortname() {
        return Clique.shortname;
    }
}
