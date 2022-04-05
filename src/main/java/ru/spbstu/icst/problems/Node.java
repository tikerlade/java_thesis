package ru.spbstu.icst.problems;

public class Node {
    public static int nodeCounter;

    private int index;
    private String label;

    public Node() {
        this.index = nodeCounter++;
    }

    public Node(String label) {
        this();
        this.label = label;
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
