package ax.ha.it.starter;

import ax.ha.it.starter.utils.FileUtility;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppController {

    //FX Views
    @FXML
    private TextArea resultTextArea;
    @FXML
    private TabPane codeAreaLayout;

    //File Menu Items
    @FXML
    private MenuItem openFileMenuItem;

    //Controllers
    @FXML
    static AppController mainController;


    private ExecutorService executorService;
    private static final int THREADS_AVAILABLE = Runtime.getRuntime().availableProcessors();


    public void initialize() {
        onMenuItemsActions();
        executorService = Executors.newFixedThreadPool(THREADS_AVAILABLE);
        codeAreaLayout.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    private void onMenuItemsActions() {
        openFileMenuItem.setOnAction(event -> openFileAction());
    }

    //TODO: Check for only Java extensions
    private void openFileAction() {
        File javaFile = FileUtility.openFileInExplorer("Find and select Java file");
        if (javaFile != null) {
            executorService.execute(() -> openFile(javaFile));
        }
    }

    //TODO: Update fileList view
    private void openFile(File sourceFile) {
        Tab javaTab = new Tab(sourceFile.getName());
        javaTab.setUserData(sourceFile.getPath());

        CodeArea codeTextArea = new CodeArea();
        Editor editorController = new Editor(codeTextArea, resultTextArea);

        try {
            StringBuilder code = new StringBuilder();
            Files.readAllLines(Path.of(sourceFile.getPath()), Charset.defaultCharset()).forEach(s -> code.append(s).append("\n"));
            codeTextArea.replaceText(0, 0, code.toString());
            javaTab.setContent(new VirtualizedScrollPane<>(codeTextArea));
            Platform.runLater(() -> {
                codeAreaLayout.getTabs().add(javaTab);
            });
            editorController.codeAreaHighlighter(); // Does currently not work, might be cause it doesn't find the proper css file
            editorController.updateSourceFile(sourceFile);
        } catch (IOException e) {
            System.out.println("Can't open file");
        }
    }
}
