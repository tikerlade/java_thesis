package ru.spbstu.icst.problems;

import java.util.LinkedList;
import java.util.List;

public class Node {
    public static int nodeCounter;

    private int index;
    private String label;

    private List<Node> adjacent;

    public Node() {
        this.index = nodeCounter++;
        this.adjacent = new LinkedList<>();
    }

    public Node(String label) {
        this();
        this.label = label;
    }

    public void addAdjacent(Node adjNode) {
        adjacent.add(adjNode);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIndex() {
        return index;
    }
}
