package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_CLIENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.CEDRIC;
import static seedu.address.testutil.TypicalPersons.DIANA;
import static seedu.address.testutil.TypicalPersons.ETHAN;
import static seedu.address.testutil.TypicalPersons.FELICIA;
import static seedu.address.testutil.TypicalPersons.GAVIN;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalJobseekers;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindClientCommand}.
 */
public class FindClientCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FindClientCommand(null));
    }

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(List.of("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(List.of("second"));

        FindClientCommand findFirstCommand = new FindClientCommand(firstPredicate);
        FindClientCommand findSecondCommand = new FindClientCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindClientCommand findFirstCommandCopy = new FindClientCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // jobseeker command with the same predicate -> returns false
        assertFalse(findFirstCommand.equals(new FindJobseekerCommand(firstPredicate)));
    }

    @Test
    public void execute_zeroKeywords_noClientFound() {
        assertFindSuccess(" ", List.of());
    }

    @Test
    public void execute_multipleNameKeywords_clientsFound() {
        assertFindSuccess("Tan Lim Koh", List.of(ADRIAN, BRENDA, GAVIN));
    }

    /**
     * Checks that substring matching applies to clients too: {@code "an"} is part of
     * "Adrian", "Diana" and "Ethan".
     */
    @Test
    public void execute_partialNameKeyword_clientsFound() {
        assertFindSuccess("an", List.of(ADRIAN, DIANA, ETHAN));
    }

    @Test
    public void execute_partialPhoneKeyword_clientFound() {
        assertFindSuccess("0003", List.of(CEDRIC));
    }

    @Test
    public void execute_partialEmailKeyword_clientFound() {
        assertFindSuccess("felicia@", List.of(FELICIA));
    }

    /**
     * Checks that {@code findc} leaves the jobseeker list alone. {@code ModelManager#equals} does not
     * compare filtered lists, so {@code assertCommandSuccess} alone would not notice if this command
     * filtered jobseekers by mistake. {@code "Alice"} matches a jobseeker but no client, so a mix-up
     * would also show up as a non-empty client list.
     */
    @Test
    public void execute_jobseekerKeyword_jobseekerListUnchanged() {
        assertFindSuccess("Alice", List.of());
        assertEquals(getTypicalJobseekers(), model.getFilteredJobseekerList());
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("keyword"));
        FindClientCommand findCommand = new FindClientCommand(predicate);
        String expected = FindClientCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Executes a {@code FindClientCommand} built from {@code userInput} on the typical address book,
     * and checks that exactly {@code expectedClients} are listed, in that order, with the matching
     * result message.
     */
    private void assertFindSuccess(String userInput, List<Client> expectedClients) {
        String expectedMessage = String.format(MESSAGE_CLIENTS_LISTED_OVERVIEW, expectedClients.size());
        NameContainsKeywordsPredicate predicate = preparePredicate(userInput);
        FindClientCommand command = new FindClientCommand(predicate);
        expectedModel.updateFilteredClientList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedClients, model.getFilteredClientList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(List.of(userInput.split("\\s+")));
    }
}
