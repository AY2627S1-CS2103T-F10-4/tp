package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.InputLimits.MAX_COMMAND_LENGTH;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.JavaFxTestUtil;

/**
 * Exercises command entry, completion and history on the JavaFX application thread.
 */
public class CommandBoxTest {
    @BeforeAll
    public static void startJavaFx() throws Exception {
        JavaFxTestUtil.startJavaFx();
    }

    @Test
    public void suggestions_inMainWindow_overlayOutputWithoutShiftingLayout() throws Exception {
        runOnFxThread(() -> {
            Stage stage = new Stage();
            try {
                StorageManager storage = new StorageManager(
                        new JsonAddressBookStorage(Path.of("unused-addressbook.json")),
                        new JsonUserPrefsStorage(Path.of("unused-preferences.json")));
                MainWindow window = new MainWindow(stage, new LogicManager(new ModelManager(), storage),
                        Path.of("unused-addressbook.json"));
                window.fillInnerParts();
                stage.show();
                Scene scene = stage.getScene();
                scene.getRoot().applyCss();
                scene.getRoot().layout();
                TextField field = (TextField) scene.lookup("#commandTextField");
                Node result = scene.lookup("#resultDisplayPlaceholder");
                double resultY = result.localToScene(result.getBoundsInLocal()).getMinY();
                field.setText("lis");
                scene.getRoot().applyCss();
                scene.getRoot().layout();
                ListView<?> suggestions = (ListView<?>) scene.lookup("#suggestionList");
                assertTrue(suggestions.isVisible());
                assertEquals(74, suggestions.getHeight(), 1.0,
                        "Allow pixel snapping at fractional display scales");
                assertTrue(suggestions.getWidth() > 0);
                assertTrue(suggestions.localToScene(suggestions.getLayoutBounds()).getMinY()
                        >= field.localToScene(field.getLayoutBounds()).getMaxY());
                assertTrue(result.localToScene(result.getBoundsInLocal()).getMinY()
                        < suggestions.localToScene(suggestions.getBoundsInLocal()).getMaxY(),
                        "Suggestions must overlay the result panel instead of reserving space");
                assertFalse(scene.lookup("#suggestionOverlay").isManaged());
                assertTrue(scene.lookup("#commandBoxPlaceholder").getViewOrder() < result.getViewOrder());
                assertEquals(resultY, result.localToScene(result.getBoundsInLocal()).getMinY());
                for (String input : List.of("listh", "ex", "xyz", "", "listc")) {
                    field.setText(input);
                    scene.getRoot().applyCss();
                    scene.getRoot().layout();
                    assertEquals(resultY, result.localToScene(result.getBoundsInLocal()).getMinY(),
                            "Suggestion count must not move the result panel");
                }
                field.setText("lis");
                press(field, KeyCode.ESCAPE);
                scene.getRoot().layout();
                assertFalse(suggestions.isVisible());
                assertFalse(scene.lookup("#suggestionOverlay").isVisible());
                assertEquals(resultY, result.localToScene(result.getBoundsInLocal()).getMinY());
            } finally {
                stage.close();
            }
        });
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
    public void inputLengthFilter_rejectsCharactersBeyondMaximum() throws Exception {
        runOnFxThread(() -> {
            TextField field = createField(command -> new CommandResult("Success"));
            field.replaceText(0, 0, "a".repeat(MAX_COMMAND_LENGTH + 1));
            assertEquals(MAX_COMMAND_LENGTH, field.getText().length());
            Label hint = (Label) field.getParent().lookup("#commandHint");
            assertTrue(hint.getText().contains("Input truncated"));
            assertTrue(hint.getStyleClass().contains(CommandBox.WARNING_STYLE_CLASS));
            field.appendText("b");
            assertEquals(MAX_COMMAND_LENGTH, field.getText().length());
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

    @Test
    public void suggestions_arrowsAndTab_completeWithoutExecuting() throws Exception {
        runOnFxThread(() -> {
            List<String> executed = new ArrayList<>();
            CommandBox box = new CommandBox(command -> {
                executed.add(command);
                return new CommandResult("Success");
            });
            TextField field = (TextField) box.getRoot().lookup("#commandTextField");
            ListView<?> suggestions = (ListView<?>) box.getRoot().lookup("#suggestionList");
            field.setText("lis");
            assertTrue(suggestions.isVisible());
            assertTrue(suggestions.isManaged());
            assertEquals(List.of("list", "listc", "listhc", "listhj", "listj"), suggestions.getItems());
            press(field, KeyCode.UP);
            assertEquals("list", suggestions.getSelectionModel().getSelectedItem());
            press(field, KeyCode.DOWN);
            press(field, KeyCode.DOWN);
            press(field, KeyCode.DOWN);
            assertEquals("listhj", suggestions.getSelectionModel().getSelectedItem());
            press(field, KeyCode.UP);
            assertEquals("listhc", suggestions.getSelectionModel().getSelectedItem());
            press(field, KeyCode.DOWN);
            press(field, KeyCode.F2);
            assertTrue(suggestions.isVisible());
            press(field, KeyCode.TAB);
            assertEquals("listhj", field.getText());
            assertEquals(field.getLength(), field.getCaretPosition());
            assertFalse(suggestions.isVisible());
            assertFalse(suggestions.isManaged());
            assertTrue(executed.isEmpty());
            submit(field);
            assertEquals(List.of("listhj"), executed);
        });
    }

    @Test
    public void suggestions_enterOnlyCompletes_preservesLeadingSpaces() throws Exception {
        runOnFxThread(() -> {
            List<String> executed = new ArrayList<>();
            TextField field = createField(command -> {
                executed.add(command);
                return new CommandResult("Success");
            });
            field.setText("  lis");
            press(field, KeyCode.ENTER);
            assertEquals("  list", field.getText());
            assertTrue(executed.isEmpty());
            submit(field);
            assertEquals(List.of("  list"), executed);
            field.setText("hel");
            submit(field);
            assertEquals("help", field.getText());
            assertEquals(1, executed.size());
        });
    }

    @Test
    public void suggestions_escapeAndHistoryRestoreDraft_withoutReopeningSuggestions() throws Exception {
        runOnFxThread(() -> {
            CommandBox box = new CommandBox(command -> new CommandResult("Success"));
            TextField field = (TextField) box.getRoot().lookup("#commandTextField");
            ListView<?> suggestions = (ListView<?>) box.getRoot().lookup("#suggestionList");
            field.setText("help");
            submit(field);
            field.setText("lis");
            field.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN,
                    false, true, false, false));
            assertEquals("list", suggestions.getSelectionModel().getSelectedItem());
            press(field, KeyCode.ESCAPE);
            assertFalse(suggestions.isVisible());
            assertEquals("lis", field.getText());
            press(field, KeyCode.UP);
            assertEquals("help", field.getText());
            press(field, KeyCode.DOWN);
            assertEquals("lis", field.getText());
            assertFalse(suggestions.isVisible());
            field.setText("li");
            assertTrue(suggestions.isVisible());
            field.setText("listc ");
            assertFalse(suggestions.isVisible());
        });
    }

    @Test
    public void suggestions_focusLeavesField_dismissesSuggestions() throws Exception {
        Stage[] window = new Stage[1];
        CountDownLatch focused = new CountDownLatch(1);
        try {
            runOnFxThread(() -> {
                CommandBox box = new CommandBox(command -> new CommandResult("Success"));
                Stage stage = new Stage();
                window[0] = stage;
                stage.focusedProperty().addListener((observable, oldValue, hasFocus) -> {
                    if (hasFocus) {
                        focused.countDown();
                    }
                });
                TextField otherField = new TextField();
                otherField.setId("otherField");
                stage.setScene(new Scene(new VBox(box.getRoot(), otherField)));
                stage.show();
                stage.requestFocus();
            });
            assertTrue(focused.await(10, TimeUnit.SECONDS), "Test window did not receive focus");
            runOnFxThread(() -> {
                Scene scene = window[0].getScene();
                TextField field = (TextField) scene.lookup("#commandTextField");
                ListView<?> suggestions = (ListView<?>) scene.lookup("#suggestionList");
                field.requestFocus();
                assertTrue(field.isFocused());
                field.setText("lis");
                assertTrue(suggestions.isVisible());
                scene.lookup("#otherField").requestFocus();
                assertFalse(suggestions.isVisible());
            });
        } finally {
            runOnFxThread(() -> {
                if (window[0] != null) {
                    window[0].close();
                }
            });
        }
    }

    @Test
    public void suggestions_largeCatalogue_capsHeightAndScrollsToSelection() throws Exception {
        runOnFxThread(() -> {
            List<String> executed = new ArrayList<>();
            CommandBox box = new CommandBox(command -> {
                executed.add(command);
                return new CommandResult("Success");
            }, new CommandSuggestions(List.of("lista", "listb", "listc", "listd", "liste", "listf", "listg")));
            Scene scene = new Scene(new VBox(box.getRoot()), 600, 300);
            scene.getRoot().applyCss();
            scene.getRoot().layout();
            TextField field = (TextField) scene.lookup("#commandTextField");
            ListView<?> suggestions = (ListView<?>) scene.lookup("#suggestionList");
            field.setText("list");
            scene.getRoot().applyCss();
            scene.getRoot().layout();
            assertEquals(74, suggestions.getHeight(), 1.0,
                    "Allow pixel snapping at fractional display scales");
            for (int i = 0; i < 8; i++) {
                press(field, KeyCode.DOWN);
            }
            scene.getRoot().layout();
            assertEquals("listg", suggestions.getSelectionModel().getSelectedItem());
            assertSelectedRowInViewport(suggestions);
            for (int i = 0; i < 8; i++) {
                press(field, KeyCode.UP);
            }
            scene.getRoot().layout();
            assertEquals("lista", suggestions.getSelectionModel().getSelectedItem());
            assertSelectedRowInViewport(suggestions);
            press(field, KeyCode.TAB);
            assertEquals("lista", field.getText());
            assertTrue(executed.isEmpty());
        });
    }

    @Test
    public void suggestions_editPrefix_resetsSelectionAndHidesEmptyMatches() throws Exception {
        runOnFxThread(() -> {
            CommandBox box = new CommandBox(command -> new CommandResult("Success"));
            TextField field = (TextField) box.getRoot().lookup("#commandTextField");
            ListView<?> suggestions = (ListView<?>) box.getRoot().lookup("#suggestionList");
            field.setText("lis");
            press(field, KeyCode.DOWN);
            field.setText("add");
            assertEquals(List.of("addc", "addj"), suggestions.getItems());
            assertEquals("addc", suggestions.getSelectionModel().getSelectedItem());
            for (String input : List.of("xyz", "", "list", "listc", "addj n/Alice")) {
                field.setText("lis");
                field.setText(input);
                assertFalse(suggestions.isVisible());
                assertFalse(suggestions.isManaged());
            }
        });
    }

    @Test
    public void suggestions_completionDoesNotAddHistory() throws Exception {
        runOnFxThread(() -> {
            TextField field = createField(command -> new CommandResult("Success"));
            field.setText("help");
            submit(field);
            field.setText("lis");
            press(field, KeyCode.DOWN);
            press(field, KeyCode.TAB);
            press(field, KeyCode.UP);
            assertEquals("help", field.getText());
            press(field, KeyCode.DOWN);
            assertEquals("listc", field.getText());
        });
    }

    @Test
    public void suggestions_modifiedKeys_doNotSelectCompleteOrDismiss() throws Exception {
        runOnFxThread(() -> {
            List<String> executed = new ArrayList<>();
            CommandBox box = new CommandBox(command -> {
                executed.add(command);
                return new CommandResult("Success");
            });
            TextField field = (TextField) box.getRoot().lookup("#commandTextField");
            ListView<?> suggestions = (ListView<?>) box.getRoot().lookup("#suggestionList");
            field.setText("lis");
            for (KeyCode code : List.of(KeyCode.UP, KeyCode.DOWN, KeyCode.TAB, KeyCode.ESCAPE)) {
                for (int modifier = 0; modifier < 4; modifier++) {
                    field.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code,
                            modifier == 0, modifier == 1, modifier == 2, modifier == 3));
                    assertEquals("lis", field.getText());
                    assertEquals("list", suggestions.getSelectionModel().getSelectedItem());
                    assertTrue(suggestions.isVisible());
                }
            }
            assertTrue(executed.isEmpty());
        });
    }

    /**
     * Creates an isolated command field with the supplied executor.
     */
    private static TextField createField(CommandBox.CommandExecutor executor) {
        CommandBox box = new CommandBox(executor);
        return (TextField) box.getRoot().lookup("#commandTextField");
    }

    /**
     * Checks the selected row is entirely inside the scrollable viewport, not merely instantiated.
     */
    private static void assertSelectedRowInViewport(ListView<?> list) {
        ListCell<?> selected = list.lookupAll(".list-cell").stream()
                .map(node -> (ListCell<?>) node)
                .filter(cell -> cell.isSelected() && !cell.isEmpty())
                .findFirst().orElseThrow();
        Bounds row = selected.localToScene(selected.getBoundsInLocal());
        Node viewport = list.lookup(".clipped-container");
        Bounds bounds = viewport.localToScene(viewport.getBoundsInLocal());
        assertTrue(row.getMinY() >= bounds.getMinY() - 1);
        assertTrue(row.getMaxY() <= bounds.getMaxY() + 1);
    }

    /**
     * Fires the text field action used to submit or complete its input.
     */
    private static void submit(TextField field) {
        field.fireEvent(new ActionEvent());
    }

    /**
     * Sends an unmodified key press through the command field event filters.
     */
    private static void press(TextField field, KeyCode code) {
        field.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false));
    }

    /**
     * Runs an assertion block on the JavaFX thread and propagates failures to the test thread.
     */
    private static void runOnFxThread(Runnable action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(action, null);
        Platform.runLater(task);
        task.get(10, TimeUnit.SECONDS);
    }
}
