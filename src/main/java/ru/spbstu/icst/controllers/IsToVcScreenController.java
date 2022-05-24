package ru.spbstu.icst.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.spbstu.icst.Main;
import ru.spbstu.icst.reductions.IsToVc;
import ru.spbstu.icst.reductions.Reduction;
import ru.spbstu.icst.smartgraph.graph.Digraph;
import ru.spbstu.icst.smartgraph.graph.DigraphEdgeList;
import ru.spbstu.icst.smartgraph.graph.Graph;
import ru.spbstu.icst.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import ru.spbstu.icst.smartgraph.graphview.SmartGraphPanel;
import ru.spbstu.icst.smartgraph.graphview.SmartPlacementStrategy;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class IsToVcScreenController extends Controller implements Initializable {

    @FXML
    private BorderPane graphInputPane, graphOutputPane;

    @FXML
    private TextField independentSetSizeInput, vertexCoverSizeInput;

    private SmartGraphPanel<String, String> inputGraphView, outputGraphView;

    private DigraphEdgeList<String, String> inputGraph, outputGraph;
    private SmartPlacementStrategy placementStrategy;

    public IsToVcScreenController() {
    }

    @Override
    public void runStage(Stage currentStage, String screenLocation) {
        try {
            InputStream fxmlStream = Main.class.getResourceAsStream(screenLocation);
            InputStream iconStream = Main.class.getResourceAsStream("icons/reductions_application_icon.png");

            // Initialize new Stage loader
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setController(this);

            // Set parameteres for our new stage
            Scene newScene = new Scene(fxmlLoader.load(fxmlStream));
//            newScene.getStylesheets().add(getClass().getResource("../styles/application_styles.css").toExternalForm());

            // Inherit style
            if (currentStage != null) {
                String oldStyle = currentStage.getScene().getRoot().getStyle();
                newScene.getRoot().setStyle(oldStyle);
            }

            this.inputGraphView = new SmartGraphPanel<>(inputGraph, placementStrategy);
            this.outputGraphView = new SmartGraphPanel<>(outputGraph, placementStrategy);

            this.graphInputPane.setCenter(this.inputGraphView);
            this.graphOutputPane.setCenter(this.outputGraphView);

            // Present screen
            this.stage = new Stage();
            stage.setScene(newScene);
            stage.getIcons().add(new Image(iconStream));
            stage.setMaximized(true);
            stage.show();

            inputGraphView.init();
            outputGraphView.init();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize two graphs
//        this.inputGraph = build_sample_digraph();
//        this.outputGraph = build_sample_digraph();

        this.inputGraph = new DigraphEdgeList<>();
        this.outputGraph = new DigraphEdgeList<>();

        // Initialize placement strategy for graphs
        this.placementStrategy = new SmartCircularSortedPlacementStrategy();
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
        this.reduction = (IsToVc) reduction;
    }

    @Override
    public void forwardOnly() throws Exception {

    }

    @Override
    public void forwardSolve() throws Exception {

    }

    @Override
    public void forwardSolveBackward() throws Exception {

    }

    @Override
    public void backwardOnly() throws Exception {

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
    protected void readInput() {

    }

    @Override
    protected void readSolution() {

    }

    @Override
    protected Reduction getReduction() {
        return this.reduction;
    }

    @Override
    void makeForwardStep() {
        this.outputGraphView.addNewGraph(inputGraphView, outputGraph);
    }

    @Override
    void makeForwardSolveStep() {

    }

    @Override
    void makeForwardSolveBackwardStep() {

    }

    @Override
    void makeBackwardStep() throws Exception {

    }

    @Override
    void initSteps() throws Exception {

    }


    @Override
    Stage getStage() {
        return this.stage;
    }

    @FXML
    public void resetEnvironment() {
        this.inputGraph.clearGraph();
        this.outputGraph.clearGraph();

        this.inputGraphView.update();
        this.outputGraphView.update();
    }
}
