package ru.spbstu.icst;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.spbstu.icst.controllers.StartScreenController;

import java.io.FileInputStream;
import java.io.InputStream;

public class StartScreen extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Locations of important resources
        String screenLocation = "StartScreen.fxml";
        String appIconLocation = "icons/reductions_application_icon.png";
        String stylesLocation = "styles/green_button.css";

        // Load resources for initializing start screen
        String startScreenLocation = getClass().getResource(screenLocation).getPath();
        InputStream iconStream = Main.class.getResourceAsStream(appIconLocation);
        String sceneStylesString = getClass().getResource(stylesLocation).toExternalForm();
        FileInputStream fxmlStream = new FileInputStream(startScreenLocation);

        // Check that resources loaded
        assert iconStream != null;

        // Initialize new Stage loader
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new StartScreenController(stage));

        // Initialize start screen
        Scene scene = new Scene(fxmlLoader.load(fxmlStream));
        scene.getStylesheets().add(sceneStylesString);

        // Finaly show start screen
        stage.setScene(scene);
        stage.getIcons().add(new Image(iconStream));
        stage.show();
    }

    public void run() {
        launch();
    }
}
