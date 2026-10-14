package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_JOBSEEKERS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindJobseekerCommand}.
 */
public class FindJobseekerCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Helper method that executes a {@code FindJobseekerCommand} built from {@code userInput} on the
     * typical jobseekers, and checks that exactly {@code expectedJobseekers} are listed, in that order,
     * with the matching result message.
     */
    private void assertFindSuccess(String userInput, List<Jobseeker> expectedJobseekers) {
        String expectedMessage = String.format(MESSAGE_JOBSEEKERS_LISTED_OVERVIEW, expectedJobseekers.size());
        NameContainsKeywordsPredicate predicate = preparePredicate(userInput);
        FindJobseekerCommand command = new FindJobseekerCommand(predicate);
        expectedModel.updateFilteredJobseekerList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedJobseekers, model.getFilteredJobseekerList());
    }

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FindJobseekerCommand(null));
    }

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(List.of("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(List.of("second"));

        FindJobseekerCommand findFirstCommand = new FindJobseekerCommand(firstPredicate);
        FindJobseekerCommand findSecondCommand = new FindJobseekerCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindJobseekerCommand findFirstCommandCopy = new FindJobseekerCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different jobseeker -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        assertFindSuccess(" ", List.of());
    }

    @Test
    public void execute_multipleNameKeywords_personsFound() {
        assertFindSuccess("Kurz Elle Kunz", List.of(CARL, ELLE, FIONA));
    }

    /**
     * Checks that a keyword matching only part of a jobseeker's name still finds those jobseekers.
     * {@code "eier"} is not a whole word in any name but lies inside "Meier", so exactly
     * Benson Meier and Daniel Meier are listed. This test case guards substring matching: it fails
     * if {@code findj} reverts to matching whole words only.
     */
    @Test
    public void execute_partialNameKeyword_personsFound() {
        assertFindSuccess("eier", List.of(BENSON, DANIEL));
    }

    /**
     * Checks that a jobseeker whose name is close to the keyword, but does not contain it, is not listed.
     * Elle Meyer is one letter away from "Meier" and does not contain {@code "eier"}, so this test case
     * fails if matching is loosened, for example to fuzzy or typo-tolerant matching.
     * On its own, this test case would also pass if nothing were found, so it relies on
     * {@link #execute_partialNameKeyword_personsFound()} to check that matching still works.
     */
    @Test
    public void execute_partialNameKeyword_nearMissNotFound() {
        FindJobseekerCommand command = new FindJobseekerCommand(preparePredicate("eier"));
        command.execute(model);
        assertFalse(model.getFilteredJobseekerList().contains(ELLE));
    }

    @Test
    public void execute_partialPhoneKeyword_personFound() {
        assertFindSuccess("9435", List.of(ALICE));
    }

    @Test
    public void execute_partialEmailKeyword_personFound() {
        assertFindSuccess("cornelia", List.of(DANIEL));
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("keyword"));
        FindJobseekerCommand findCommand = new FindJobseekerCommand(predicate);
        String expected = FindJobseekerCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(List.of(userInput.split("\\s+")));
    }
}
