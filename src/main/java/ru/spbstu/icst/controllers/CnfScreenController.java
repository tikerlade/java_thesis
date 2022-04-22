package ru.spbstu.icst.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.spbstu.icst.Main;
import ru.spbstu.icst.reductions.CnfTo3CnfReduction;
import ru.spbstu.icst.reductions.ProgramMode;
import ru.spbstu.icst.reductions.Reduction;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class CnfScreenController extends Controller implements Initializable {

    // Components from FXML file
    @FXML
    private TextField formulaInput;

    @FXML
    private TextField solutionInput;

    @FXML
    private TextField outputFormula;

    @FXML
    private TextField solutionOutput;


    @FXML
    private TableColumn<Pair, String> leftColumn;

    @FXML
    private TableColumn<Pair, String> rightColumn;

    @FXML
    private TableView<Pair> table;

    private CnfTo3CnfReduction reduction;
    private List<Pair<String, String>> clausePairs;
    private Iterator<Pair<String, String>> clauseIterator;

    public CnfScreenController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resetEnvironment();
    }

    @FXML
    void makeStep(ActionEvent event) {
        switch (this.reduction.getReductionMode()) {
            case FORWARD_ONLY -> makeForwardStep();
            case FORWARD_SOLVE -> makeForwardSolveStep();
        }
    }

    @FXML
    void resetEnvironment() {
        // Enable input form
        formulaInput.setDisable(false);

        // Enable solution field if required
        if (reduction.getReductionMode() == ProgramMode.BACKWARD_ONLY) {
            solutionInput.setDisable(false);
        }

        // Empty columns of table
        table.getItems().clear();

        // Initialize columns of table
        leftColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        rightColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Empty solution
        outputFormula.setText("");
        solutionOutput.setText("");
        

        // Update buttons states
        startButton.setDisable(false);
        resetButton.setDisable(true);
    }

    @FXML
    void backFromReduction() {
        // Load class which will control UI
        StartScreenController controller = new StartScreenController();
        String screenLocation = Main.class.getResource("start_screen.fxml").getPath();
        controller.runStage(this.stage, screenLocation);
        this.stage.hide();
    }


    private void makeForwardStep() {
        if (this.clauseIterator.hasNext()) {
            table.getItems().add(this.clauseIterator.next());
            table.scrollTo(table.getItems().size()-1);

            if (!this.clauseIterator.hasNext()) {
                outputFormula.setText(this.reduction.getReducedInput());
                stepButton.setDisable(true);
                resetButton.setDisable(false);
            }
        }
    }

    private void makeForwardSolveStep() {
        makeForwardStep();

        if (!this.clauseIterator.hasNext()) {
            // Run solve function
            this.reduction.solveProblemB();

            // Present results
            String solution = this.reduction.getProblemBStringSolution();
            this.solutionOutput.setText(solution);
        }
    }

    @Override
    public void forwardOnly() throws Exception {
        super.forwardOnly();

        // Get required steps
        this.clausePairs = this.reduction.createSteps();
        this.clauseIterator = this.clausePairs.iterator();

        // If any of steps exists we can go with them
        if (this.clauseIterator.hasNext()) {
            stepButton.setDisable(false);
            startButton.setDisable(true);
            formulaInput.setDisable(true);
            this.makeStep(new ActionEvent());
        }
    }

    @Override
    public void forwardSolve() throws Exception {
        // First of all run forward only
        this.forwardOnly();
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
    protected void solveProblemA() {

    }

    @Override
    protected void runBackwardSteps() {

    }

    @Override
    protected void readInput() {
        String input = formulaInput.getText();

        try {
            this.reduction.readInputFromString(input);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    protected void readSolution() {
        String solution = solutionInput.getText();

        try {
            this.reduction.readSolutionFromString(solution);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    protected Reduction getReduction() {
        return this.reduction;
    }

    @Override
    public void setReduction(Reduction reduction) {
        this.reduction = (CnfTo3CnfReduction) reduction;
    }


    @Override
    Stage getStage() {
        return this.stage;
    }

}
