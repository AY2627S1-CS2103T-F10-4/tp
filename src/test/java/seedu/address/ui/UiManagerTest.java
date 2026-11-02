package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.ModelManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.JavaFxTestUtil;

/**
 * Tests that {@code UiManager} shows the startup message it is given once the window has opened.
 */
public class UiManagerTest {

    @TempDir
    public Path testFolder;

    @BeforeAll
    public static void startJavaFx() throws Exception {
        JavaFxTestUtil.startJavaFx();
    }

    /**
     * Guards the call that passes the message on to the window. {@code MainApp} sets the message before
     * the window exists, so without this call a first-time user would never see it.
     */
    @Test
    public void start_startupMessage_shownInResultDisplay() throws Exception {
        JavaFxTestUtil.runOnFxThread(() -> {
            // EP: non-empty message
            assertEquals("First line\nSecond line", startAndReadResultDisplay("First line\nSecond line"));
            assertEquals("a", startAndReadResultDisplay("a")); // Boundary value

            // EP: empty message, which is what every launch after the first one passes
            assertEquals("", startAndReadResultDisplay("")); // Boundary value
        });
    }

    /**
     * Starts a {@code UiManager} with {@code startupMessage}, and returns the text then shown in the
     * result display of its window.
     */
    private String startAndReadResultDisplay(String startupMessage) {
        Path dataFile = testFolder.resolve("addressbook.json");
        StorageManager storage = new StorageManager(new JsonAddressBookStorage(dataFile),
                new JsonUserPrefsStorage(testFolder.resolve("prefs.json")));
        Logic logic = new LogicManager(new ModelManager(), storage);
        Stage stage = new Stage();
        try {
            new UiManager(logic, dataFile, startupMessage).start(stage);
            return ((TextArea) stage.getScene().lookup("#resultDisplay")).getText();
        } finally {
            stage.close();
        }
    }
}
