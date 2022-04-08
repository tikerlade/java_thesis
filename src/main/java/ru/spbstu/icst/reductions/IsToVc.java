package ru.spbstu.icst.reductions;

import ru.spbstu.icst.problems.*;

import java.util.HashSet;

public class IsToVc extends Reduction {

    public IsToVc() {
        super(new IndependentSet(), new VertexCover());
        this.screenLocation = "IsToVc_screen.fxml";
    }

    public void forward() {
        // Cast problems to required type
        IndependentSet independentSet = (IndependentSet) this.problemA;
        VertexCover vertexCover = (VertexCover) this.problemB;

        // Prepare independant set size
        int coverSize = independentSet.getGraph().getVertexCount() - independentSet.getIndependentSetSize();

        // Add converted data to Vertex cover problem
        vertexCover.setGraph(independentSet.getGraph());
        vertexCover.setCoverSize(coverSize);

        // Reassign new version of problem
        this.problemB = vertexCover;
    }

    public void backward() {
        // Cast problems to required type
        IndependentSet independentSet = (IndependentSet) this.problemA;
        VertexCover vertexCover = (VertexCover) this.problemB;

        // TODO check that solution exists

        // Initialize independent set which contains all nodes of graph
        independentSet.independentSet = new HashSet<>();
        for (int nodeNum = 0; nodeNum < vertexCover.getGraph().getVertexCount(); nodeNum++) {
            independentSet.independentSet.add(nodeNum);
        }

        // Remove nodes which exists in vertex cover of problemB
        for (Object nodeNum : vertexCover.vertexCover) {
            independentSet.independentSet.remove(nodeNum);
        }

        // Reassign new version of problem
        this.problemA = independentSet;
    }
}
