package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

public class CommandBoxTest {
    @BeforeAll
    public static void startJavaFx() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        Platform.startup(() -> {
            Platform.setImplicitExit(false);
            started.countDown();
        });
        assertTrue(started.await(10, TimeUnit.SECONDS), "JavaFX startup timed out");
    }

    @Test
    public void submit_emptyInputIgnored_successExecutesExactInputAndClearsField() throws Exception {
        runOnFxThread(() -> {
            List<String> executed = new ArrayList<>();
            TextField field = createField(command -> {
                executed.add(command);
                return new CommandResult("Success");
            });
            submit(field);
            assertTrue(executed.isEmpty());
            field.setText("  listc  ");
            submit(field);
            assertEquals(List.of("  listc  "), executed);
            assertEquals("", field.getText());
            assertFalse(field.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS));
            press(field, KeyCode.UP);
            assertEquals("  listc  ", field.getText());
            assertEquals(1, executed.size());
        });
    }

    @Test
    public void submit_failuresRetainInputAndHistory_editClearsErrorStyle() throws Exception {
        runOnFxThread(() -> {
            TextField field = createField(command -> {
                if (command.equals("parse")) {
                    throw new ParseException("Invalid syntax");
                }
                throw new CommandException("Cannot execute");
            });
            for (String command : List.of("parse", "execute")) {
                field.setText(command);
                assertFalse(field.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS));
                submit(field);
                submit(field);
                assertEquals(command, field.getText());
                assertEquals(1, field.getStyleClass().stream()
                        .filter(CommandBox.ERROR_STYLE_CLASS::equals).count());
                field.setText("draft");
                assertFalse(field.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS));
                press(field, KeyCode.UP);
                assertEquals(command, field.getText());
                press(field, KeyCode.DOWN);
                assertEquals("draft", field.getText());
            }
        });
    }

    @Test
    public void arrows_browseHistoryRestoreDraftAndPositionCaret_withoutExecuting() throws Exception {
        runOnFxThread(() -> {
            List<String> executed = new ArrayList<>();
            TextField field = createField(command -> {
                executed.add(command);
                return new CommandResult("Success");
            });
            field.setText("draft");
            press(field, KeyCode.UP);
            press(field, KeyCode.DOWN);
            assertEquals("draft", field.getText());
            for (String command : List.of("listc", "listj")) {
                field.setText(command);
                submit(field);
            }
            field.setText("unfinished");
            press(field, KeyCode.UP);
            assertEquals("listj", field.getText());
            assertEquals(field.getLength(), field.getCaretPosition());
            press(field, KeyCode.UP);
            press(field, KeyCode.UP);
            assertEquals("listc", field.getText());
            press(field, KeyCode.DOWN);
            assertEquals("listj", field.getText());
            press(field, KeyCode.DOWN);
            press(field, KeyCode.DOWN);
            assertEquals("unfinished", field.getText());
            assertEquals(List.of("listc", "listj"), executed);
        });
    }

    @Test
    public void otherKeysAndModifiedArrows_doNotBrowseHistory() throws Exception {
        runOnFxThread(() -> {
            TextField field = createField(command -> new CommandResult("Success"));
            field.setText("help");
            submit(field);
            field.setText("draft");
            press(field, KeyCode.F2);
            assertEquals("draft", field.getText());
            for (KeyCode code : List.of(KeyCode.UP, KeyCode.DOWN)) {
                for (int modifier = 0; modifier < 4; modifier++) {
                    field.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code,
                            modifier == 0, modifier == 1, modifier == 2, modifier == 3));
                    assertEquals("draft", field.getText());
                }
            }
            press(field, KeyCode.UP);
            assertEquals("help", field.getText());
        });
    }

    private static TextField createField(CommandBox.CommandExecutor executor) {
        CommandBox box = new CommandBox(executor);
        return (TextField) box.getRoot().lookup("#commandTextField");
    }

    private static void submit(TextField field) {
        field.fireEvent(new ActionEvent());
    }

    private static void press(TextField field, KeyCode code) {
        field.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false));
    }

    private static void runOnFxThread(Runnable action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(action, null);
        Platform.runLater(task);
        task.get(10, TimeUnit.SECONDS);
    }
}
