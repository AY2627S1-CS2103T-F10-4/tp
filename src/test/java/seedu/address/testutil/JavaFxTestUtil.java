package seedu.address.testutil;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

/**
 * Starts the JavaFX toolkit for UI tests.
 * Gradle runs every test class in one JVM, but JavaFX can be started only once per JVM,
 * so every UI test class must start it through here to be safe in any run order.
 */
public class JavaFxTestUtil {
    /**
     * Starts the JavaFX toolkit and keeps it alive between window-based tests,
     * tolerating a toolkit already started by another UI test class in this JVM.
     */
    public static void startJavaFx() throws InterruptedException {
        CountDownLatch started = new CountDownLatch(1);
        try {
            Platform.startup(() -> {
                Platform.setImplicitExit(false);
                started.countDown();
            });
        } catch (IllegalStateException alreadyStarted) {
            started.countDown();
        }
        assertTrue(started.await(10, TimeUnit.SECONDS), "JavaFX startup timed out");
    }
}
