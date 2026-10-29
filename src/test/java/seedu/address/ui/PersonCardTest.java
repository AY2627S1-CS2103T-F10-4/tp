package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.Node;
import seedu.address.model.person.Person;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JavaFxTestUtil;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Checks that {@code PersonCard} shows the "Hidden" badge only for hidden persons.
 */
public class PersonCardTest {
    @BeforeAll
    public static void startJavaFx() throws Exception {
        JavaFxTestUtil.startJavaFx();
    }

    @Test
    public void constructor_hiddenJobseeker_indicatorVisibleAndManaged() throws Exception {
        Node indicator = createIndicator(new JobseekerBuilder().withHidden(true).build());
        assertTrue(indicator.isVisible());
        assertTrue(indicator.isManaged());
    }

    /**
     * Checks the badge is unmanaged as well as invisible, because an invisible but managed
     * node still reserves its width and would leave a gap after the name.
     */
    @Test
    public void constructor_unhiddenJobseeker_indicatorInvisibleAndUnmanaged() throws Exception {
        Node indicator = createIndicator(new JobseekerBuilder().withHidden(false).build());
        assertFalse(indicator.isVisible());
        assertFalse(indicator.isManaged());
    }

    @Test
    public void constructor_hiddenClient_indicatorVisibleAndManaged() throws Exception {
        Node indicator = createIndicator(new ClientBuilder().withHidden(true).build());
        assertTrue(indicator.isVisible());
        assertTrue(indicator.isManaged());
    }

    /**
     * Builds a card for {@code person} on the JavaFX thread and returns its hidden badge.
     */
    private static Node createIndicator(Person person) throws Exception {
        FutureTask<Node> task = new FutureTask<>(() ->
                new PersonCard(person, 1).getRoot().lookup("#hiddenIndicator"));
        Platform.runLater(task);
        return task.get(10, TimeUnit.SECONDS);
    }
}
