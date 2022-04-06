package ru.spbstu.icst;

import javafx.application.Application;
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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.spbstu.icst.reductions.CnfTo3CnfReduction;
import ru.spbstu.icst.reductions.ProgramMode;
import ru.spbstu.icst.reductions.Reduction;

import java.net.URL;
import java.util.ResourceBundle;


public class Main extends Application implements Initializable {
    static Stage stg;

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("controllers/cnf_screen.fxml"));
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

        ObservableList<Reduction> reductionItems = FXCollections.observableArrayList(new CnfTo3CnfReduction());
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


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("start_screen.fxml"));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("reductions_application_icon.png")));

        primaryStage.setTitle("Search problems reductions");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}