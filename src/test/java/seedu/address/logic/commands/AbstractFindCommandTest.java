package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Contains unit tests for the behavior every find command inherits from {@code AbstractFindCommand}:
 * the null check in its constructor, {@code equals} and {@code toString}.
 * Each test case runs against every find command, so it fails if any of them changes this behavior.
 * The test classes of the individual find commands therefore only need to cover {@code execute}.
 */
public class AbstractFindCommandTest {

    /**
     * Constructors of every find command, which the test cases in this class run against.
     * If there are any new types of find commands in future, they must be added here by hand: nothing
     * checks that this list is complete, so a command left out would not be covered by these test cases.
     */
    private static final List<Function<PersonContainsKeywordsPredicate, AbstractFindCommand>>
            FIND_COMMAND_FACTORIES = List.of(FindCommand::new, FindJobseekerCommand::new,
                    FindClientCommand::new, FindHiddenJobseekerCommand::new, FindHiddenClientCommand::new);

    private final PersonContainsKeywordsPredicate predicate =
            new PersonContainsKeywordsPredicate(List.of("alice"));

    /**
     * Returns one of each find command, all built from the same {@code predicate}.
     */
    private List<AbstractFindCommand> createOneOfEachFindCommand(PersonContainsKeywordsPredicate predicate) {
        return FIND_COMMAND_FACTORIES.stream()
                .map(factory -> factory.apply(predicate))
                .toList();
    }

    /**
     * Checks that no find command can be created with a null predicate.
     * Every find command passes its predicate to the {@code AbstractFindCommand} constructor, so this
     * fails if the null check there is removed.
     */
    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        FIND_COMMAND_FACTORIES.forEach(factory ->
                assertThrows(NullPointerException.class, () -> factory.apply(null)));
    }

    /**
     * Checks that find commands of different kinds are not equal, even when their keywords are the same.
     * For example, {@code findj alice} and {@code findc alice} search different lists. This test case
     * guards the {@code getClass()} comparison in {@code equals}: it fails if that comparison is
     * replaced by {@code instanceof AbstractFindCommand}.
     * Each command must be equal to exactly one of {@code commands}: itself.
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
     * equal predicate. This fails if {@code equals} compares object identity rather than the predicates.
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

    /**
     * Checks that two commands of the same kind are not equal when their keywords differ.
     * For example, {@code findj alice} and {@code findj bob} must not be equal. This fails if
     * {@code equals} only checks that both commands are the same class and ignores the keywords.
     */
    @Test
    public void equals_sameFindCommandWithDifferentPredicate_returnsFalse() {
        PersonContainsKeywordsPredicate otherPredicate = new PersonContainsKeywordsPredicate(List.of("bob"));
        List<AbstractFindCommand> commands = createOneOfEachFindCommand(predicate);
        List<AbstractFindCommand> otherCommands = createOneOfEachFindCommand(otherPredicate);
        for (int i = 0; i < commands.size(); i++) {
            assertFalse(commands.get(i).equals(otherCommands.get(i)));
        }
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        for (AbstractFindCommand command : createOneOfEachFindCommand(predicate)) {
            assertTrue(command.equals(command));
        }
    }

    /**
     * Checks that a find command is not equal to an object of another type, such as the number 1.
     * If {@code equals} did not check the class first, it would try to cast that object to a find
     * command and throw {@code ClassCastException} instead of returning false.
     */
    @Test
    public void equals_differentType_returnsFalse() {
        for (AbstractFindCommand command : createOneOfEachFindCommand(predicate)) {
            assertFalse(command.equals(1));
        }
    }

    @Test
    public void equals_null_returnsFalse() {
        for (AbstractFindCommand command : createOneOfEachFindCommand(predicate)) {
            assertFalse(command.equals(null));
        }
    }

    /**
     * Checks that {@code toString} gives the concrete find command's class name, not
     * {@code AbstractFindCommand}, followed by the predicate, so the output tells find commands apart.
     */
    @Test
    public void toString_anyFindCommand_namesConcreteClass() {
        for (AbstractFindCommand command : createOneOfEachFindCommand(predicate)) {
            String expected = command.getClass().getCanonicalName() + "{predicate=" + predicate + "}";
            assertEquals(expected, command.toString());
        }
    }
}
