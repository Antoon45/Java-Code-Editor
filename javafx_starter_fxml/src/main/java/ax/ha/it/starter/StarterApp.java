package ax.ha.it.starter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 * FXML edition
 */
public class StarterApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StarterApp.class.getResource("layout.fxml"));

        // Note: Exclude width/height params to only use as much space as needed
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        scene.getStylesheets().add
                (AppController.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}