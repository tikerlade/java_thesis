package ru.spbstu.icst.reductions;

import ru.spbstu.icst.controllers.Controller;
import ru.spbstu.icst.controllers.IsToVcScreenController;
import ru.spbstu.icst.exceptions.SolutionNotFoundException;
import ru.spbstu.icst.problems.IndependentSet;
import ru.spbstu.icst.problems.Problem;
import ru.spbstu.icst.problems.VertexCover;

import java.util.HashSet;

public class IsToVc extends Reduction {

    public IsToVc() {
        super(new IndependentSet(), new VertexCover());
        this.screenFilename = "controllers/IsToVcScreen.fxml";
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

    public void backward() throws SolutionNotFoundException {
        // Cast problems to required type
        IndependentSet independentSet = (IndependentSet) this.problemA;
        VertexCover vertexCover = (VertexCover) this.getProblemB();

        // TODO check that solution exists

        if (vertexCover.getIsSolved()) {
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
        } else {
            // TODO FORMAT MESSAGE STRING FOR CLARITY
            throw new SolutionNotFoundException("Vertex cover doesn't exist");
        }
    }

    @Override
    public Controller getScreenController() {
        return new IsToVcScreenController();
    }

    @Override
    public void resetProblems() {
        this.problemA = new IndependentSet();
        this.problemB = new VertexCover();
    }

    public void setProblemA(IndependentSet independentSet) {
        this.problemA = independentSet;
    }

    public void setProblemB(VertexCover vertexCover) {
        this.problemB = vertexCover;
    }


    public Problem getProblemB() {
        return this.problemB;
    }

}
