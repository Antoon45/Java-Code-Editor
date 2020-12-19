package ax.ha.it.starter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AppController {

    public VBox root;
    public ScrollPane directory;
    public ScrollPane codeArea;
    public TabPane tabs;
    int tabNum = 2;

    @FXML TabPane tabby;

    @FXML
    private void closeButton(){
        System.exit(0);
    }

    @FXML
    private void addNewTab() {
        final Tab tab = new Tab("Untitled Tab" + tabNum);
        tabby.getTabs().add(tab);
        tabby.getSelectionModel().select(tab);
        tabNum++;
    }
}

