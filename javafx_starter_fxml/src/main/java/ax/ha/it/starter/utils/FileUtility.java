package ax.ha.it.starter.utils;

import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class FileUtility {

    @Nullable
    public static File openFileInExplorer(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(null);
    }
}
