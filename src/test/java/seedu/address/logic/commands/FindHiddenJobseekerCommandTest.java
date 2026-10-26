package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_HIDDEN_JOBSEEKERS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code FindHiddenJobseekerCommand}. Keyword matching itself (partial words, phone numbers, emails) is
 * shared with {@code findj} and covered by {@code FindJobseekerCommandTest}.
 */
public class FindHiddenJobseekerCommandTest {

    private final Jobseeker hiddenBenson = new JobseekerBuilder(BENSON).withHidden(true).build();
    private final Jobseeker hiddenCarl = new JobseekerBuilder(CARL).withHidden(true).build();

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FindHiddenJobseekerCommand(null));
    }

    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(List.of("first"));
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(List.of("second"));

        FindHiddenJobseekerCommand findFirstCommand = new FindHiddenJobseekerCommand(firstPredicate);
        FindHiddenJobseekerCommand findSecondCommand = new FindHiddenJobseekerCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        assertTrue(findFirstCommand.equals(new FindHiddenJobseekerCommand(firstPredicate)));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // findj with the same predicate -> returns false, as it searches unhidden jobseekers instead
        assertFalse(findFirstCommand.equals(new FindJobseekerCommand(firstPredicate)));
    }

    @Test
    public void toStringMethod() {
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("keyword"));
        FindHiddenJobseekerCommand findCommand = new FindHiddenJobseekerCommand(predicate);
        String expected = FindHiddenJobseekerCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * An unhidden jobseeker must not be found, even though the keywords match them.
     */
    @Test
    public void execute_keywordsMatchHiddenAndUnhidden_onlyHiddenFound() {
        assertFindSuccess(prepareModel(), "Alice Benson", List.of(hiddenBenson));
    }

    @Test
    public void execute_multipleKeywords_allMatchingHiddenJobseekersFound() {
        assertFindSuccess(prepareModel(), "Benson Carl", List.of(hiddenBenson, hiddenCarl));
    }

    /**
     * Alice and Benson are both tagged "friends", but only Benson is hidden, so a tag search must
     * still leave out unhidden jobseekers.
     */
    @Test
    public void execute_tagMatchesHiddenAndUnhidden_onlyHiddenFound() {
        assertFindSuccess(prepareModel(), "friends", List.of(hiddenBenson));
    }

    @Test
    public void execute_keywordMatchesOnlyUnhidden_noJobseekerFound() {
        assertFindSuccess(prepareModel(), "Alice", List.of());
    }

    /**
     * Follows what a recruiter does: hide a jobseeker with {@code hidej}, then search for them
     * with this command.
     */
    @Test
    public void execute_afterHidej_findsHiddenJobseeker() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Jobseeker jobseekerToHide = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());
        new HideJobseekerCommand(INDEX_FIRST_PERSON).execute(model);

        Jobseeker hiddenJobseeker = new JobseekerBuilder(jobseekerToHide).withHidden(true).build();
        assertFindSuccess(model, "Alice", List.of(hiddenJobseeker));
    }

    /**
     * Only the jobseeker list is searched; the client list must keep showing what it showed before.
     * The keywords match the clients, so searching the wrong list would also show up, and the
     * test starts from a search result, so it fails on a reset of the client list too.
     */
    @Test
    public void execute_clientListFiltered_clientListUnchanged() {
        Model model = new ModelManager();
        model.addClient(ADRIAN);
        model.addClient(BRENDA);
        showClientAtIndex(model, INDEX_SECOND_PERSON);

        assertFindSuccess(model, "Adrian Brenda", List.of());
        assertEquals(List.of(BRENDA), model.getFilteredClientList());
    }

    /**
     * Returns a model with Alice, who is unhidden, and Benson and Carl, who are hidden.
     */
    private Model prepareModel() {
        Model model = new ModelManager();
        model.addJobseeker(ALICE);
        model.addJobseeker(hiddenBenson);
        model.addJobseeker(hiddenCarl);
        return model;
    }

    /**
     * Executes a {@code FindHiddenJobseekerCommand} built from {@code userInput} on {@code model}, and
     * checks that exactly {@code expectedJobseekers} are listed, in that order, with the matching result
     * message.
     */
    private void assertFindSuccess(Model model, String userInput, List<Jobseeker> expectedJobseekers) {
        CommandResult result = new FindHiddenJobseekerCommand(preparePredicate(userInput)).execute(model);

        assertEquals(String.format(MESSAGE_HIDDEN_JOBSEEKERS_LISTED_OVERVIEW, expectedJobseekers.size()),
                result.getFeedbackToUser());
        assertEquals(expectedJobseekers, model.getFilteredJobseekerList());
    }

    /**
     * Parses {@code userInput} into a {@code PersonContainsKeywordsPredicate}.
     */
    private PersonContainsKeywordsPredicate preparePredicate(String userInput) {
        return new PersonContainsKeywordsPredicate(List.of(userInput.split("\\s+")));
    }
}
