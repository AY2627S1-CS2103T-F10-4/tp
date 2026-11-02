package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

/** Tests persistent filter descriptions through the command parser and the UI-facing logic API. */
public class FilterDescriptionTest {
    private final Model model = new ModelManager();
    private final Logic logic = new LogicManager(model, null);
    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void initialDescriptions_showUnhiddenLists() {
        assertDescriptions("Showing all unhidden jobseekers", "Showing all unhidden clients");
    }

    @ParameterizedTest
    @CsvSource(value = {
        "findj alex|Showing unhidden jobseekers matching \"alex\"|clients unchanged",
        "findc alex|jobseekers unchanged|Showing unhidden clients matching \"alex\"",
        "findhj alex|Showing hidden jobseekers matching \"alex\"|clients unchanged",
        "findhc alex|jobseekers unchanged|Showing hidden clients matching \"alex\"",
        "find alex|Showing unhidden jobseekers matching \"alex\"|Showing unhidden clients matching \"alex\"",
        "listj|Showing all unhidden jobseekers|clients unchanged",
        "listc|jobseekers unchanged|Showing all unhidden clients",
        "listhj|Showing all hidden jobseekers|clients unchanged",
        "listhc|jobseekers unchanged|Showing all hidden clients",
        "list|Showing all unhidden jobseekers|Showing all unhidden clients"
    }, delimiter = '|')
    public void execute_updatesOnlyAffectedDescriptions(String command, String jobseekers, String clients)
            throws Exception {
        model.jobseekerFilterDescriptionProperty().set("jobseekers unchanged");
        model.clientFilterDescriptionProperty().set("clients unchanged");
        parser.parseCommand(command).execute(model);
        assertDescriptions(jobseekers, clients);
    }

    @Test
    public void execute_searchThenHelp_descriptionPersistsAndBindingUpdates() throws Exception {
        StringProperty displayedDescription = new SimpleStringProperty();
        displayedDescription.bind(logic.jobseekerFilterDescriptionProperty());
        parser.parseCommand("findj Alex diploma").execute(model);
        assertTrue(model.getFilteredJobseekerList().isEmpty());
        assertEquals("Showing unhidden jobseekers matching \"Alex diploma\"", displayedDescription.get());
        parser.parseCommand("help").execute(model);
        assertEquals("Showing unhidden jobseekers matching \"Alex diploma\"", displayedDescription.get());
        parser.parseCommand("findj Bob").execute(model);
        assertEquals("Showing unhidden jobseekers matching \"Bob\"", displayedDescription.get());
        parser.parseCommand("listj").execute(model);
        assertEquals("Showing all unhidden jobseekers", displayedDescription.get());
    }

    @Test
    public void execute_independentSearchesAndHiddenLists_preserveOtherDescription() throws Exception {
        parser.parseCommand("findj alex").execute(model);
        parser.parseCommand("listhc").execute(model);
        assertDescriptions("Showing unhidden jobseekers matching \"alex\"", "Showing all hidden clients");
        parser.parseCommand("findhc Bob").execute(model);
        parser.parseCommand("listhj").execute(model);
        assertDescriptions("Showing all hidden jobseekers", "Showing hidden clients matching \"Bob\"");
        parser.parseCommand("list").execute(model);
        assertDescriptions("Showing all unhidden jobseekers", "Showing all unhidden clients");
    }

    @ParameterizedTest(name = "hidden={0}, remainingMatch={1}")
    @CsvSource({
        "false, none",
        "false, email",
        "false, tag",
        "false, job",
        "true, none",
        "true, email",
        "true, tag",
        "true, job"
    })
    public void execute_findThenRenameClient_refreshesResultsAndPreservesLabel(
            boolean hidden, String remainingMatch) throws Exception {
        ClientBuilder builder = new ClientBuilder().withName("Roy").withHidden(hidden)
                .withEmail("contact@example.com").withTags();
        if (remainingMatch.equals("email")) {
            builder.withEmail("roy@example.com");
        } else if (remainingMatch.equals("tag")) {
            builder.withTags("roy");
        } else if (remainingMatch.equals("job")) {
            builder.withAvailableJobs("Royal Engineering");
        }
        Client roy = builder.build();
        Client oppositeVisibility = new ClientBuilder(roy).withName("Roy Other")
                .withPhone("91234567").withEmail("other@example.com").withHidden(!hidden).build();
        model.addClient(roy);
        model.addClient(oppositeVisibility);
        StringProperty displayedDescription = new SimpleStringProperty();
        displayedDescription.bind(logic.clientFilterDescriptionProperty());
        parser.parseCommand(hidden ? "findhc roy" : "findc roy").execute(model);
        assertEquals(List.of(roy), model.getFilteredClientList());
        String expectedLabel = "Showing " + (hidden ? "hidden" : "unhidden")
                + " clients matching \"roy\"";
        assertEquals(expectedLabel, displayedDescription.get());

        CommandResult result = parser.parseCommand("editc 1 n/John").execute(model);

        Client john = new ClientBuilder(roy).withName("John").build();
        assertEquals(remainingMatch.equals("none") ? List.of() : List.of(john),
                model.getFilteredClientList());
        assertEquals(List.of(john, oppositeVisibility), model.getAddressBook().getClientList());
        assertTrue(result.getFeedbackToUser().contains("John"));
        assertEquals(expectedLabel, displayedDescription.get());
        assertEquals("Showing all unhidden jobseekers",
                logic.jobseekerFilterDescriptionProperty().get());
        parser.parseCommand(hidden ? "listhc" : "listc").execute(model);
        assertEquals(List.of(john), model.getFilteredClientList());
        assertEquals("Showing all " + (hidden ? "hidden" : "unhidden") + " clients",
                displayedDescription.get());
    }

    @ParameterizedTest(name = "hidden={0}, remainingMatch={1}")
    @CsvSource({
        "false, none",
        "false, email",
        "false, tag",
        "false, qualification",
        "true, none",
        "true, email",
        "true, tag",
        "true, qualification"
    })
    public void execute_findThenRenameJobseeker_refreshesResultsAndPreservesLabel(
            boolean hidden, String remainingMatch) throws Exception {
        JobseekerBuilder builder = new JobseekerBuilder().withName("Roy").withHidden(hidden)
                .withEmail("contact@example.com").withTags();
        if (remainingMatch.equals("email")) {
            builder.withEmail("roy@example.com");
        } else if (remainingMatch.equals("tag")) {
            builder.withTags("roy");
        } else if (remainingMatch.equals("qualification")) {
            builder.withHighestQualification("Royal Engineering");
        }
        Jobseeker roy = builder.build();
        Jobseeker oppositeVisibility = new JobseekerBuilder(roy).withName("Roy Other")
                .withPhone("91234567").withEmail("other@example.com").withHidden(!hidden).build();
        model.addJobseeker(roy);
        model.addJobseeker(oppositeVisibility);
        StringProperty displayedDescription = new SimpleStringProperty();
        displayedDescription.bind(logic.jobseekerFilterDescriptionProperty());
        parser.parseCommand(hidden ? "findhj roy" : "findj roy").execute(model);
        assertEquals(List.of(roy), model.getFilteredJobseekerList());
        String expectedLabel = "Showing " + (hidden ? "hidden" : "unhidden")
                + " jobseekers matching \"roy\"";
        assertEquals(expectedLabel, displayedDescription.get());

        CommandResult result = parser.parseCommand("editj 1 n/John").execute(model);

        Jobseeker john = new JobseekerBuilder(roy).withName("John").build();
        assertEquals(remainingMatch.equals("none") ? List.of() : List.of(john),
                model.getFilteredJobseekerList());
        assertEquals(List.of(john, oppositeVisibility), model.getAddressBook().getJobseekerList());
        assertTrue(result.getFeedbackToUser().contains("John"));
        assertEquals(expectedLabel, displayedDescription.get());
        assertEquals("Showing all unhidden clients",
                logic.clientFilterDescriptionProperty().get());
        parser.parseCommand(hidden ? "listhj" : "listj").execute(model);
        assertEquals(List.of(john), model.getFilteredJobseekerList());
        assertEquals("Showing all " + (hidden ? "hidden" : "unhidden") + " jobseekers",
                displayedDescription.get());
    }

    private void assertDescriptions(String jobseekers, String clients) {
        assertEquals(jobseekers, logic.jobseekerFilterDescriptionProperty().get());
        assertEquals(clients, logic.clientFilterDescriptionProperty().get());
    }
}
