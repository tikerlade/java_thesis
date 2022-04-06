package ru.spbstu.icst.problems;

import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.jgrapht.alg.independentset.ChordalGraphIndependentSetFinder;
import org.jgrapht.alg.vertexcover.GreedyVCImpl;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

// Nodes in graph numbered from 0 to vertexCount - 1
public class Graph {
    // Meta information about graph
    private int vertexCount;
    private int edgesCount;

    Scanner scanner = new Scanner(System.in);

    private final org.jgrapht.Graph<Integer, DefaultEdge> metaGraph;

    public Graph() {
        this.metaGraph = new SimpleGraph<>(DefaultEdge.class);
    }

    public Graph(int vertexCount, int edgesCount, List<Map.Entry<Integer, Integer>> edges) {
        // Initialize meatagraph
        this();

        // Init meta information
        this.vertexCount = vertexCount;
        this.edgesCount = edgesCount;

        for (int i = 0; i < vertexCount; i++) {
            this.metaGraph.addVertex(i);
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

        for (int i = 0; i < this.vertexCount; i++) {
            this.metaGraph.addVertex(i);
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

    public Set<Integer> getVertexCover() {
        GreedyVCImpl<Integer, DefaultEdge> vertexCoverFinder = new GreedyVCImpl<>(this.metaGraph);
        return new HashSet<>(vertexCoverFinder.getVertexCover());
    }

    public Set<Integer> getIndependentSet() {
        ChordalGraphIndependentSetFinder<Integer, DefaultEdge> independentSetFinder =
                new ChordalGraphIndependentSetFinder<>(this.metaGraph);
        return new HashSet<>(independentSetFinder.getIndependentSet());
    }

    public Set<Integer> getClique() {
        BronKerboschCliqueFinder<Integer, DefaultEdge> cliqueFinder = new BronKerboschCliqueFinder<>(this.metaGraph);
        return cliqueFinder.maximumIterator().next();
    }

    public void addEdge(int uIndex, int vIndex) {
        this.metaGraph.addEdge(uIndex, vIndex);
    }

    public int getVertexCount() {
        return this.metaGraph.vertexSet().size();
    }
    public int getEdgesCount() {return this.metaGraph.edgeSet().size();}

    public Set<DefaultEdge> getEdges() {
        return this.metaGraph.edgeSet();
    }
}
