package ru.spbstu.icst.problems;

import java.util.*;

// Nodes in graph numbered from 0 to vertexCount - 1
public class Graph {
    // Meta information about graph
    private int vertexCount;
    private int edgesCount;
    private final boolean isDirected = false;
    Scanner scanner = new Scanner(System.in);

    // Store graph as adjacency list
    Node[] nodes;

    public Graph() {}

    public Graph(int vertexCount, int edgesCount, List<Map.Entry<Integer, Integer>> edges) {
        // Init meta information
        this.vertexCount = vertexCount;
        this.edgesCount = edgesCount;

        this.nodes = new Node[this.vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            nodes[i] = new Node();
        }

        // Put pairs of edges into adjacency list
        for (int i = 0; i < edgesCount; i++) {
            int u = edges.get(i).getKey();
            int v = edges.get(i).getValue();

            this.addEdge(u, v);
        }
    }

    public void readMeta() {
        // Read metainformation
        System.out.println("Graph nodes are 0-based indexed.\n" +
                "On next line print space separated two numbers: <Number of nodes in graph> and <Number of edges you will input>");
        this.vertexCount = scanner.nextInt();
        this.edgesCount = scanner.nextInt();

        this.nodes = new Node[this.vertexCount];
        for (int i = 0; i < this.vertexCount; i++) {
            this.nodes[i] = new Node();
        }
    }

    public void readGraph() {
        // Firstly read number of nodes and edges and init all nodes
        this.readMeta();

        // Read edges of the graph
        for (int i = 0; i < this.edgesCount; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            this.addEdge(u, v);
        }
    }

    public void addEdge(int uIndex, int vIndex) {
        Node u = this.nodes[uIndex];
        Node v = this.nodes[vIndex];

        // Add forward edge
        u.addAdjacent(v);

        // If graph is not directed add also backward edge
        if (!this.isDirected) {
            v.addAdjacent(u);
        }
    }
}
