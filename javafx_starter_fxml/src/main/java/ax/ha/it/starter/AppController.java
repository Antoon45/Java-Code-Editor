package ax.ha.it.starter;

import ax.ha.it.starter.utilities.FileUtility;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppController {

    private static final int THREADS_AVAILABLE = Runtime.getRuntime().availableProcessors();


    //FX Views
    @FXML
    private TextArea resultTextArea;
    @FXML
    private TabPane codeAreaLayout;

    //File Menu Items
    @FXML
    private MenuItem openFileMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem newFileMenuItem;
    @FXML
    private MenuItem newFolderMenuItem;


    @FXML
    private ListView openedFilesList;

    private ExecutorService executorService;

    @FXML
    private void kill() {
        System.exit(0);
    }

    public void initialize() {
        onMenuItemsActions();
        executorService = Executors.newFixedThreadPool(THREADS_AVAILABLE);
        codeAreaLayout.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    private void onMenuItemsActions() {
        exitMenuItem.setOnAction(event -> kill());
        openFileMenuItem.setOnAction(event -> openFileAction());
        saveMenuItem.setOnAction(event -> saveFileAction());
        newFileMenuItem.setOnAction(event -> createNewFile());
    }

    /**
     * @description Opens fileexplorer and calls openNewTabWithFile() to send the returned file
     * <p>
     * TODO: Check for only Java extensions
     */
    private void openFileAction() {
        File javaFile = FileUtility.openFileInExplorer("Find and select Java file");
        if (javaFile != null) {
            executorService.execute(() -> openNewTabWithFile(javaFile));
        }
    }

    private void saveFileAction() {
        File javaFile = FileUtility.openSaveFileExplorer("Select and save Java file");
        if (javaFile != null) {
            executorService.execute(() -> {
                try {
                    saveSourceCode(javaFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * @param sourceFile
     * @description creates a new tab based on a file
     */
    //TODO: Update fileList view
    private void openNewTabWithFile(File sourceFile) {
        Tab newTab = new Tab(sourceFile.getName());
        newTab.setUserData(sourceFile.getPath());

        CodeArea codeTextArea = new CodeArea();
        Editor editorController = new Editor(codeTextArea, resultTextArea);

        try {
            editorController.codeAreaHighlighter();
            StringBuilder code = new StringBuilder();
            List<String> codeLines = Files.readAllLines(Path.of(sourceFile.getPath()), Charset.defaultCharset());
            for (String s : codeLines) {
                code.append(s).append("\n");
            }
            codeTextArea.replaceText(0, 0, code.toString());
            ScrollPane scrollArea = new ScrollPane(codeTextArea);
            newTab.setContent(scrollArea);
            scrollArea.fitToWidthProperty().set(true);
            scrollArea.fitToHeightProperty().set(true);
            Platform.runLater(() -> codeAreaLayout.getTabs().add(newTab));
        } catch (IOException e) {
            System.out.println("Can't open file");
        }
    }

    private void createNewFile() {
        Tab newTab = new Tab();
        //newTab.setUserData();

        CodeArea codeTextArea = new CodeArea();
        Editor editorController = new Editor(codeTextArea, resultTextArea);
        editorController.codeAreaHighlighter();
        ScrollPane scrollArea = new ScrollPane(codeTextArea);
        newTab.setContent(scrollArea);
        scrollArea.fitToWidthProperty().set(true);
        scrollArea.fitToHeightProperty().set(true);
        Platform.runLater(() -> codeAreaLayout.getTabs().add(newTab));
    }

    private void saveSourceCode(File sourceFile) throws IOException {
        String saveLocation = sourceFile.getPath();
        StringBuilder code = new StringBuilder();

        List<String> codeLines = Files.readAllLines(Path.of(sourceFile.getPath()), Charset.defaultCharset());
        for (String s : codeLines) {
            code.append(s).append("\n");
        }

        try {
            FileWriter file = new FileWriter(saveLocation);
            file.write(String.valueOf(code));
            file.close();
            File testFile = new File(String.valueOf(file));
            System.out.println(testFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
