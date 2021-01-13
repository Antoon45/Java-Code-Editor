package ax.ha.it.starter.utilities;

import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class FileUtility {

    @Nullable
    public static File openFileInExplorer(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(null);
    }

    @Nullable
    public static File openSaveFileExplorer(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showSaveDialog(null);
    }
}
