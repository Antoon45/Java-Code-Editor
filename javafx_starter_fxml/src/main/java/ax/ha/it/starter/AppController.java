package ax.ha.it.starter;

import ax.ha.it.starter.utils.FileUtility;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppController {

    private static final int THREADS_AVAILABLE = Runtime.getRuntime().availableProcessors();
    //Controllers
    @FXML
    static AppController mainController;
    //FX Views
    @FXML
    private TextArea resultTextArea;
    @FXML
    private TabPane codeAreaLayout;
    //File Menu Items
    @FXML
    private MenuItem openFileMenuItem;
    private ExecutorService executorService;

    public void initialize() {
        onMenuItemsActions();
        executorService = Executors.newFixedThreadPool(THREADS_AVAILABLE);
        codeAreaLayout.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    private void onMenuItemsActions() {
        openFileMenuItem.setOnAction(event -> openFileAction());
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
            editorController.codeAreaHighlighter(); // Does currently not work, might be cause it doesn't find the proper css file
            Platform.runLater(() -> {
                codeAreaLayout.getTabs().add(newTab);
            });
        } catch (IOException e) {
            System.out.println("Can't open file");
        }
    }
}
