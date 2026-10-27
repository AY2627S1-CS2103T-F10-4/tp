package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.DIANA;
import static seedu.address.testutil.TypicalPersons.ETHAN;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FindCommand(null));
    }

    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(List.of("first"));
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(List.of("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        assertTrue(findFirstCommand.equals(new FindCommand(firstPredicate)));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // findj/findc with the same predicate -> returns false
        assertFalse(findFirstCommand.equals(new FindJobseekerCommand(firstPredicate)));
        assertFalse(findFirstCommand.equals(new FindClientCommand(firstPredicate)));
    }

    /**
     * Checks that both lists are filtered: {@code "an"} is part of jobseekers "Daniel" and
     * "anna@example.com" (George), and of clients "Adrian", "Diana" and "Ethan".
     */
    @Test
    public void execute_keywordMatchesBothTypes_jobseekersAndClientsFound() {
        assertFindSuccess("an", List.of(DANIEL, GEORGE), List.of(ADRIAN, DIANA, ETHAN));
    }

    /**
     * Checks that one keyword per list works: "Alice" only matches a jobseeker and "Brenda" only a
     * client, so each list must show just its own match.
     */
    @Test
    public void execute_multipleKeywords_eachListShowsOwnMatches() {
        assertFindSuccess("Alice Brenda", List.of(ALICE), List.of(BRENDA));
    }

    /**
     * Checks that a tag search narrows the client list to nobody when only jobseekers carry the tag,
     * so this fails if {@code find} left the client list unfiltered.
     */
    @Test
    public void execute_tagOnlyOnJobseekers_noClientFound() {
        assertFindSuccess("friends", List.of(ALICE, BENSON, DANIEL), List.of());
    }

    /**
     * Checks the mirror case of {@link #execute_tagOnlyOnJobseekers_noClientFound()}: a tag held only
     * by clients must empty the jobseeker list, so this fails if the jobseeker list were left alone.
     */
    @Test
    public void execute_tagOnlyOnClients_noJobseekerFound() {
        assertFindSuccess("partner", List.of(), List.of(BRENDA, DIANA));
    }

    @Test
    public void execute_tagSharedByBothTypes_jobseekersAndClientsFound() {
        Jobseeker taggedJobseeker = new JobseekerBuilder(ALICE).withTags("technology").build();
        Client taggedClient = new ClientBuilder(ADRIAN).withTags("technology").build();
        Model taggedModel = new ModelManager();
        taggedModel.addJobseeker(taggedJobseeker);
        taggedModel.addJobseeker(BENSON);
        taggedModel.addClient(taggedClient);
        taggedModel.addClient(BRENDA);

        CommandResult result = new FindCommand(preparePredicate("technology")).execute(taggedModel);

        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1, 1), result.getFeedbackToUser());
        assertEquals(List.of(taggedJobseeker), taggedModel.getFilteredJobseekerList());
        assertEquals(List.of(taggedClient), taggedModel.getFilteredClientList());
    }

    /**
     * Hidden jobseekers and clients must not be found, even though the keywords match them, as for
     * {@code findj} and {@code findc}.
     */
    @Test
    public void execute_keywordMatchesHiddenPeople_hiddenPeopleNotFound() {
        Model hiddenModel = new ModelManager();
        hiddenModel.addJobseeker(ALICE);
        hiddenModel.addJobseeker(new JobseekerBuilder(BENSON).withHidden(true).build());
        hiddenModel.addClient(ADRIAN);
        hiddenModel.addClient(new ClientBuilder(BRENDA).withHidden(true).build());

        CommandResult result = new FindCommand(preparePredicate("Alice Benson Adrian Brenda"))
                .execute(hiddenModel);

        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1, 1), result.getFeedbackToUser());
        assertEquals(List.of(ALICE), hiddenModel.getFilteredJobseekerList());
        assertEquals(List.of(ADRIAN), hiddenModel.getFilteredClientList());
    }

    @Test
    public void toStringMethod() {
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Executes a {@code FindCommand} built from {@code userInput} on the typical address book, and
     * checks that exactly {@code expectedJobseekers} and {@code expectedClients} are listed, in that
     * order, with the matching result message.
     */
    private void assertFindSuccess(String userInput, List<Jobseeker> expectedJobseekers,
            List<Client> expectedClients) {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedJobseekers.size(), expectedClients.size());
        PersonContainsKeywordsPredicate predicate = preparePredicate(userInput);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredJobseekerList(predicate);
        expectedModel.updateFilteredClientList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        // ModelManager#equals does not compare filtered lists, so check both lists explicitly.
        assertEquals(expectedJobseekers, model.getFilteredJobseekerList());
        assertEquals(expectedClients, model.getFilteredClientList());
    }

    /**
     * Parses {@code userInput} into a {@code PersonContainsKeywordsPredicate}.
     */
    private PersonContainsKeywordsPredicate preparePredicate(String userInput) {
        return new PersonContainsKeywordsPredicate(List.of(userInput.split("\\s+")));
    }
}
