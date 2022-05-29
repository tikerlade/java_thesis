package ru.spbstu.icst.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.spbstu.icst.Main;
import ru.spbstu.icst.exceptions.InputException;
import ru.spbstu.icst.problems.IndependentSet;
import ru.spbstu.icst.problems.VertexCover;
import ru.spbstu.icst.reductions.IsToVc;
import ru.spbstu.icst.reductions.Reduction;
import ru.spbstu.icst.smartgraph.graph.DigraphEdgeList;
import ru.spbstu.icst.smartgraph.graph.Graph;
import ru.spbstu.icst.smartgraph.graph.Vertex;
import ru.spbstu.icst.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import ru.spbstu.icst.smartgraph.graphview.SmartGraphPanel;
import ru.spbstu.icst.smartgraph.graphview.SmartPlacementStrategy;

import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class IsToVcScreenController extends Controller implements Initializable {

    @FXML
    private BorderPane graphInputPane, graphOutputPane;

    @FXML
    private TextField independentSetSizeInput, vertexCoverSizeInput;

    @FXML
    private Label stepInformation;

    private SmartGraphPanel<String, String> inputGraphView, outputGraphView;

    private DigraphEdgeList<String, String> inputGraph, outputGraph;
    private SmartPlacementStrategy placementStrategy;

    private IsToVc reduction;
    private boolean reduced;
    private boolean solvedB;

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
        this.inputGraph = new DigraphEdgeList<>();
        this.outputGraph = new DigraphEdgeList<>();

        // Initialize placement strategy for graphs
        this.placementStrategy = new SmartCircularSortedPlacementStrategy();

        // Prepare all fields to given reduction ProgramMode
        this.resetEnvironment();
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
    protected void readInput() throws InputException {
        int independentSetSize;

        // Read desired size of independent set
        try {
            independentSetSize = Integer.parseInt(this.independentSetSizeInput.getText());
        } catch (Exception e) {
            throw new InputException("Size of independent set must be specified.");
        }

        // Here we'll read graph
        Graph<String, String> inputGraph = this.inputGraphView.getGraph();

        // Convert this graph into my own representation of graph
        ru.spbstu.icst.problems.Graph g = new ru.spbstu.icst.problems.Graph(inputGraph);

        if (g.isEmpty()) {
            throw new InputException("You must enter graph.");
        }

        IndependentSet problemA = new IndependentSet(independentSetSize, g);
        this.reduction.setProblemA(problemA);
    }

    @Override
    protected void readSolution() {
        int vertexCoverSize = Integer.parseInt(this.vertexCoverSizeInput.getText());

        // Here we'll read graph
        Graph<String, String> outputGraph = this.outputGraphView.getGraph();
    }

    @Override
    protected Reduction getReduction() {
        return this.reduction;
    }

    @Override
    void makeForwardStep() {
        // Complete forward reduction step
        this.reduction.forward();

        // Add information on screen
        int vertexCoverSize = ((VertexCover) this.reduction.getProblemB()).getCoverSize();
        this.vertexCoverSizeInput.setText(String.valueOf(vertexCoverSize));
        this.outputGraphView.addNewGraph(inputGraphView, outputGraph);

        this.reduced = true;
        this.stepButton.setDisable(true);

        // Lock changing of input
        this.graphInputPane.setDisable(true);
        this.independentSetSizeInput.setDisable(true);

    }


    @Override
    void makeForwardSolveStep() {
        if (reduced) {
            // Run solve function
            this.reduction.solveProblemB();

            // Present results
            VertexCover vertexCoverProblem = (VertexCover) this.reduction.getProblemB();
            HashSet<Integer> vertexCover = (HashSet<Integer>) vertexCoverProblem.vertexCover;
            HashSet<String> vertexCoverLabels = vertexCover.stream().map(Object::toString).collect(Collectors.toCollection(HashSet::new));

            // Find vertices to color by matching them from one graph to another
            for (Vertex<String> vertex : outputGraph.vertices()) {
                if (vertexCoverLabels.contains(vertex.element())) {
                    this.outputGraphView.colorVertex(vertex);
                }
            }

            this.solvedB = true;
            this.stepButton.setDisable(true);
        } else {
            makeForwardStep();
            this.stepButton.setDisable(false);
        }
    }

    @Override
    void makeForwardSolveBackwardStep() {
        if (!reduced) {
            this.makeForwardSolveStep();

            if (reduced) {
                this.stepButton.setDisable(false);
            }
        } else {
            if (!solvedB) {
                this.makeForwardSolveStep();
                this.stepButton.setDisable(false);
            } else {
                this.reduction.backward();

                // Get independent set
                IndependentSet independentSetProblem = (IndependentSet) this.reduction.getProblemA();
                HashSet<Integer> independentSet = (HashSet<Integer>) independentSetProblem.independentSet;
                HashSet<String> independentSetLabels = independentSet.stream().map(Object::toString).collect(Collectors.toCollection(HashSet::new));

                // Find vertices to color by matching them from one graph to another
                for (Vertex<String> vertex : inputGraph.vertices()) {
                    if (independentSetLabels.contains(vertex.element())) {
                        this.inputGraphView.colorVertex(vertex);
                    }
                }

                this.stepButton.setDisable(true);
            }
        }
    }

    @Override
    void makeBackwardStep() throws Exception {
        if (!reduced) {
            this.makeForwardSolveStep();

            if (reduced) {
                this.stepButton.setDisable(false);
                this.graphOutputPane.setDisable(false);
            }
        } else {
            VertexCover vertexCoverProblem = (VertexCover) this.reduction.getProblemB();

            // Read user input here
            // Idea is the following: add double click listener.
            // On select: new vertex added to list of selected nodes; and vice versa
            HashSet<Vertex<String>> selectedVertexCover = outputGraphView.getSelectedVertices();

            // Check that vertices create correct vertex cover and move them into solution
            HashSet<Integer> selectedVerticesInts = selectedVertexCover.stream()
                    .map(x -> Integer.parseInt(x.element())).collect(Collectors.toCollection(HashSet::new));
            vertexCoverProblem.setVertexCover(selectedVerticesInts);
            this.reduction.setProblemB(vertexCoverProblem);

            this.reduction.backward();
            // Get independent set
            IndependentSet independentSetProblem = (IndependentSet) this.reduction.getProblemA();
            HashSet<Integer> independentSet = (HashSet<Integer>) independentSetProblem.independentSet;
            HashSet<String> independentSetLabels = independentSet.stream().map(Object::toString).collect(Collectors.toCollection(HashSet::new));

            // Find vertices to color by matching them from one graph to another
            for (Vertex<String> vertex : inputGraph.vertices()) {
                if (independentSetLabels.contains(vertex.element())) {
                    this.inputGraphView.colorVertex(vertex);
                }
            }

            this.stepButton.setDisable(true);
        }
    }

    @Override
    void initSteps() throws Exception {
        this.readInput();
    }


    @Override
    Stage getStage() {
        return this.stage;
    }

    @FXML
    public void resetEnvironment() {
        // Clean reduction
        this.reduction.resetProblems();

        // Clear graphs if required
        if (this.inputGraphView != null && this.outputGraphView != null) {
            this.inputGraph.clearGraph();
            this.outputGraph.clearGraph();

            this.inputGraphView.resetCounters();
            this.outputGraphView.resetCounters();

            this.inputGraphView.update();
            this.outputGraphView.update();
        }

        // Clear inputs
        this.independentSetSizeInput.setDisable(false);
        this.independentSetSizeInput.clear();

        // Clear outputs
        this.vertexCoverSizeInput.setDisable(true);
        this.vertexCoverSizeInput.clear();

        this.solvedB = false;
        this.reduced = false;

        // Enable input graph form
        graphInputPane.setDisable(false);
        graphOutputPane.setDisable(true);
        stepButton.setDisable(false);

        // Add information about next step required
        this.stepInformation.setText("Input graph and desired size of Independant Set:");
    }

}
