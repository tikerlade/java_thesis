package ru.spbstu.icst.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.spbstu.icst.Main;
import ru.spbstu.icst.reductions.Reduction;

import java.io.*;

public abstract class Controller {

    @FXML
    private MenuItem darkThemeMenuItem, aboutMenuItem;

    @FXML
    protected Button startButton, stepButton, resetButton;

    protected Reduction reduction;
    public Stage stage;

    public abstract void setReduction(Reduction reduction);

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

            this.stage = new Stage();
            stage.setScene(newScene);
            stage.getIcons().add(new Image(iconStream));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Entry point into actual algorithm.
     * From here we decide which parts of reduction we want to complete.
     * @throws Exception if there are problems with parsing input data.
     */
    @FXML
    void startReduction(ActionEvent event) throws Exception {
        switch (this.getReduction().getReductionMode()) {
            case FORWARD_ONLY -> {
                this.forwardOnly();
            }

            case FORWARD_SOLVE -> {
                this.forwardSolve();
            }

            case FORWARD_SOLVE_BACKWARD -> {
                this.forwardSolveBackward();
            }

            case BACKWARD_ONLY -> {
                this.backwardOnly();
            }
        }
    }


    /**
     * Wrapper function for FORWARD_ONLY mode to read user data and after that
     * execute clean forward reduction.
     * @throws Exception if there are problems with parsing input data.
     */
    public void forwardOnly() throws Exception {
        this.readInput();
        this.runForwardSteps();
    }



    /**
     * Wrapper function for FORWARD_SOLVE mode to read user data and after that
     * execute clean forward reduction + solve reduced problem.
     * @throws Exception if there are problems with parsing input data.
     */
    public void forwardSolve() throws Exception {
        this.forwardOnly();
        this.solveProblemB();
    }

    /**
     * Wrapper function for FORWARD_SOLVE_BACKWARD mode to read user data and after that
     * execute clean forward reduction + solve reduced problem + backward reduction.
     * @throws Exception if there are problems with parsing input data.
     */
    public void forwardSolveBackward() throws Exception {
        this.forwardSolve();
        this.runBackwardSteps();
    }


    /**
     * Wrapper function for BACKWARD_ONLY mode to read user data and after that
     * execute clean backward reduction.
     * @throws Exception if there are problems with parsing input data.
     */
    public void backwardOnly() throws Exception {
        this.readInput();
        this.readSolution();
        // TODO init solved environment
        this.runBackwardSteps();
    }


    @FXML
    void onDarkThemeMenuItemSelected(ActionEvent event) {
        // TODO add preferences file and change state there

        if (darkThemeMenuItem.getText().equals("Dark Theme")) {
            // Move system into dark mode
            this.stage.getScene().getRoot().setStyle("-fx-base:#3f474f");

            // Change menu item for changing into white mode
            darkThemeMenuItem.setText("Light Theme");
        } else {
            // Move system into dark mode
            Scene currentScene = this.stage.getScene();
            currentScene.getRoot().setStyle("");

            // Change menu item for changing into white mode
            darkThemeMenuItem.setText("Dark Theme");
        }
    }


    @FXML
    void onAboutMenuItemSelected(ActionEvent event) throws IOException {
        // Load page from fxml file
        String screenLocation = getClass().getResource("AboutScreen.fxml").getPath();
        String appIconLocation = "icons/reductions_application_icon.png";

        InputStream iconStream = Main.class.getResourceAsStream(appIconLocation);
        FileInputStream fxmlStream = new FileInputStream(screenLocation);

        FXMLLoader fxmlLoader = new FXMLLoader();

        // Create new temporary Stage
        Stage aboutWindow = new Stage();
        aboutWindow.initModality(Modality.APPLICATION_MODAL);
        aboutWindow.setTitle("About");
        aboutWindow.setResizable(false);
        aboutWindow.getIcons().add(new Image(iconStream));

        // Setting Scene for window
        Scene aboutScene = new Scene(fxmlLoader.load(fxmlStream));
        String currentStyle = this.getStage().getScene().getRoot().getStyle();
        aboutScene.getRoot().setStyle(currentStyle);
        aboutWindow.setScene(aboutScene);

        // Show page and wait until it will be closed
        aboutWindow.showAndWait();
    }

    public void createExceptionAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error Dialog");
        alert.setHeaderText(e.getMessage());
        alert.setContentText(e.toString());

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    abstract Stage getStage();


    protected abstract void runForwardSteps();
    protected abstract void solveProblemB();
    protected abstract void runBackwardSteps();

    protected abstract void readInput() throws Exception;

    protected abstract void readSolution() throws Exception;

    protected abstract Reduction getReduction();
}
