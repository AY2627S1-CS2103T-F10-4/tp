package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showJobseekerAtIndex;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;

/**
 * Contains integration tests for CopyJobseekerCommand.
 */
public class CopyJobseekerCommandTest {

    @Test
    public void execute_multipleDisplayedJobseekers_copiesDetailsInDisplayOrder() throws Exception {
        // EP: no index supplied; all displayed jobseekers are copied in display order.
        Model model = new ModelManager();
        model.addJobseeker(ALICE);
        model.addJobseeker(BENSON);

        CommandResult result = new CopyJobseekerCommand().execute(model);

        assertEquals(String.format(CopyJobseekerCommand.MESSAGE_SUCCESS, 2), result.getFeedbackToUser());
        assertEquals(String.join(System.lineSeparator(), Messages.format(ALICE), Messages.format(BENSON)),
                result.getClipboardText());
    }

    @Test
    public void execute_filteredJobseekers_copiesOnlyDisplayedJobseekers() throws Exception {
        Model model = new ModelManager();
        model.addJobseeker(ALICE);
        model.addJobseeker(BENSON);
        model.updateFilteredJobseekerList(jobseeker -> jobseeker.equals(BENSON));

        CommandResult result = new CopyJobseekerCommand().execute(model);

        assertEquals(String.format(CopyJobseekerCommand.MESSAGE_SUCCESS, 1), result.getFeedbackToUser());
        assertEquals(Messages.format(BENSON), result.getClipboardText());
    }

    @Test
    public void execute_noDisplayedJobseekers_copiesEmptyText() throws Exception {
        // EP: empty displayed list
        Model model = new ModelManager();
        model.updateFilteredJobseekerList(jobseeker -> false);

        CommandResult result = new CopyJobseekerCommand().execute(model);

        assertEquals(String.format(CopyJobseekerCommand.MESSAGE_SUCCESS, 0), result.getFeedbackToUser());
        assertEquals("", result.getClipboardText());
    }

    @Test
    public void execute_validDisplayedIndex_copiesSelectedJobseeker() throws Exception {
        // EP: valid displayed index within the unfiltered list
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Jobseeker jobseekerToCopy = model.getFilteredJobseekerList().get(INDEX_SECOND_PERSON.getZeroBased());

        CommandResult result = new CopyJobseekerCommand(INDEX_SECOND_PERSON).execute(model);

        assertEquals(CopyJobseekerCommand.MESSAGE_SUCCESS_SINGLE, result.getFeedbackToUser());
        assertEquals(Messages.format(jobseekerToCopy), result.getClipboardText());
    }

    @Test
    public void execute_validDisplayedIndexAfterFiltering_copiesSelectedJobseeker() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showJobseekerAtIndex(model, INDEX_FIRST_PERSON);

        // EP: valid index within a filtered displayed list; indexing is relative to the display.
        CommandResult result = new CopyJobseekerCommand(INDEX_FIRST_PERSON).execute(model);

        assertEquals(CopyJobseekerCommand.MESSAGE_SUCCESS_SINGLE, result.getFeedbackToUser());
        assertEquals(Messages.format(model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased())),
                result.getClipboardText());
    }

    @Test
    public void execute_indexOutsideDisplayedRange_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // EP: positive index immediately above the displayed-list upper boundary
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredJobseekerList().size() + 1);

        assertCommandFailure(new CopyJobseekerCommand(outOfBoundsIndex), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_indexInAddressBookButOutsideFilteredRange_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showJobseekerAtIndex(model, INDEX_FIRST_PERSON);

        // EP: index valid for the address book but outside the filtered displayed-list partition
        assertCommandFailure(new CopyJobseekerCommand(INDEX_SECOND_PERSON), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }
}
