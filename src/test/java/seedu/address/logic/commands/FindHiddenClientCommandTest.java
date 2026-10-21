package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_HIDDEN_CLIENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.showJobseekerAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
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
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.testutil.ClientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code FindHiddenClientCommand}. Keyword matching itself (partial words, phone numbers, emails) is
 * shared with {@code findc} and covered by {@code FindClientCommandTest}.
 */
public class FindHiddenClientCommandTest {

    private final Client hiddenBrenda = new ClientBuilder(BRENDA).withHidden(true).build();
    private final Client hiddenCedric = new ClientBuilder(CEDRIC).withHidden(true).build();

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FindHiddenClientCommand(null));
    }

    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(List.of("first"));
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(List.of("second"));

        FindHiddenClientCommand findFirstCommand = new FindHiddenClientCommand(firstPredicate);
        FindHiddenClientCommand findSecondCommand = new FindHiddenClientCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        assertTrue(findFirstCommand.equals(new FindHiddenClientCommand(firstPredicate)));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // findc with the same predicate -> returns false, as it searches unhidden clients instead
        assertFalse(findFirstCommand.equals(new FindClientCommand(firstPredicate)));
    }

    @Test
    public void toStringMethod() {
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("keyword"));
        FindHiddenClientCommand findCommand = new FindHiddenClientCommand(predicate);
        String expected = FindHiddenClientCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * An unhidden client must not be found, even though the keywords match them.
     */
    @Test
    public void execute_keywordsMatchHiddenAndUnhidden_onlyHiddenFound() {
        assertFindSuccess(prepareModel(), "Adrian Brenda", List.of(hiddenBrenda));
    }

    @Test
    public void execute_multipleKeywords_allMatchingHiddenClientsFound() {
        assertFindSuccess(prepareModel(), "Brenda Cedric", List.of(hiddenBrenda, hiddenCedric));
    }

    @Test
    public void execute_keywordMatchesOnlyUnhidden_noClientFound() {
        assertFindSuccess(prepareModel(), "Adrian", List.of());
    }

    /**
     * Follows what a recruiter does: hide a client with {@code hidec}, then search for them
     * with this command.
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
     * Only the client list is searched; the jobseeker list must keep showing what it showed before.
     * The keywords match the jobseekers, so searching the wrong list would also show up, and the
     * test starts from a search result, so it fails on a reset of the jobseeker list too.
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
     * Parses {@code userInput} into a {@code PersonContainsKeywordsPredicate}.
     */
    private PersonContainsKeywordsPredicate preparePredicate(String userInput) {
        return new PersonContainsKeywordsPredicate(List.of(userInput.split("\\s+")));
    }
}
