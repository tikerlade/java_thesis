package ru.spbstu.icst.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.spbstu.icst.Main;
import ru.spbstu.icst.reductions.*;

import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenController implements Initializable {
    @FXML
    private ComboBox<ProgramMode> modeCombo;

    @FXML
    private ComboBox<Reduction> reductionCombo;

    @FXML
    private Button runButton;

    @FXML
    void runReduction(ActionEvent event) throws Exception {
        Reduction selectedReduction = reductionCombo.getValue();
        ProgramMode mode = modeCombo.getValue();

        try {
            String fxmlLocation = selectedReduction.getScreenLocation();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlLocation));

            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Main.stg.close();
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
                new IsToVc(),
                new IsToClique()
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

        ObservableList<ProgramMode> modeItems = FXCollections.observableArrayList(ProgramMode.FORWARD_ONLY);
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
            this.runButton.setDisable(false);
        }
    }

    @FXML
    void onReductionComboChanged(ActionEvent event) {
        // If other combobox was selected - enable run button
        if (this.modeCombo.getValue() != null) {
            this.runButton.setDisable(false);
        }
    }
}
