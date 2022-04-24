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
import java.util.ResourceBundle;

public class CnfScreenController extends Controller implements Initializable {

    // Components from FXML file
    @FXML
    private TextField formulaInput, convertedFormula, solutionForConvertedFormula, reducedSolutionOutput;

    @FXML
    private TableColumn<Pair, String> leftColumn, rightColumn;

    @FXML
    private TableView<Pair> table;

    private CnfTo3CnfReduction reduction;
    private Iterator<Pair<String, String>> clauseIterator;

    public CnfScreenController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resetEnvironment();
    }

    @FXML
    void makeStep(ActionEvent event) throws Exception {
        this.initSteps();

        switch (this.reduction.getReductionMode()) {
            case FORWARD_ONLY -> makeForwardStep();
            case FORWARD_SOLVE -> makeForwardSolveStep();
            case FORWARD_SOLVE_BACKWARD -> makeForwardSolveBackwardStep();
            case BACKWARD_ONLY -> makeBackwardStep();
        }
    }

    private void makeBackwardStep() throws Exception {
        if (this.clauseIterator.hasNext()) {
            // Initialize table of conversions
            while (this.clauseIterator.hasNext()) {
                table.getItems().add(this.clauseIterator.next());
            }
            table.scrollTo(table.getItems().size() - 1);

            // Print converted problem
            convertedFormula.setText(this.reduction.getReducedInput());
        } else {
            // Read users solution of problem B and parse it
            String problemBSolution = this.solutionForConvertedFormula.getText();
            this.reduction.readSolutionFromString(problemBSolution);

            // Do backward reduction
            this.reduction.backward();

            // Present result to user
            String solutionForA = this.reduction.getProblemA().getStringSolution();
            this.reducedSolutionOutput.setText(solutionForA);
            this.stepButton.setDisable(true);
            this.resetButton.setDisable(false);
        }
    }

    private void makeForwardSolveBackwardStep() {
        // Make forward steps while we can
        if (this.clauseIterator.hasNext()) {
            this.makeForwardSolveStep();

            if (!this.clauseIterator.hasNext()) {
                this.stepButton.setDisable(false);
            }
        } else {
            this.reduction.backward();
            String solutionForA = this.reduction.getProblemA().getStringSolution();
            this.reducedSolutionOutput.setText(solutionForA);
            this.stepButton.setDisable(true);
        }
    }

    @FXML
    void resetEnvironment() {
        // Empty iterator and clean reduction
        this.reduction.resetProblems();
        this.clauseIterator = null;

        // Enable input form
        formulaInput.setDisable(false);

        // Enable solution field if required
        if (reduction.getReductionMode() == ProgramMode.BACKWARD_ONLY) {
            // Enable field to input solution
            this.solutionForConvertedFormula.setDisable(false);
            this.solutionForConvertedFormula.setEditable(true);
        }

        // Empty columns of table
        table.getItems().clear();

        // Initialize columns of table
        leftColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        rightColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Empty solution
        convertedFormula.setText("");
        solutionForConvertedFormula.setText("");
        reducedSolutionOutput.setText("");

        // Update buttons states
        resetButton.setDisable(true);
        stepButton.setDisable(false);
    }

    @FXML
    void backFromReduction() {
        // Load class which will control UI
        StartScreenController controller = new StartScreenController();
        String screenLocation = Main.class.getResource("StartScreen.fxml").getPath();
        controller.runStage(this.stage, screenLocation);
        this.stage.hide();
    }


    private void makeForwardStep() {
        if (this.clauseIterator.hasNext()) {
            table.getItems().add(this.clauseIterator.next());
            table.scrollTo(table.getItems().size()-1);

            if (!this.clauseIterator.hasNext()) {
                convertedFormula.setText(this.reduction.getReducedInput());
                stepButton.setDisable(true);
                resetButton.setDisable(false);
            }
        }
    }

    private void makeForwardSolveStep() {
        if (!this.clauseIterator.hasNext()) {
            // Run solve function
            this.reduction.solveProblemB();

            // Present results
            String solution = this.reduction.getProblemBStringSolution();
            this.solutionForConvertedFormula.setText(solution);
            this.stepButton.setDisable(true);
        } else {
            makeForwardStep();
            if (!this.clauseIterator.hasNext()) {
                this.stepButton.setDisable(false);
            }
        }
    }

    @Override
    public void forwardOnly() throws Exception {
        super.forwardOnly();

        // If any of steps exists we can go with them
        if (this.clauseIterator.hasNext()) {
            stepButton.setDisable(false);
            formulaInput.setDisable(true);
            this.makeStep(new ActionEvent());
        }
    }

    private void initSteps() throws Exception {
        // Get required steps
        if (this.clauseIterator == null) {
            this.readInput();
            this.clauseIterator = this.reduction.createSteps().iterator();
        }
    }

    @Override
    public void forwardSolve() throws Exception {
        // First of all run forward only
        this.forwardOnly();
    }

    @Override
    public void forwardSolveBackward() throws Exception {
        // First of all run forward only
        this.forwardOnly();
        this.readSolution();
        System.out.println("H");

    }

    @Override
    public void backwardOnly() throws Exception {

    }

    @Override
    protected void runForwardSteps() {

    }

    @Override
    protected void solveProblemB() {
        this.reduction.solveProblemB();

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
        String solution = solutionForConvertedFormula.getText();

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
