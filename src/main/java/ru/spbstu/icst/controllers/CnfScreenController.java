package ru.spbstu.icst.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import ru.spbstu.icst.problems.CnfFormula;
import ru.spbstu.icst.reductions.CnfTo3CnfReduction;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class CnfScreenController implements Initializable {

    // Components from FXML file


    @FXML
    private TextField formulaInput;

    @FXML
    private TextField outputFormula;

    @FXML
    private Button startButton;

    @FXML
    private Button stepButton;

    @FXML
    private Button resetButton;

    @FXML
    private TableColumn<Pair, String> leftColumn;

    @FXML
    private TableColumn<Pair, String> rightColumn;

    @FXML
    private TableView<Pair> table;

    private CnfTo3CnfReduction reduction;
    private List<Pair<String, String>> clausePairs;
    private Iterator<Pair<String, String>> clauseIterator;

    public CnfScreenController() {
        this.reduction = new CnfTo3CnfReduction();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        rightColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }


    ObservableList<Pair> getItems() {
        ObservableList<Pair> items = FXCollections.observableArrayList();

        items.add(new Pair("(a & b)", "(a & b"));
        items.add(new Pair("(a & b & c)", "(a & b"));
        items.add(new Pair("(b)", "(b"));
        items.add(new Pair("(a & b)", "(a & b"));
        items.add(new Pair("(a & b & c)", "(a & b"));
        items.add(new Pair("(b)", "(b"));

        return items;
    }



    @FXML
    void makeStep(ActionEvent event) {
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

    @FXML
    void startReduction(ActionEvent event) {
        String inputFormula = formulaInput.getText();

        try {
            this.reduction = new CnfTo3CnfReduction(new CnfFormula(inputFormula));
            this.clausePairs = this.reduction.createSteps();
            this.clauseIterator = this.clausePairs.iterator();

            if (this.clauseIterator.hasNext()) {
                stepButton.setDisable(false);
                startButton.setDisable(true);
                formulaInput.setDisable(true);

                makeStep(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resetEnvironment(ActionEvent event) {

    }
}
