package ru.spbstu.icst.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.spbstu.icst.exceptions.InputException;
import ru.spbstu.icst.exceptions.SolutionNotFoundException;
import ru.spbstu.icst.reductions.CnfTo3CnfReduction;
import ru.spbstu.icst.reductions.Reduction;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.ResourceBundle;

public class CnfScreenController extends Controller implements Initializable {

    // Components from FXML file
    @FXML
    private TextField formulaInput, convertedFormula, solutionForConvertedFormula, reducedSolutionOutput;

    @FXML
    private MenuItem loadInputMenuIteam, loadSolutionMenuItem, saveReducedInputMenuItem,
            saveReducedSolutionMenuItem, saveInitialSolutionMenuItem;

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


    protected void makeBackwardStep() throws Exception {
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
            this.saveInitialSolutionMenuItem.setDisable(false);
        }
    }

    protected void makeForwardSolveBackwardStep() throws SolutionNotFoundException, InputException {
        // Make forward steps while we can
        if (this.clauseIterator.hasNext()) {
            this.makeForwardSolveStep();

            if (!this.clauseIterator.hasNext()) {
                this.stepButton.setDisable(false);
            }
        } else {
            if (this.solutionForConvertedFormula.getText().isEmpty()) {
                this.makeForwardSolveStep();
                this.stepButton.setDisable(false);
            } else {
                this.reduction.backward();
                String solutionForA = this.reduction.getProblemA().getStringSolution();
                this.reducedSolutionOutput.setText(solutionForA);
                this.stepButton.setDisable(true);
            }
        }
    }

    @FXML
    void resetEnvironment() {
        // Empty iterator and clean reduction
        this.reduction.resetProblems();
        this.clauseIterator = null;

        // Disable save menu items because fields are empty
        this.saveReducedInputMenuItem.setDisable(true);
        this.saveReducedSolutionMenuItem.setDisable(true);
        this.saveInitialSolutionMenuItem.setDisable(true);

        // Enable input form
        formulaInput.setDisable(false);

        // Enable solution field if required
        switch (reduction.getReductionMode()) {
            case FORWARD_SOLVE -> {
                this.solutionForConvertedFormula.setDisable(false);
            }
            case FORWARD_SOLVE_BACKWARD -> {
                this.solutionForConvertedFormula.setDisable(false);
                this.reducedSolutionOutput.setDisable(false);
            }
            case BACKWARD_ONLY -> {
                this.solutionForConvertedFormula.setEditable(true);
                this.solutionForConvertedFormula.setDisable(false);
                this.reducedSolutionOutput.setDisable(false);
                this.loadSolutionMenuItem.setDisable(false);
            }
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
//        resetButton.setDisable(true);
        stepButton.setDisable(false);
    }

    protected void makeForwardStep() {
        if (this.clauseIterator.hasNext()) {
            table.getItems().add(this.clauseIterator.next());
            table.scrollTo(table.getItems().size()-1);

            if (!this.clauseIterator.hasNext()) {
                convertedFormula.setText(this.reduction.getReducedInput());
                stepButton.setDisable(true);
                this.saveReducedInputMenuItem.setDisable(false);
            }
        }
    }

    protected void makeForwardSolveStep() throws InputException, SolutionNotFoundException {
        if (!this.clauseIterator.hasNext()) {
            // Run solve function
            this.reduction.solveProblemB();

            // Present results
            String solution = this.reduction.getProblemBStringSolution();
            this.solutionForConvertedFormula.setText(solution);
            this.stepButton.setDisable(true);
            this.saveReducedSolutionMenuItem.setDisable(false);
        } else {
            makeForwardStep();
            if (!this.clauseIterator.hasNext()) {
                this.stepButton.setDisable(false);
            }
        }
    }

//    @Override
//    public void forwardOnly() throws Exception {
//        super.forwardOnly();
//
//        // If any of steps exists we can go with them
//        if (this.clauseIterator.hasNext()) {
//            stepButton.setDisable(false);
//            formulaInput.setDisable(true);
//            this.makeStep(new ActionEvent());
//        }
//    }

    protected void initSteps() throws Exception {
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
    protected void solveProblemB() throws InputException, SolutionNotFoundException {
        this.reduction.solveProblemB();

    }

    @Override
    protected void runBackwardSteps() {

    }

    @Override
    protected void readInput() throws Exception {
        String input = formulaInput.getText();
        this.reduction.readInputFromString(input);
    }

    @Override
    protected void readSolution() throws Exception {
        String solution = solutionForConvertedFormula.getText();
        this.reduction.readSolutionFromString(solution);
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

    private String readUserDataGui() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            return Files.readString(Path.of(selectedFile.getPath()));
        }

        return "";
    }

    private void saveUserDataGui(String output) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output");
        File selectedFile = fileChooser.showSaveDialog(stage);

        try {
            Files.writeString(Path.of(selectedFile.getPath()), output);
        } catch (IOException e) {
            this.createExceptionAlert(e);
        }
    }

    @FXML
    void onLoadInputMenuIteamSelected() {
        try {
            String userInput = this.readUserDataGui();

            if (!userInput.isEmpty()) {
                this.formulaInput.setText(userInput);
            }
        } catch (IOException e) {
            this.createExceptionAlert(e);
        }
    }

    @FXML
    void onLoadSolutionMenuIteamSelected() {
        try {
            String userSolution = this.readUserDataGui();

            if (!userSolution.isEmpty()) {
                this.solutionForConvertedFormula.setText(userSolution);
            }
        } catch (IOException e) {
            this.createExceptionAlert(e);
        }
    }

    @FXML
    void onSaveReducedInputMenuIteamSelected() {
        String reducedInput = this.convertedFormula.getText();
        this.saveUserDataGui(reducedInput);
    }

    @FXML
    void onSaveReducedSolutionMenuItemSelected() {
        String solutionForReducedInput = this.solutionForConvertedFormula.getText();
        this.saveUserDataGui(solutionForReducedInput);
    }

    @FXML
    void onSaveInitialSolutionMenuItemSelected() {
        String solutionForInitialInput = this.reducedSolutionOutput.getText();
        this.saveUserDataGui(solutionForInitialInput);
    }
}
