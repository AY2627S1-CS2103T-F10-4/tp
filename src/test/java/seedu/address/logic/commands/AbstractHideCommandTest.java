package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

/** Tests the behavior shared by the four hide and unhide commands. */
public class AbstractHideCommandTest {
    private static final String INVALID_INDEX_MESSAGE = Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX;

    private List<AbstractHideCommand> createCommands() {
        Index index = Index.fromOneBased(1);
        return List.of(new HideClientCommand(index), new HideJobseekerCommand(index),
                new UnhideClientCommand(index), new UnhideJobseekerCommand(index));
    }

    /**
     * Guards the {@code getClass()} comparison in {@code equals}: with {@code instanceof}, a
     * {@code hidec 1} would wrongly equal a {@code hidej 1}.
     */
    @Test
    public void equals_sameArguments_distinguishesAllCommandTypes() {
        List<AbstractHideCommand> commands = createCommands();
        List<AbstractHideCommand> equalCommands = createCommands();
        for (int i = 0; i < commands.size(); i++) {
            for (int j = 0; j < equalCommands.size(); j++) {
                if (i == j) {
                    assertEquals(commands.get(i), equalCommands.get(j));
                    assertEquals(commands.get(i).hashCode(), equalCommands.get(j).hashCode());
                } else {
                    assertNotEquals(commands.get(i), equalCommands.get(j));
                }
            }
        }
    }

    /** Guards the operation comparison in {@code equals}, which the concrete classes alone cannot reach. */
    @Test
    public void equals_sameClassAndIndexWithDifferentOperations_returnsFalse() {
        AbstractHideCommand hideCommand = new TestHideCommand(AbstractHideCommand.HideOperation.HIDE);
        AbstractHideCommand unhideCommand = new TestHideCommand(AbstractHideCommand.HideOperation.UNHIDE);

        assertNotEquals(hideCommand, unhideCommand);
        assertNotEquals(unhideCommand, hideCommand);
        assertEquals(hideCommand, new TestHideCommand(AbstractHideCommand.HideOperation.HIDE));
    }

    @Test
    public void shouldBeHidden_matchesOperation() {
        assertTrue(new TestHideCommand(AbstractHideCommand.HideOperation.HIDE).shouldBeHidden());
        assertFalse(new TestHideCommand(AbstractHideCommand.HideOperation.UNHIDE).shouldBeHidden());
    }

    @Test
    public void isAlreadyApplied_hiddenStatusMatchesOperation_returnsTrue() {
        Person hiddenPerson = new ClientBuilder(ADRIAN).withHidden(true).build();
        Person shownPerson = new JobseekerBuilder(ALICE).withHidden(false).build();
        AbstractHideCommand hideCommand = new TestHideCommand(AbstractHideCommand.HideOperation.HIDE);
        AbstractHideCommand unhideCommand = new TestHideCommand(AbstractHideCommand.HideOperation.UNHIDE);

        assertTrue(hideCommand.isAlreadyApplied(hiddenPerson));
        assertTrue(unhideCommand.isAlreadyApplied(shownPerson));
    }

    @Test
    public void isAlreadyApplied_hiddenStatusDiffersFromOperation_returnsFalse() {
        Person hiddenPerson = new ClientBuilder(ADRIAN).withHidden(true).build();
        Person shownPerson = new JobseekerBuilder(ALICE).withHidden(false).build();
        AbstractHideCommand hideCommand = new TestHideCommand(AbstractHideCommand.HideOperation.HIDE);
        AbstractHideCommand unhideCommand = new TestHideCommand(AbstractHideCommand.HideOperation.UNHIDE);

        assertFalse(hideCommand.isAlreadyApplied(shownPerson));
        assertFalse(unhideCommand.isAlreadyApplied(hiddenPerson));
    }

    @Test
    public void getTarget_indexInRange_returnsPersonAtIndex() throws CommandException {
        Client second = new ClientBuilder().withName("Second").build();
        List<Client> displayedClients = List.of(ADRIAN, second);
        TestHideCommand command = new TestHideCommand(Index.fromOneBased(2));

        assertEquals(second, command.getTarget(displayedClients, INVALID_INDEX_MESSAGE));
    }

    /** The last valid index is size - 1, so an index equal to the size must be rejected. */
    @Test
    public void getTarget_indexEqualToSize_throwsCommandException() {
        TestHideCommand command = new TestHideCommand(Index.fromOneBased(2));

        assertThrows(CommandException.class, INVALID_INDEX_MESSAGE, () ->
                command.getTarget(List.of(ADRIAN), INVALID_INDEX_MESSAGE));
    }

    @Test
    public void constructor_nullOperation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new TestHideCommand((AbstractHideCommand.HideOperation) null));
    }

    /** Allows both operations on the same concrete type to exercise the shared behavior directly. */
    private static class TestHideCommand extends AbstractHideCommand {
        TestHideCommand(HideOperation operation) {
            super(Index.fromOneBased(1), operation);
        }

        TestHideCommand(Index targetIndex) {
            super(targetIndex, HideOperation.HIDE);
        }

        @Override
        public CommandResult execute(Model model) {
            throw new UnsupportedOperationException("This command is only used to test shared behavior.");
        }
    }

}
