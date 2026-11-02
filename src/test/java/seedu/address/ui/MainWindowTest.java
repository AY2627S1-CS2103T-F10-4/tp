package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.ModelManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.JavaFxTestUtil;

/**
 * Tests what {@code MainWindow} shows in its result display. Commands save to a temporary folder,
 * so these tests never touch the real save file.
 */
public class MainWindowTest {

    @TempDir
    public Path testFolder;

    @BeforeAll
    public static void startJavaFx() throws Exception {
        JavaFxTestUtil.startJavaFx();
    }

    @Test
    public void showMessage_validMessage_replacesResultDisplayText() throws Exception {
        JavaFxTestUtil.runOnFxThread(() -> {
            Stage stage = new Stage();
            try {
                MainWindow window = createWindow(stage);

                // EP: one line
                assertMessageShown(window, stage, "Some message");
                assertMessageShown(window, stage, "a"); // Boundary value

                // EP: several lines, like the sample data message
                assertMessageShown(window, stage, "First line\nSecond line");

                // EP: empty message, which must clear the earlier message
                assertMessageShown(window, stage, ""); // Boundary value
            } finally {
                stage.close();
            }
        });
    }

    @Test
    public void showMessage_nullMessage_throwsNullPointerException() throws Exception {
        JavaFxTestUtil.runOnFxThread(() -> {
            Stage stage = new Stage();
            try {
                MainWindow window = createWindow(stage);

                // EP: null
                assertThrows(NullPointerException.class, () -> window.showMessage(null));
            } finally {
                stage.close();
            }
        });
    }

    /**
     * The startup message is only a hint, so the result of the user's next command must replace it
     * rather than being added to it or hidden by it.
     */
    @Test
    public void showMessage_thenCommandEntered_replacedByCommandResult() throws Exception {
        JavaFxTestUtil.runOnFxThread(() -> {
            Stage stage = new Stage();
            try {
                MainWindow window = createWindow(stage);
                window.showMessage("Some message");

                Scene scene = stage.getScene();
                TextField commandField = (TextField) scene.lookup("#commandTextField");
                commandField.setText("list");
                commandField.fireEvent(new ActionEvent());

                assertNotEquals("", getResultDisplayText(stage));
                assertNotEquals("Some message", getResultDisplayText(stage));
            } finally {
                stage.close();
            }
        });
    }

    private MainWindow createWindow(Stage stage) {
        Path dataFile = testFolder.resolve("addressbook.json");
        StorageManager storage = new StorageManager(new JsonAddressBookStorage(dataFile),
                new JsonUserPrefsStorage(testFolder.resolve("prefs.json")));
        Logic logic = new LogicManager(new ModelManager(), storage);
        MainWindow window = new MainWindow(stage, logic, dataFile);
        window.fillInnerParts();
        return window;
    }

    private void assertMessageShown(MainWindow window, Stage stage, String message) {
        window.showMessage(message);
        assertEquals(message, getResultDisplayText(stage));
    }

    private String getResultDisplayText(Stage stage) {
        return ((TextArea) stage.getScene().lookup("#resultDisplay")).getText();
    }
}
