package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Tests that {@link CommandTestUtil#assertCommandFailure(Command, Model, String)} accepts unchanged
 * filtered lists and detects changes to visible jobseekers or clients after a command fails.
 */
public class CommandTestUtilTest {

    private static final String FAILURE_MESSAGE = "Command failed";

    @Test
    public void assertCommandFailure_jobseekerFilterUnchanged_passes() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.updateFilteredJobseekerList(jobseeker -> jobseeker.equals(ALICE));
        assertEquals(List.of(ALICE), model.getFilteredJobseekerList());
        Command command = new Command() {
            @Override
            public CommandResult execute(Model model) throws CommandException {
                throw new CommandException(FAILURE_MESSAGE);
            }
        };

        assertCommandFailure(command, model, FAILURE_MESSAGE);
    }

    @Test
    public void assertCommandFailure_jobseekerFilterChanged_throwsAssertionError() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Command command = new Command() {
            @Override
            public CommandResult execute(Model model) throws CommandException {
                model.updateFilteredJobseekerList(jobseeker -> jobseeker.equals(ALICE));
                throw new CommandException(FAILURE_MESSAGE);
            }
        };

        assertThrows(AssertionError.class, () -> assertCommandFailure(command, model, FAILURE_MESSAGE));
        assertEquals(List.of(ALICE), model.getFilteredJobseekerList());
        assertEquals(getTypicalAddressBook(), model.getAddressBook());
    }

    @Test
    public void assertCommandFailure_clientFilterUnchanged_passes() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.updateFilteredClientList(client -> client.equals(ADRIAN));
        assertEquals(List.of(ADRIAN), model.getFilteredClientList());
        Command command = new Command() {
            @Override
            public CommandResult execute(Model model) throws CommandException {
                throw new CommandException(FAILURE_MESSAGE);
            }
        };

        assertCommandFailure(command, model, FAILURE_MESSAGE);
    }

    @Test
    public void assertCommandFailure_clientFilterChanged_throwsAssertionError() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Command command = new Command() {
            @Override
            public CommandResult execute(Model model) throws CommandException {
                model.updateFilteredClientList(client -> client.equals(ADRIAN));
                throw new CommandException(FAILURE_MESSAGE);
            }
        };

        assertThrows(AssertionError.class, () -> assertCommandFailure(command, model, FAILURE_MESSAGE));
        assertEquals(List.of(ADRIAN), model.getFilteredClientList());
        assertEquals(getTypicalAddressBook(), model.getAddressBook());
    }
}
