package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_JOBSEEKERS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
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
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindJobseekerCommand}.
 */
public class FindJobseekerCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

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
        String expectedMessage = String.format(MESSAGE_JOBSEEKERS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindJobseekerCommand command = new FindJobseekerCommand(predicate);
        expectedModel.updateFilteredJobseekerList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(List.of(), model.getFilteredJobseekerList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_JOBSEEKERS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindJobseekerCommand command = new FindJobseekerCommand(predicate);
        expectedModel.updateFilteredJobseekerList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(List.of(CARL, ELLE, FIONA), model.getFilteredJobseekerList());
    }

    /**
     * Checks that a keyword matching only part of a jobseeker's name still finds those jobseekers.
     * {@code "eier"} is not a whole word in any name but lies inside "Meier", so exactly
     * Benson Meier and Daniel Meier are listed. This test case guards substring matching: it fails
     * if {@code findj} reverts to matching whole words only.
     */
    @Test
    public void execute_partialKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_JOBSEEKERS_LISTED_OVERVIEW, 2);
        NameContainsKeywordsPredicate predicate = preparePredicate("eier");
        FindJobseekerCommand command = new FindJobseekerCommand(predicate);
        expectedModel.updateFilteredJobseekerList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(List.of(BENSON, DANIEL), model.getFilteredJobseekerList());
    }

    /**
     * Checks that a jobseeker whose name is close to the keyword, but does not contain it, is not listed.
     * Elle Meyer is one letter away from "Meier" and does not contain {@code "eier"}, so this test case
     * fails if matching is loosened, for example to fuzzy or typo-tolerant matching.
     * On its own, this test case would also pass if nothing were found, so it relies on
     * {@link #execute_partialKeyword_multiplePersonsFound()} to check that matching still works.
     */
    @Test
    public void execute_partialKeyword_nearMissNotFound() {
        FindJobseekerCommand command = new FindJobseekerCommand(preparePredicate("eier"));
        command.execute(model);
        assertFalse(model.getFilteredJobseekerList().contains(ELLE));
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
