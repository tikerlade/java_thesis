package ru.spbstu.icst.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.spbstu.icst.reductions.CnfTo3CnfReduction;
import ru.spbstu.icst.reductions.IsToVc;
import ru.spbstu.icst.reductions.ProgramMode;
import ru.spbstu.icst.reductions.Reduction;

import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenController extends Controller implements Initializable {
    @FXML
    private ComboBox<ProgramMode> modeCombo;

    @FXML
    private ComboBox<Reduction> reductionCombo;


    public StartScreenController() {}

    public StartScreenController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void runReduction(ActionEvent event) throws Exception {
        // Retrieve combination which user selected
        Reduction selectedReduction = reductionCombo.getValue();
        ProgramMode mode = modeCombo.getValue();

        // Set selected mode for selected reduction
        selectedReduction.setReductionMode(mode);

        try {
            // Load class which will control UI
            Controller controller = selectedReduction.getScreenController();
            controller.setReduction(selectedReduction);

            String screenLocation = getClass().getResource(selectedReduction.getScreenFilename()).getPath();
            controller.runStage(this.stage, screenLocation);
            this.stage.hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeReductionCombo() {
        reductionCombo.getItems().removeAll(reductionCombo.getItems());
        reductionCombo.setConverter(new StringConverter<Reduction>() {
            @Override
            public String toString(Reduction object) {
                return object.toString();
            }

            @Override
            public Reduction fromString(String string) {
                return null;
            }
        });

        ObservableList<Reduction> reductionItems = FXCollections.observableArrayList(
                new CnfTo3CnfReduction(),
                new IsToVc()
//                new IsToClique()
        );
        reductionCombo.setItems(reductionItems);
    }

    private void initializeModeCombo() {
        modeCombo.getItems().removeAll(modeCombo.getItems());
        modeCombo.setConverter(new StringConverter<ProgramMode>() {
            @Override
            public String toString(ProgramMode object) {
                return object.toString();
            }

            @Override
            public ProgramMode fromString(String string) {
                return null;
            }
        });

        ObservableList<ProgramMode> modeItems = FXCollections.observableArrayList(
                ProgramMode.FORWARD_ONLY,
                ProgramMode.FORWARD_SOLVE,
                ProgramMode.FORWARD_SOLVE_BACKWARD,
                ProgramMode.BACKWARD_ONLY);

        modeCombo.setItems(modeItems);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeReductionCombo();
        initializeModeCombo();
    }


    @FXML
    void onModeComboChanged(ActionEvent event) {
        // If other combobox was selected - enable run button
        if (this.reductionCombo.getValue() != null) {
            this.startButton.setDisable(false);
        }
    }

    @FXML
    void onReductionComboChanged(ActionEvent event) {
        // If other combobox was selected - enable run button
        if (this.modeCombo.getValue() != null) {
            this.startButton.setDisable(false);
        }
    }


    @Override
    public void setReduction(Reduction reduction) {
        this.reduction = (CnfTo3CnfReduction) reduction;
    }

    @Override
    Stage getStage() {
        return this.stage;
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

}
