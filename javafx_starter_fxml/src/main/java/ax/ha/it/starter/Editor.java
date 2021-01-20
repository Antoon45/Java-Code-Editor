package ax.ha.it.starter;

import ax.ha.it.starter.constants.Keywords;
import ax.ha.it.starter.utilities.FileUtility;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.*;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;

public class Editor {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private final ExecutorService taskExecutor;
    private final EventHandler<KeyEvent> keyTyped;
    private final EventHandler<KeyEvent> keyPress;
    private CodeArea codeTextArea;
    private File sourceFile;
    private static TextArea resultTextArea; // Place holder for terminal
    {
        keyTyped = (KeyEvent event) -> {
            if (event.isConsumed()) {
                int position = codeTextArea.getCaretPosition();
                String character = event.getCharacter();
                Platform.runLater(() -> codeTextArea.replaceText(position - 1, position, character));
                System.out.print(codeTextArea.getText());
            }
        };

        keyPress = (KeyEvent event) -> {
            if (event.isConsumed()) {
                KeyCode key = event.getCode();
                int position = codeTextArea.getCaretPosition();
                if (key == KeyCode.ENTER) {
                    Platform.runLater(() -> codeTextArea.replaceText(position - 1, position, Keywords.ENTER));
                } else if (key == KeyCode.BACK_SPACE) {
                    Platform.runLater(() -> codeTextArea.replaceText(position, position, Keywords.BACK_SPACE));
                }
                System.out.print(codeTextArea.getText());
            }
        };
    }

    public static void openWindowsTerminal() {
        if (OS.contains("win")) {
            try {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("cmd.exe /c start", null);
            } catch (IOException e) {
                System.out.println("Can't start windows terminal");
            }
        }
    }

    protected Editor(CodeArea codeArea, TextArea resultArea) {
        taskExecutor = Executors.newSingleThreadExecutor();
        codeTextArea = codeArea;
        resultTextArea = resultArea;
        codeTextArea.setOnKeyTyped(keyTyped);
        codeTextArea.setOnKeyPressed(keyPress);
        getEditorMenu();
        resultArea.setEditable(false);
    }

    private void getEditorMenu() {
        MenuItem save = new MenuItem("Save");
        MenuItem run = new MenuItem("Run");
        save.setOnAction(this::saveSourceCode);
        run.setOnAction(e -> codeExecute(sourceFile));
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(save);
        contextMenu.getItems().add(run);
        codeTextArea.setContextMenu(contextMenu);

    }

    void updateSourceFile(File source) {
        sourceFile = source;
    }


    private void saveSourceCode(ActionEvent event) {
        String sourceCode = codeTextArea.getText();
        if (sourceFile.canWrite()) {
            FileUtility.updateContent(sourceFile, sourceCode);
        }
    }

    private void codeExecute(File file) {
        String compile = "javac -cp src ".concat(file.getAbsolutePath());
        String run = "java -cp src ".concat(file.getAbsolutePath()).split("\\.")[0];
        System.out.println("java ".concat(file.getAbsolutePath()).split("\\.")[0].concat(".class"));
        System.out.println("java ".concat(file.getAbsolutePath()).split("\\.")[0].concat(".class"));

        try {
            runProcess(compile);
            runProcess(run);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printLines(String cmd, InputStream inputStream) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = in.readLine()) != null) {
            resultTextArea.replaceText(0, 0, cmd + " " + line + "\n");
        }
    }

    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        resultTextArea.replaceText(0, 0, command + " exitValue(): " + pro.exitValue() + "\n");
    }
    /*
     * This code is taken from the demo
     * https://github.com/FXMisc/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywordsAsyncDemo.java
     * */

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = Keywords.PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public void codeAreaHighlighter() {
        codeTextArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(codeTextArea.multiPlainChanges())
                .filterMap(tryTask -> {
                    if (tryTask.isSuccess()) {
                        return Optional.of(tryTask.get());
                    } else {
                        tryTask.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
        codeTextArea.setParagraphGraphicFactory(LineNumberFactory.get(codeTextArea));
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeTextArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<>() {
            @Override
            protected StyleSpans<Collection<String>> call() {
                return computeHighlighting(text);
            }
        };
        taskExecutor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeTextArea.setStyleSpans(0, highlighting);
    }
}
