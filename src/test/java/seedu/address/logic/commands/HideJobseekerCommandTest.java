package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showJobseekerAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code HideJobseekerCommand}.
 */
public class HideJobseekerCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new HideJobseekerCommand(null));
    }

    /**
     * Hiding must leave the jobseeker in the address book, unlike deleting, and take them out of the
     * displayed list. Model equality ignores the displayed list, so that is checked separately.
     */
    @Test
    public void execute_validIndexUnfilteredList_success() {
        Jobseeker jobseekerToHide = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());
        Jobseeker hiddenJobseeker = new JobseekerBuilder(jobseekerToHide).withHidden(true).build();
        HideJobseekerCommand hideCommand = new HideJobseekerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(HideJobseekerCommand.MESSAGE_HIDE_JOBSEEKER_SUCCESS,
                Messages.format(hiddenJobseeker));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setJobseeker(jobseekerToHide, hiddenJobseeker);

        assertCommandSuccess(hideCommand, model, expectedMessage, expectedModel);
        assertTrue(model.getAddressBook().getJobseekerList().contains(hiddenJobseeker));
        assertTrue(model.getFilteredJobseekerList().stream().noneMatch(jobseekerToHide::isSamePerson));
    }

    /**
     * Hiding the only jobseeker in a search result must empty that result, as a search never shows
     * hidden jobseekers.
     */
    @Test
    public void execute_validIndexFilteredList_success() {
        showJobseekerAtIndex(model, INDEX_FIRST_PERSON);

        Jobseeker jobseekerToHide = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());
        Jobseeker hiddenJobseeker = new JobseekerBuilder(jobseekerToHide).withHidden(true).build();
        HideJobseekerCommand hideCommand = new HideJobseekerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(HideJobseekerCommand.MESSAGE_HIDE_JOBSEEKER_SUCCESS,
                Messages.format(hiddenJobseeker));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setJobseeker(jobseekerToHide, hiddenJobseeker);

        assertCommandSuccess(hideCommand, model, expectedMessage, expectedModel);
        assertTrue(model.getFilteredJobseekerList().isEmpty());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredJobseekerList().size() + 1);
        HideJobseekerCommand hideCommand = new HideJobseekerCommand(outOfBoundIndex);

        assertCommandFailure(hideCommand, model, Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showJobseekerAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of the address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getJobseekerList().size());

        HideJobseekerCommand hideCommand = new HideJobseekerCommand(outOfBoundIndex);

        assertCommandFailure(hideCommand, model, Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    /**
     * Hiding a jobseeker who is already hidden is not an error, as with tagging a jobseeker with a tag
     * they already have, but it must change nothing. The default view never shows hidden jobseekers,
     * so the test shows everyone first, as a view of hidden jobseekers would.
     */
    @Test
    public void execute_jobseekerAlreadyHidden_noChange() {
        Jobseeker hiddenJobseeker = new JobseekerBuilder(ALICE).withHidden(true).build();
        Model hiddenModel = new ModelManager();
        hiddenModel.addJobseeker(hiddenJobseeker);
        hiddenModel.updateFilteredJobseekerList(jobseeker -> true);
        Model expectedModel = new ModelManager(hiddenModel.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new HideJobseekerCommand(INDEX_FIRST_PERSON), hiddenModel,
                HideJobseekerCommand.MESSAGE_NO_CHANGE, expectedModel);
        assertEquals(List.of(hiddenJobseeker), hiddenModel.getFilteredJobseekerList());
    }

    @Test
    public void equals() {
        HideJobseekerCommand hideFirstCommand = new HideJobseekerCommand(INDEX_FIRST_PERSON);
        HideJobseekerCommand hideSecondCommand = new HideJobseekerCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(hideFirstCommand.equals(hideFirstCommand));

        // same values -> returns true
        assertTrue(hideFirstCommand.equals(new HideJobseekerCommand(INDEX_FIRST_PERSON)));

        // different types -> returns false
        assertFalse(hideFirstCommand.equals(1));

        // null -> returns false
        assertFalse(hideFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(hideFirstCommand.equals(hideSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        HideJobseekerCommand hideCommand = new HideJobseekerCommand(targetIndex);
        String expected = HideJobseekerCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, hideCommand.toString());
    }
}
