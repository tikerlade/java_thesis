package ru.spbstu.icst;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.spbstu.icst.controllers.StartScreenController;


public class Main extends Application {
    public static Stage stg;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StartScreenController controller = new StartScreenController();
        String startScreenLocation = getClass().getResource("start_screen.fxml").getPath();
        controller.runStage(null, startScreenLocation);





//        // Store current stage as static variable to hide it in future
//        stg = primaryStage;
//
//        // Load resources for initializing start screen
//        URL startScreenLocation = getClass().getResource("start_screen.fxml");
//        InputStream iconStream = Main.class.getResourceAsStream("reductions_application_icon.png");
//
//        // Check that resources loaded
//        assert startScreenLocation != null;
//        assert iconStream != null;
//
//        // Initialize start screen
//        Parent root = FXMLLoader.load(startScreenLocation);
//        primaryStage.getIcons().add(new Image(iconStream));
//
//        // Present start screen
//        primaryStage.setTitle("Search problems reductions");
//        primaryStage.setScene(new Scene(root, 800, 600));
//        primaryStage.show();
    }
}