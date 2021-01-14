package ax.ha.it.starter;

import ax.ha.it.starter.utilities.DialogUtility;
import ax.ha.it.starter.utilities.FileUtility;
import ax.ha.it.starter.utilities.TreeViewUtility;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
    private MenuItem aboutMenuItem;
    @FXML
    private MenuItem newFileMenuItem;
    @FXML
    private MenuItem newFolderMenuItem;

    private ExecutorService executorService;
    @FXML
    private TreeView<String> fileTreeView;
    @FXML
    private TreeItem<String> treeItem;

    private TreeViewUtility treeViewList;


    @FXML
    private void kill() {
        System.exit(0);
    }

    public void initialize() {
        onMenuItemsActions();
        treeViewList = new TreeViewUtility(fileTreeView, treeItem);
        executorService = Executors.newFixedThreadPool(THREADS_AVAILABLE);
        codeAreaLayout.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    private void onMenuItemsActions() {
        exitMenuItem.setOnAction(event -> kill());
        openFileMenuItem.setOnAction(event -> openFileAction());
        saveMenuItem.setOnAction(event -> saveFileAction());
        aboutMenuItem.setOnAction(actionEvent -> DialogUtility.openAlertDialog("Created by Anton Wärnström and Andreas Tallberg"));
        newFileMenuItem.setOnAction(event -> createNewFile(DialogUtility.inputDialog("Enter filename")));

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
            Platform.runLater(() -> {
                treeViewList.addTreeItem(sourceFile.getName());
                codeAreaLayout.getTabs().add(newTab);
            });
        } catch (IOException e) {
            System.out.println("Can't open file");
        }
    }

    private void createNewFile(String fileName) {
        if (!fileName.isEmpty()) {
            Tab newTab = new Tab(fileName);
            CodeArea codeTextArea = new CodeArea();
            Editor editorController = new Editor(codeTextArea, resultTextArea);
            ScrollPane scrollArea = new ScrollPane(codeTextArea);

            newTab.setUserData(fileName);
            editorController.codeAreaHighlighter();
            newTab.setContent(scrollArea);
            scrollArea.fitToWidthProperty().set(true);
            scrollArea.fitToHeightProperty().set(true);
            Platform.runLater(() -> {
                treeViewList.addTreeItem(fileName);
                codeAreaLayout.getTabs().add(newTab);
            });
        } else {
            DialogUtility.openAlertDialog("Please enter a filename");
        }
    }

    private void saveSourceCode(File sourceFile) throws IOException {
        String saveLocation = sourceFile.getPath();
        StringBuilder code = new StringBuilder();
        List<String> codeLines = Files.readAllLines(Path.of(sourceFile.getPath()), Charset.defaultCharset());
        code.append("Hello World");
        codeLines.forEach(System.out::println);

        try (PrintWriter out = new PrintWriter(sourceFile)) {
            out.println(code);
        }
    }
}
