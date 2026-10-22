package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Contains integration tests for CopyJobseekerCommand.
 */
public class CopyJobseekerCommandTest {

    @Test
    public void execute_multipleDisplayedJobseekers_copiesDetailsInDisplayOrder() {
        Model model = new ModelManager();
        model.addJobseeker(ALICE);
        model.addJobseeker(BENSON);

        CommandResult result = new CopyJobseekerCommand().execute(model);

        assertEquals(String.format(CopyJobseekerCommand.MESSAGE_SUCCESS, 2), result.getFeedbackToUser());
        assertEquals(String.join(System.lineSeparator(), Messages.format(ALICE), Messages.format(BENSON)),
                result.getClipboardText());
    }

    @Test
    public void execute_filteredJobseekers_copiesOnlyDisplayedJobseekers() {
        Model model = new ModelManager();
        model.addJobseeker(ALICE);
        model.addJobseeker(BENSON);
        model.updateFilteredJobseekerList(jobseeker -> jobseeker.equals(BENSON));

        CommandResult result = new CopyJobseekerCommand().execute(model);

        assertEquals(String.format(CopyJobseekerCommand.MESSAGE_SUCCESS, 1), result.getFeedbackToUser());
        assertEquals(Messages.format(BENSON), result.getClipboardText());
    }

    @Test
    public void execute_noDisplayedJobseekers_copiesEmptyText() {
        Model model = new ModelManager();
        model.updateFilteredJobseekerList(jobseeker -> false);

        CommandResult result = new CopyJobseekerCommand().execute(model);

        assertEquals(String.format(CopyJobseekerCommand.MESSAGE_SUCCESS, 0), result.getFeedbackToUser());
        assertEquals("", result.getClipboardText());
    }
}
