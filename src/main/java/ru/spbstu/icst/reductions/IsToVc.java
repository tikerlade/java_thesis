package ru.spbstu.icst.reductions;

import ru.spbstu.icst.problems.*;

public class IsToVc extends Reduction {

    private IndependentSet independentSet;
    private VertexCover vertexCover;

    public IsToVc() {
        this.problemA = new IndependentSet();
        this.problemB = new VertexCover();
    }

    @Override
    public void start() {

    }

    public void forward(IndependentSet independentSet) {
        this.independentSet = independentSet;

        Graph graph = independentSet.getGraph();
        int vertexCoverSize = graph.getVertexCount() - independentSet.getIndependentSetSize();

        this.vertexCover = new VertexCover(vertexCoverSize, graph);
    }

    public void backward(IndependentSet independentSet, VertexCover vertexCover) {}

//    @Override
//    public String toString() {
//        return independentSet.shortname + " -> " + vertexCover.shortname;
//    }
}
