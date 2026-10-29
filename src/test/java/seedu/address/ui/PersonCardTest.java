package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import seedu.address.model.person.Person;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

public class PersonCardTest {
    @BeforeAll
    public static void startJavaFx() {
        try {
            Platform.startup(() -> Platform.setImplicitExit(false));
        } catch (IllegalStateException e) {
            // Another UI test has already started the shared toolkit.
        }
    }

    @Test
    public void card_populatedOptionalFields_displaysOnlyRelevantField() throws Exception {
        runOnFxThread(() -> {
            PersonCard jobseeker = new PersonCard(new JobseekerBuilder()
                    .withHighestQualification("Diploma in IT").build(), 1);
            assertLabel(jobseeker, "highestQualification", "Highest qualification: Diploma in IT", true);
            assertLabel(jobseeker, "availableJobs", "Available jobs: ", false);
            PersonCard client = new PersonCard(new ClientBuilder()
                    .withAvailableJobs("QA Tester", "Engineer").build(), 1);
            assertLabel(client, "availableJobs", "Available jobs: Engineer, QA Tester", true);
            assertLabel(client, "highestQualification", "Highest qualification: ", false);
        });
    }

    @Test
    public void card_emptyOptionalFields_hidesRowsAndReleasesSpace() throws Exception {
        runOnFxThread(() -> {
            for (Person person : List.of(new JobseekerBuilder().build(), new ClientBuilder().build())) {
                PersonCard card = new PersonCard(person, 1);
                assertLabel(card, "highestQualification", "Highest qualification: ", false);
                assertLabel(card, "availableJobs", "Available jobs: ", false);
            }
        });
    }

    private static void assertLabel(PersonCard card, String id, String expected, boolean visibility) {
        Label label = (Label) card.getRoot().lookup("#" + id);
        assertEquals(expected, label.getText());
        assertEquals(visibility, label.isVisible());
        assertEquals(visibility, label.isManaged());
        assertTrue(label.isWrapText());
        assertFalse(label.getText().contains("null"));
    }

    private static void runOnFxThread(Runnable action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(action, null);
        Platform.runLater(task);
        task.get(10, TimeUnit.SECONDS);
    }
}
