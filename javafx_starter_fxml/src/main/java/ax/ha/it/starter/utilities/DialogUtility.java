package ax.ha.it.starter.utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class DialogUtility {

    public static void openAlertDialog(String content) { // TODO: Handle exit/cancel buttons
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setContentText(content);
        a.getDialogPane().getButtonTypes().add(ButtonType.OK);
        a.show();
    }

    public static String inputDialog(String title) { // TODO: Handle exit/cancel buttons
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setContentText(title);
        dialog.setHeaderText(null);
        dialog.setTitle(null);
        dialog.setGraphic(null);
        Optional<String> result =  dialog.showAndWait();
        return result.orElse(null);
    }
}
