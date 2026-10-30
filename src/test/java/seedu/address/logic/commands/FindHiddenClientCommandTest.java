package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_HIDDEN_CLIENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.preparePredicate;
import static seedu.address.logic.commands.CommandTestUtil.showJobseekerAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.CEDRIC;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.testutil.ClientBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindHiddenClientCommand}.
 * Keyword matching itself (partial words, phone numbers, emails) is shared with {@code findc} and
 * covered by {@code FindClientCommandTest}.
 */
public class FindHiddenClientCommandTest {

    private final Client hiddenBrenda = new ClientBuilder(BRENDA).withHidden(true).build();
    private final Client hiddenCedric = new ClientBuilder(CEDRIC).withHidden(true).build();

    /**
     * Returns a model with Adrian, who is unhidden, and Brenda and Cedric, who are hidden.
     */
    private Model prepareModel() {
        Model model = new ModelManager();
        model.addClient(ADRIAN);
        model.addClient(hiddenBrenda);
        model.addClient(hiddenCedric);
        return model;
    }

    /**
     * Executes a {@code FindHiddenClientCommand} built from {@code userInput} on {@code model}, and
     * checks that exactly {@code expectedClients} are listed, in that order, with the matching result
     * message.
     */
    private void assertFindSuccess(Model model, String userInput, List<Client> expectedClients) {
        CommandResult result = new FindHiddenClientCommand(preparePredicate(userInput)).execute(model);

        assertEquals(String.format(MESSAGE_HIDDEN_CLIENTS_LISTED_OVERVIEW, expectedClients.size()),
                result.getFeedbackToUser());
        assertEquals(expectedClients, model.getFilteredClientList());
    }

    /**
     * An unhidden client must not be found, even though the keywords match them.
     */
    @Test
    public void execute_keywordsMatchHiddenAndUnhidden_onlyHiddenFound() {
        assertFindSuccess(prepareModel(), "Adrian Brenda", List.of(hiddenBrenda));
    }

    /**
     * Adrian and Brenda are both tagged "employer", but only Brenda is hidden, so a tag search must
     * still leave out unhidden clients.
     */
    @Test
    public void execute_tagMatchesHiddenAndUnhidden_onlyHiddenFound() {
        assertFindSuccess(prepareModel(), "employer", List.of(hiddenBrenda));
    }

    /**
     * Checks that every hidden client matching any of the keywords is listed.
     * {@code "Brenda"} and {@code "Cedric"} each match a different hidden client.
     */
    @Test
    public void execute_multipleKeywords_allMatchingHiddenClientsFound() {
        assertFindSuccess(prepareModel(), "Brenda Cedric", List.of(hiddenBrenda, hiddenCedric));
    }

    /**
     * Checks that nobody is listed when the keyword matches only an unhidden client.
     * {@code "Adrian"} matches Adrian, who is not hidden, so {@code findhc} must not list them.
     */
    @Test
    public void execute_keywordMatchesOnlyUnhidden_noClientFound() {
        assertFindSuccess(prepareModel(), "Adrian", List.of());
    }

    /**
     * Checks that a client hidden with {@code hidec} can then be found with {@code findhc}, which is
     * how a recruiter would use the two commands together.
     */
    @Test
    public void execute_afterHidec_findsHiddenClient() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Client clientToHide = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());
        new HideClientCommand(INDEX_FIRST_PERSON).execute(model);

        Client hiddenClient = new ClientBuilder(clientToHide).withHidden(true).build();
        assertFindSuccess(model, "Adrian", List.of(hiddenClient));
    }

    /**
     * Checks that {@code findhc} searches only the client list and leaves the jobseeker list as it was.
     * The jobseeker list starts narrowed down to Benson, so this fails if the command resets it to show
     * all jobseekers. The keywords match both jobseekers, so this also fails if the command searches
     * the jobseeker list by mistake.
     */
    @Test
    public void execute_jobseekerListFiltered_jobseekerListUnchanged() {
        Model model = new ModelManager();
        model.addJobseeker(ALICE);
        model.addJobseeker(BENSON);
        showJobseekerAtIndex(model, INDEX_SECOND_PERSON);

        assertFindSuccess(model, "Alice Benson", List.of());
        assertEquals(List.of(BENSON), model.getFilteredJobseekerList());
    }
}
