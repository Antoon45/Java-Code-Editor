package ax.ha.it.starter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Application logic goes here
 */
public class StarterController {

    @FXML
    private void sayHello() {
        Alert alert = new Alert(  Alert.AlertType.INFORMATION,
                "Hello JavaFX!",
                ButtonType.OK);
        alert.setHeaderText(null); // IMHO JavaFX dialog headers look really stupid
        alert.showAndWait();
    }
}
