package ru.spbstu.icst;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.spbstu.icst.controllers.StartScreenController;

import java.io.InputStream;

/**
 * JavaFX GUI Application generates and starts from this class.
 * Instance of the class should be created and after this {@link StartScreen#run()}
 * must be called on created instance to start application.
 */
public class StartScreen extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Locations of important resources
        String screenLocation = "controllers/StartScreen.fxml";
        String appIconLocation = "icons/reductions_application_icon.png";
        String stylesLocation = "styles/application_styles.css";

        // Load resources for initializing start screen
        InputStream iconStream = Main.class.getResourceAsStream(appIconLocation);
//        String sceneStylesString = Main.class.getResource(stylesLocation).toExternalForm();
        InputStream fxmlStream = Main.class.getResourceAsStream(screenLocation);

        // Check that resources loaded
        assert iconStream != null;

        // Initialize new Stage loader
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new StartScreenController(stage));

        // Initialize start screen
        Scene scene = new Scene(fxmlLoader.load(fxmlStream));
        // TODO remove stylesheets and start using BootstrapFX
//        scene.getStylesheets().add(sceneStylesString);

        // Finaly show start screen
        stage.setScene(scene);
//        stage.setMaximized(true);
        stage.getIcons().add(new Image(iconStream));
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    /**
     * Starts application using underlying JavaFX methods.
     * Must be called on instance of {@link StartScreen} class.
     */
    public void run() {
        launch();
    }
}
