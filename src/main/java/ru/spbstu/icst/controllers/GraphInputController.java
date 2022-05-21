package ru.spbstu.icst.controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.spbstu.icst.reductions.Reduction;
import ru.spbstu.icst.smartgraph.containers.SmartGraphDemoContainer;
import ru.spbstu.icst.smartgraph.graph.Digraph;
import ru.spbstu.icst.smartgraph.graph.DigraphEdgeList;
import ru.spbstu.icst.smartgraph.graph.Graph;
import ru.spbstu.icst.smartgraph.graphview.*;

public class GraphInputController extends Controller {
    static int counter;

    @Override
    public void runStage(Stage currentStage, String screenLocation) {
        try {
            Graph<String, String> g = build_sample_digraph();

            SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
            SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);
//            graphView.setAutomaticLayout(true);




            Scene scene = new Scene(new SmartGraphDemoContainer(graphView), 1024, 768);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("JavaFX SmartGraph Visualization");
//            stage.setMinHeight(500);
//            stage.setMinWidth(800);
            stage.setScene(scene);
            stage.show();
            currentStage.hide();


        /*
        Bellow you can see how to attach actions for when vertices and edges are double clicked
         */
            graphView.setVertexDoubleClickAction((SmartGraphVertex<String> graphVertex) -> {
                System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());

                //toggle different styling
                if( !graphVertex.removeStyleClass("myVertex") ) {
                /* for the golden vertex, this is necessary to clear the inline
                css class. Otherwise, it has priority. Test and uncomment. */
                    //graphVertex.setStyle(null);

                    graphVertex.addStyleClass("myVertex");
                }

                //want fun? uncomment below with automatic layout
                //g.removeVertex(graphVertex.getUnderlyingVertex());
                //graphView.update();
            });

            graphView.setEdgeDoubleClickAction(graphEdge -> {
                System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
                //dynamically change the style when clicked
                graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");

                graphEdge.getStylableArrow().setStyle("-fx-stroke: black; -fx-stroke-width: 3;");

                //uncomment to see edges being removed after click
                //Edge<String, String> underlyingEdge = graphEdge.getUnderlyingEdge();
                //g.removeEdge(underlyingEdge);
                //graphView.update();
            });

            graphView.init();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Graph<String, String> build_sample_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");

        g.insertEdge("A", "B", "AB");
        g.insertEdge("B", "A", "AB2");
        g.insertEdge("A", "C", "AC");
        g.insertEdge("A", "D", "AD");
        g.insertEdge("B", "C", "BC");
        g.insertEdge("C", "D", "CD");
        g.insertEdge("B", "E", "BE");
        g.insertEdge("F", "D", "DF");
        g.insertEdge("F", "D", "DF2");

        //yep, its a loop!
//        g.insertEdge("A", "A", "Loop");

        return g;
    }

    @Override
    public void setReduction(Reduction reduction) {
        return;
    }

    @Override
    Stage getStage() {
        return null;
    }

    @Override
    protected void runForwardSteps() {

    }

    @Override
    protected void solveProblemB() {

    }

    @Override
    protected void runBackwardSteps() {

    }

    @Override
    protected void readInput() throws Exception {

    }

    @Override
    protected void readSolution() throws Exception {

    }

    @Override
    protected Reduction getReduction() {
        return null;
    }
}
