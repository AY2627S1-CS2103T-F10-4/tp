package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Contains unit tests for the logic that {@code AbstractFindCommand} shares with all find commands.
 * Each find command's own test class checks equality within that command; this class checks
 * equality across the different find commands.
 */
public class AbstractFindCommandTest {

    private final PersonContainsKeywordsPredicate predicate =
            new PersonContainsKeywordsPredicate(List.of("alice"));

    /**
     * Returns one of each find command, all built from the same {@code predicate}.
     */
    private List<AbstractFindCommand> createOneOfEachFindCommand(PersonContainsKeywordsPredicate predicate) {
        return List.of(new FindCommand(predicate), new FindJobseekerCommand(predicate),
                new FindClientCommand(predicate), new FindHiddenJobseekerCommand(predicate),
                new FindHiddenClientCommand(predicate));
    }

    /**
     * Checks that two different find commands are not equal, even when their keywords are the same.
     * For example, {@code findj alice} and {@code findc alice} search different lists. This test case
     * guards the {@code getClass()} comparison in {@code equals}: it fails if that comparison is
     * replaced by {@code instanceof AbstractFindCommand}.
     * Each command must be equal to exactly one command in the list: itself.
     * {@code distinct()} or a {@code Set} would look simpler, but they compare {@code hashCode} first,
     * which find commands do not override, so they would report every command as distinct even if
     * {@code equals} were broken.
     */
    @Test
    public void equals_differentFindCommandsWithSamePredicate_returnsFalse() {
        List<AbstractFindCommand> commands = createOneOfEachFindCommand(predicate);
        for (AbstractFindCommand command : commands) {
            long equalCommandCount = commands.stream()
                    .filter(command::equals)
                    .count();
            assertEquals(1, equalCommandCount, command.toString());
        }
    }

    /**
     * Checks that every find command is equal to a separately built command of the same kind with an
     * equal predicate. Paired with the test case above, this shows that {@code equals} compares the
     * predicate rather than the object identity.
     */
    @Test
    public void equals_sameFindCommandWithEqualPredicate_returnsTrue() {
        PersonContainsKeywordsPredicate equalPredicate = new PersonContainsKeywordsPredicate(List.of("alice"));
        List<AbstractFindCommand> commands = createOneOfEachFindCommand(predicate);
        List<AbstractFindCommand> equalCommands = createOneOfEachFindCommand(equalPredicate);
        for (int i = 0; i < commands.size(); i++) {
            assertTrue(commands.get(i).equals(equalCommands.get(i)));
        }
    }

    @Test
    public void equals_null_returnsFalse() {
        for (AbstractFindCommand command : createOneOfEachFindCommand(predicate)) {
            assertFalse(command.equals(null));
        }
    }

    /**
     * Checks that {@code toString} names the concrete find command, not {@code AbstractFindCommand},
     * so a log line still shows which command was run.
     */
    @Test
    public void toString_anyFindCommand_namesConcreteClass() {
        for (AbstractFindCommand command : createOneOfEachFindCommand(predicate)) {
            String expected = command.getClass().getCanonicalName() + "{predicate=" + predicate + "}";
            assertEquals(expected, command.toString());
        }
    }
}
