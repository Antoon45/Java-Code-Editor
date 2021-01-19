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
    /**
     * Dialog to open requested file
     * @param title
     * @return fileChooser.showOpenDialog(null);
     */
    @Nullable
    public File openFileInExplorer(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(null);
    }

    /**
     * opens dialog to Save the file you are working on
     * @param title
     * @return fileChooser.showSaveDialog(null);
     */
    @Nullable
    public File openSaveFileExplorer(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showSaveDialog(null);
    }


    //returns name of the file opened

    /**
     * returns the name of the file opened
     * @return name of the current file
     */
    public String getName() {
        return currentFile.getName();
    }

    /**
     * Gets the path of the file
     * @return the tree path of current file
     */
    public String getPath() {
        return currentFile.getPath();
    }

    /**
     * sets the editor depending on which tab is showing
     * @param editor
     */
    public void setEditor(Editor editor) {
        currentEditor = editor;
    }


    /**
     * checks which editor is the current one
     * @return whi
     */
    public Editor getEditor() {
        return currentEditor;
    }

    public File getFile() {
        return currentFile;
    }

    /**
     * updates which file is the current one
     * @param newFile
     */
    public void updateFile(File newFile) {
        currentFile = newFile;
    }

    /**
     *
     * @return the name of the File/Tab
     */
    @Override
    public String toString() {
        return getName();
    }
}
