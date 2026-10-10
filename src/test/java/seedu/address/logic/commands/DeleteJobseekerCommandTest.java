package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteJobseekerCommand}.
 */
public class DeleteJobseekerCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Jobseeker jobseekerToDelete = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteJobseekerCommand deleteCommand = new DeleteJobseekerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteJobseekerCommand.MESSAGE_DELETE_JOBSEEKER_SUCCESS,
                Messages.format(jobseekerToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteJobseeker(jobseekerToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredJobseekerList().size() + 1);
        DeleteJobseekerCommand deleteCommand = new DeleteJobseekerCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Jobseeker jobseekerToDelete = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteJobseekerCommand deleteCommand = new DeleteJobseekerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteJobseekerCommand.MESSAGE_DELETE_JOBSEEKER_SUCCESS,
                Messages.format(jobseekerToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteJobseeker(jobseekerToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getJobseekerList().size());

        DeleteJobseekerCommand deleteCommand = new DeleteJobseekerCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteJobseekerCommand deleteFirstCommand = new DeleteJobseekerCommand(INDEX_FIRST_PERSON);
        DeleteJobseekerCommand deleteSecondCommand = new DeleteJobseekerCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteJobseekerCommand deleteFirstCommandCopy = new DeleteJobseekerCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different jobseeker -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteJobseekerCommand deleteCommand = new DeleteJobseekerCommand(targetIndex);
        String expected = DeleteJobseekerCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered jobseeker list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredJobseekerList(p -> false);

        assertTrue(model.getFilteredJobseekerList().isEmpty());
    }
}
