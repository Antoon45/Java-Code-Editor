package ax.ha.it.starter.utilities;

import ax.ha.it.starter.Editor;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class FileUtility {

    private File currentFile;
    private Editor currentEditor;

    public FileUtility() {
    }

    @Nullable
    public File openFileInExplorer(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(null);
    }

    @Nullable
    public File openSaveFileExplorer(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showSaveDialog(null);
    }

    public String getName() {
        return currentFile.getName();
    }

    public String getPath() {
        return currentFile.getPath();
    }

    public void setEditor(Editor editor) {
        currentEditor = editor;
    }

    public Editor getEditor() {
        return currentEditor;
    }

    public File getFile() {
        return currentFile;
    }

    public void updateFile(File newFile) {
        currentFile = newFile;
    }

    @Override
    public String toString() {
        return getName();
    }
}
