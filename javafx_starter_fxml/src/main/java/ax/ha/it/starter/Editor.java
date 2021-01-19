package ax.ha.it.starter;

import ax.ha.it.starter.constants.Keywords;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;

public class Editor {
    private final ExecutorService taskExecutor;
    private final EventHandler<KeyEvent> keyTyped;
    private final EventHandler<KeyEvent> keyPress;
    private CodeArea codeTextArea;
    private TextArea resultTextArea; // Place holder for terminal

    //Checks if certains keys are pressed and then either:
    {
        keyTyped = (KeyEvent event) -> {
            if (event.isConsumed()) {
                int position = codeTextArea.getCaretPosition();
                String character = event.getCharacter();
                Platform.runLater(() -> codeTextArea.replaceText(position - 1, position, character));
                //prints them out in console
                System.out.print(codeTextArea.getText());
            }
        };

        keyPress = (KeyEvent event) -> {
            if (event.isConsumed()) {
                KeyCode key = event.getCode();
                int position = codeTextArea.getCaretPosition();
                if (key == KeyCode.ENTER) {
                    //adds a new row
                    Platform.runLater(() -> codeTextArea.replaceText(position - 1, position, Keywords.ENTER));
                } else if (key == KeyCode.BACK_SPACE) {
                    //removes a letter
                    Platform.runLater(() -> codeTextArea.replaceText(position, position, Keywords.BACK_SPACE));
                }
                System.out.print(codeTextArea.getText());
            }
        };
    }

    Editor(CodeArea codeArea, TextArea resultArea) {
        taskExecutor = Executors.newSingleThreadExecutor();
        codeTextArea = codeArea;
        resultTextArea = resultArea;
        codeTextArea.setOnKeyTyped(keyTyped);
        codeTextArea.setOnKeyPressed(keyPress);
    }

    /**
     * A getter that gets the text inside the codeArea
     * @return the text inside the codeArea "editor"
     */
    public String getCode() {
        return codeTextArea.getText();
    }

    /*
     * This code is taken from the demo
     * https://github.com/FXMisc/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywordsAsyncDemo.java
     * */


    /**
     * starts a Matcher which checks if the key typed is the same as any of the keywords then add the color matching in css and keywords.java, if no matches are found, no styling is applied
     * @param text
     * @return a new spansBuilder
     */
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

    /**
     * adds color to the keyword in 500ms after matching.
     */
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

    /**
     *  computes highlighting if called for in codeAreaHighlighter()
     * @return Colors the word in the right color.
     */
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

    /**
     * Applies the highlighting if called for in codeAreaHighLighter
     * @param highlighting
     */
    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeTextArea.setStyleSpans(0, highlighting);
    }
}
