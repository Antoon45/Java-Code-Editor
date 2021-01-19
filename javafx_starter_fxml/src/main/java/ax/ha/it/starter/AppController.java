package ax.ha.it.starter;

import ax.ha.it.starter.utilities.DialogUtility;
import ax.ha.it.starter.utilities.FileUtility;
import ax.ha.it.starter.utilities.TreeViewUtility;
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

    private FileUtility fileManager;


    @FXML
    private void kill() {
        System.exit(0);
    }

    public void initialize() {
        onMenuItemsActions();
        fileManager = new FileUtility();
        treeViewList = new TreeViewUtility(fileTreeView, treeItem);
        executorService = Executors.newFixedThreadPool(THREADS_AVAILABLE);
        codeAreaLayout.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    private void onMenuItemsActions() {
        exitMenuItem.setOnAction(event -> kill());
        openFileMenuItem.setOnAction(event -> openFileAction());
        saveMenuItem.setOnAction(event -> saveFileAction());
        aboutMenuItem.setOnAction(actionEvent -> DialogUtility.openAlertDialog("")); // TODO: Add names to the string
        newFileMenuItem.setOnAction(event -> createNewFile(DialogUtility.inputDialog("Enter filename")));

    }

    /**
     * @description Opens fileexplorer and calls openNewTabWithFile() to send the returned file
     * <p>
     * TODO: Check for only Java extensions
     */
    private void openFileAction() {
        File javaFile = fileManager.openFileInExplorer("Find and select Java file");
        if (javaFile != null) {
            executorService.execute(() -> openNewTabWithFile(javaFile));
        }
    }

    private void saveFileAction() {
        File javaFile = fileManager.openSaveFileExplorer("Select and save Java file");
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
    private void openNewTabWithFile(File sourceFile) {
        Tab newTab = new Tab(sourceFile.getName());
        newTab.setUserData(sourceFile.getPath());

        CodeArea codeTextArea = new CodeArea();
        Editor editorController = new Editor(codeTextArea, resultTextArea);
        fileManager.setEditor(editorController);

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
            fileManager.updateFile(sourceFile);
            Platform.runLater(() -> {
                treeViewList.addTreeItemWithValue(sourceFile.getName(), code.toString());
                codeAreaLayout.getTabs().add(newTab);
            });
        } catch (IOException e) {
            DialogUtility.openAlertDialog("Couldn't find a file to open");
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

    private void saveSourceCode(File chosenFile) throws IOException {
        FileWriter fileWriter;
        fileWriter = new FileWriter(chosenFile);
        fileWriter.write(fileManager.getEditor().getCode());
        fileWriter.close();
    }
}
