package ru.spbstu.icst.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class IsToVcScreenController extends Controller implements Initializable {

    @FXML
    private BorderPane graphBox;

    private SmartGraphPanel<String, String> graphView;

    public IsToVcScreenController() {
    }

    @Override
    public void runStage(Stage currentStage, String screenLocation) {
        try {
            FileInputStream fxmlStream = new FileInputStream(screenLocation);
            InputStream iconStream = Main.class.getResourceAsStream("reductions_application_icon.png");

            // Initialize new Stage loader
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setController(this);

            // Set parameteres for our new stage
            Scene newScene = new Scene(fxmlLoader.load(fxmlStream));
            newScene.getStylesheets().add(getClass().getResource("../styles/green_button.css").toExternalForm());

            // Inherit style
            if (currentStage != null) {
                String oldStyle = currentStage.getScene().getRoot().getStyle();
                newScene.getRoot().setStyle(oldStyle);
            }

            // Initialize graph
            Graph<String, String> g = build_sample_digraph();
            SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
            graphView = new SmartGraphPanel<>(g, strategy);
            this.graphBox.setCenter(graphView);

            // Present screen
            this.stage = new Stage();
            stage.setScene(newScene);
            stage.getIcons().add(new Image(iconStream));
            stage.show();

            graphView.init();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @FXML
    public void onCreateGraphButtonClicked() {
        System.out.println("Clicked");
        GraphInputController graphInputController = new GraphInputController();
        graphInputController.runStage(this.stage, "");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        return null;
    }


    @Override
    Stage getStage() {
        return this.stage;
    }

    @FXML
    public void resetEnvironment() {

    }

    @FXML
    public void makeStep() {

    }

}
