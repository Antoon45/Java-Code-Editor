package ax.ha.it.starter.utilities;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtility {


    @Nullable
    public static File createNewFile(String filePath) {
        try {
            File newFolder = new File(filePath);
            newFolder.createNewFile();
            return newFolder;
        } catch (IOException e) {
            String errorMessage = "Can't Create new file in this path";
            System.out.print(errorMessage);
            return null;
        }
    }

    @Nullable
    public static File createNewFileWithoutPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a location to save the new file in");
        try {
            File newFolder = new File(fileChooser.showSaveDialog(null).getAbsolutePath());
            newFolder.createNewFile();
            return (newFolder);
        } catch (IOException e) {
            String errorMessage = "Can't Create new file in this path";
            System.out.print(errorMessage);
            return null;
        }
    }


    @Nullable
    public static File createNewFolder(String filePath) {
        File newFolder = new File(filePath);
        newFolder.mkdir();
        return newFolder;
    }

    @Nullable
    public static File openSourceFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(null);
    }

    @Nullable
    public static File saveSourceFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showSaveDialog(null);
    }

    public static void updateContent(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            System.out.println("File update completed");
            fileWriter.close();
        } catch (IOException iox) {
            System.out.println("File save failed.");
        }
    }

    public static void deleteFile(File file) {
        file.deleteOnExit();
    }
}
