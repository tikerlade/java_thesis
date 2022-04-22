package ru.spbstu.icst.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.spbstu.icst.reductions.IsToVc;
import ru.spbstu.icst.reductions.Reduction;

import java.net.URL;
import java.util.ResourceBundle;

public class IsToVcScreenController extends Controller implements Initializable {
    @FXML
    private Label isConnectedLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.isConnectedLabel.setText("Connected");
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
    protected void solveProblemA() {

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

}
